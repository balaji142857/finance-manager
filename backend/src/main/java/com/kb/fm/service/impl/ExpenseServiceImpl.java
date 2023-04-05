package com.kb.fm.service.impl;

import com.kb.fm.entities.*;
import com.kb.fm.exceptions.FinanceManagerException;
import com.kb.fm.repo.ExpenseRepository;
import com.kb.fm.repo.FileImportTrackerRepo;
import com.kb.fm.service.AssetService;
import com.kb.fm.service.CategoryService;
import com.kb.fm.service.ExpenseService;
import com.kb.fm.service.FileImportTrackerService;
import com.kb.fm.specs.ExpenseSearchSpecification;
import com.kb.fm.specs.PaginationHelper;
import com.kb.fm.specs.SpecificationHelperUtil;
import com.kb.fm.util.DateUtil;
import com.kb.fm.web.model.ChartData;
import com.kb.fm.web.model.ExpenseSearchModel;
import com.kb.fm.web.model.SearchModel;
import com.kb.fm.web.model.SearchResponseModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.kb.fm.specs.SpecificationHelperUtil.addConditionForNumberRange;
import static com.kb.fm.specs.SpecificationHelperUtil.addConditionsForDateRage;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
	
	private static final String FIELD_AMOUNT = "amount";
	private static final String FIELD_TRANSACTION_DATE = "transactionDate";

	@PersistenceContext
	private EntityManager entityManager;

	private final ExpenseRepository repo;
	private final AssetService assetService;
	private final CategoryService catService;
	private final FileImportTrackerService importTrackerService;
	private final FileImportTrackerRepo fileRepo;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void addExpenses(List<com.kb.fm.web.model.ExpenseModel> expenses) {
		List<Expense> entities = expenses.stream().map(this::modelToEntity).toList();
		List<Long> fileIds = entities.stream()
				.map(Expense::getFile)
				.filter(Objects::nonNull)
				.map(FileImportMetadata::getId)
				.distinct()
				.toList();
		importTrackerService.trackVerification(fileIds);
		repo.saveAll(entities);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<com.kb.fm.web.model.ExpenseModel> listExpenses() {
		return this.getAllExpenses().stream().map(this::entityToModel).toList();
	}
	
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<Expense> getAllExpenses() {
		//TODO include a max size - do not dump out everything in a single call
		return repo.findAllByOrderByTransactionDate();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteExpense(Long id) {
		Expense exp = repo.getReferenceById(id);
		exp.getAsset().setUsage(exp.getAsset().getUsage().subtract(exp.getAmount()));
		exp.getAsset().getExpenses().remove(exp);//TODO why load all expenses just to remove the link b/w an asset and expense
		assetService.save(exp.getAsset());
		repo.delete(exp);
	}
	
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<ChartData> getChartData(ExpenseSearchModel searchModel, DashboardConfig config) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<?> query = cb.createQuery();
		Root<Expense> exp = query.from(Expense.class);
		addFilterCriteria(exp, searchModel, cb, query);
		switch (searchModel.getChartType()) {
			case "getExpenseByYearMonth", "getExpenseByMonthDay" ->
					dateGrouping(cb, query, exp, config.getDateGroupingOracleFormat());
			case "getExpenseByCategories" -> {
				if (null == searchModel.getCatJoin()) {
					searchModel.setCatJoin(exp.join("category"));
				}
				Join<Expense, Category> catJoin = searchModel.getCatJoin();
				query.multiselect(catJoin.get("name"), cb.sum(exp.get(FIELD_AMOUNT)));
				query.groupBy(catJoin.get("name"));
			}
			case "getAssetUsage" -> {
				if (null == searchModel.getAssetJoin()) {
					searchModel.setAssetJoin(exp.join("asset"));
				}
				Join<Expense, Asset> assetJoin = searchModel.getAssetJoin();
				query.multiselect(assetJoin.get("name"), cb.sum(exp.get(FIELD_AMOUNT)));
				query.groupBy(assetJoin.get("name"));
			}
			default ->
					throw new FinanceManagerException("Invalid chart type: " + searchModel.getChartType() + " requested");
		}
		Query aquery = entityManager.createQuery(query);
		List<Object[]> result1 = aquery.getResultList();
		return result1.stream().map(item -> new ChartData((String) item[0], (BigDecimal) item[1], null))
				.toList();
	}
	
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public void addExpenseEntities(List<Expense> expenses) {
		repo.saveAll(expenses);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public SearchResponseModel<com.kb.fm.web.model.ExpenseModel> filterExpenses(SearchModel<ExpenseSearchModel> filterObj) {
		Pageable value1 = PaginationHelper.getPageable(filterObj.getOptions(), FIELD_TRANSACTION_DATE);
		Page<Expense> value = repo.findAll(ExpenseSearchSpecification.search(filterObj.getData()),
				value1);
		SearchResponseModel<com.kb.fm.web.model.ExpenseModel> returnValue = new SearchResponseModel<>();
		returnValue.setOverallCount(value.getTotalElements());
		returnValue.setData(value.getContent().stream().map(this::entityToModel).toList());
		return returnValue;
	}
		
	private <T> void dateGrouping(CriteriaBuilder cb, CriteriaQuery<T> query, Root<Expense> exp, String format) {
		Expression<String> dateSelection = cb.function("TO_CHAR", String.class, exp.get(FIELD_TRANSACTION_DATE), cb.literal(format));
		query.multiselect(dateSelection, cb.sum(exp.get(FIELD_AMOUNT )));
		query.groupBy(dateSelection);
		//TODO HIGH not a groupBy expression error
//		query.orderBy(cb.asc(dateSelection));
	}
	
	private void addFilterCriteria(Root<Expense> exp, ExpenseSearchModel searchModel, CriteriaBuilder cb, CriteriaQuery<?> query) {

		SpecificationHelperUtil.addCondition(query, addConditionForNumberRange(exp, cb, FIELD_AMOUNT,
				null != searchModel.getMinAmount() ? BigDecimal.valueOf(searchModel.getMinAmount()) : null,
				null != searchModel.getMaxAmount() ? BigDecimal.valueOf(searchModel.getMaxAmount()) : null));

		SpecificationHelperUtil.addCondition(query, addConditionsForDateRage(exp, cb, FIELD_TRANSACTION_DATE,
				searchModel.getFromDate(), searchModel.getToDate()));
		
		if (CollectionUtils.isNotEmpty(searchModel.getCategory())) {
			Join<Expense, Category> catJoin = exp.join("category");
			SpecificationHelperUtil.addCondition(query, catJoin.get("id").in(searchModel.getCategory()));
			searchModel.setCatJoin(catJoin);
		}
		
		if (CollectionUtils.isNotEmpty(searchModel.getAsset())) {
			Join<Expense, Asset> assetJoin = exp.join("asset");
			SpecificationHelperUtil.addCondition(query, assetJoin.get("id").in(searchModel.getAsset()));
			searchModel.setAssetJoin(assetJoin);
		}
	}

	private Expense modelToEntity(com.kb.fm.web.model.ExpenseModel model) {
		Expense dbEntity = null != model.getId() ? repo.getReferenceById(model.getId()) : new Expense();
		if (null != dbEntity.getAsset() && !model.getAsset().equals(dbEntity.getAsset().getId())) {
			dbEntity.getAsset().setUsage(dbEntity.getAsset().getUsage().subtract(dbEntity.getAmount()));
			dbEntity.getAsset().getExpenses().remove(dbEntity);
			assetService.save(dbEntity.getAsset());
		}
		dbEntity.setCategory(null != model.getCategory() ? catService.findCategory(model.getCategory()) : null);
		dbEntity.setSubCategory(
				null != model.getSubCategory() ? catService.findSubCategory(model.getSubCategory()) : null);
		dbEntity.setComment(model.getComment());
		dbEntity.setAmount(BigDecimal.valueOf(model.getAmount()));
		dbEntity.setTransactionDate(DateUtil.convert(model.getTransactionDate()));
		dbEntity.setTransactionDetail(model.getTransactionDetail());
		dbEntity.setFile(null != model.getImportFileId() ? fileRepo.getReferenceById(model.getImportFileId()) : null);
		Asset asset = assetService.getAsset(model.getAsset());
		asset.setUsage(null != asset.getUsage() ? asset.getUsage().add(dbEntity.getAmount()) : dbEntity.getAmount());
		asset.getExpenses().add(dbEntity);
		dbEntity.setAsset(asset);
		assetService.save(asset);
		return dbEntity;
	}

	private com.kb.fm.web.model.ExpenseModel entityToModel(Expense entity) {
		com.kb.fm.web.model.ExpenseModel model = new com.kb.fm.web.model.ExpenseModel();
		model.setId(entity.getId());
		model.setAsset(entity.getAsset().getId());
		model.setAmount(entity.getAmount().doubleValue());
		model.setCategory(null != entity.getCategory() ? entity.getCategory().getId() : null);
		model.setSubCategory(null != entity.getSubCategory() ? entity.getSubCategory().getId() : null);
		model.setTransactionDate(null != entity.getTransactionDate()
				? DateUtil.convertDateToDatePickerFormat(entity.getTransactionDate()) + ""
				: null);
		model.setComment(entity.getComment());
		model.setTransactionDetail(entity.getTransactionDetail());
		return model;
	}

}
