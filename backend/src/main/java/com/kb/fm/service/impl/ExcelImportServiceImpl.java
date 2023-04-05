package com.kb.fm.service.impl;

import com.kb.fm.entities.Asset;
import com.kb.fm.entities.CatSubCatIdModel;
import com.kb.fm.entities.Expense;
import com.kb.fm.service.*;
import com.kb.fm.service.impl.importers.BankStatementImportHelper;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.GenericResponse;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.kb.fm.util.Constants.BANK_ICICI;
import static com.kb.fm.util.Constants.UPI;
import static com.kb.fm.util.DateUtil.convertToDate;
import static com.kb.fm.util.UpiUtil.getUpiIdFromTransaction;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ImportService {

	private final ExpenseService expService;
	private final CategoryService catService;
	private final AssetService assetService;
	private final BankStatementImportHelper importHelper;
	private final FileImportTrackerService importTrackerService;

	@Value("${app.messages.enrichment}")
	private String enrichmentMessage;

	@Value("${app.messages.noEnrichment}")
	private String noEnrichmentMessage;

	@Override
	public GenericResponse<List<ExpenseModel>> readBankStatements(List<BankMultipartFileWrapper> files) {
		if (Objects.isNull(files)) {
			return new GenericResponse<>(Collections.emptyList(), "Nothing to import", List.of());
		}
		importTrackerService.trackImport(files);
		log.info("Added entry in the import tracking table");
		List<ExpenseModel> expenseList = new ArrayList<>();
		List<String> errorMessages = new ArrayList<>();
		for(BankMultipartFileWrapper wrapper: files) {
			try {
				var expenses = importHelper.importStatements(wrapper);
				log.info("Loaded {} rows from file: {}", CollectionUtils.size(expenses), wrapper.getFile().getOriginalFilename());
				expenseList.addAll(expenses);
			} catch(Exception e) {
				log.error("File {} import failed",wrapper.getFile().getOriginalFilename(),  e);
				errorMessages.add("Unable to import the bank statement from " + wrapper.getFile().getOriginalFilename());
			}
		}
		// if user imported expenses post uploading - then move file from temp to actual storage location
		// scheduled job to cleanup the temp location
		return enrichExpenses(expenseList, errorMessages);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void importExpenses(List<ExpenseModel> expenseList) {
		List<Expense> expenses = new ArrayList<>();
		if (CollectionUtils.isEmpty(expenseList)) {
			return;
		}		
		for (ExpenseModel exp : expenseList) {
			Asset asset = assetService.getAsset(exp.getAsset());
			BigDecimal amount = BigDecimal.valueOf(exp.getAmount());
			if (null == asset.getUsage()) {
				asset.setUsage(new BigDecimal(0L));
			}
			asset.setUsage(asset.getUsage().add(amount));
			assetService.save(asset);
			Expense e = new Expense();
			e.setAmount(amount);
			e.setAsset(asset);
			e.setCategory(catService.findCategory(exp.getCategory()));
			e.setSubCategory(catService.findSubCategory(exp.getSubCategory()));
			e.setTransactionDetail(exp.getTransactionDetail());
			e.setComment(exp.getComment());
			e.setTransactionDate(convertToDate(exp.getTransactionDate()));
			expenses.add(e);
		}
		expService.addExpenseEntities(expenses);
	}


	/**
	 * TODO
	 * 1. loads everything from DB on every import call!!!!!
	 * 2. provide a feature for user preference.. if same UPI id is referenced in two categories... how to choose
	 */	
	private GenericResponse<List<ExpenseModel>> enrichExpenses(List<ExpenseModel> expenses, List<String> errors) {
		Map<String, CatSubCatIdModel> map = getUpiIdToCategoryMappingFromPastExpenses();
		int count = 0;
		for(ExpenseModel e: expenses) {
			if( null != e.getCategory() || null != e.getSubCategory()) {
				log.debug("Not enriching as both category & subCategory are provided by user for: {}", e);
				continue;
			}
			CatSubCatIdModel ids = map.get(getUpiIdFromTransaction(e.getBankFormat(), e.getTransactionDetail()));
			if (null != ids) {
				e.setCategory(ids.getCatId());
				e.setSubCategory(ids.getSubcatId());
				count++;
			} else {
				e.setReviewRequired(true);
			}			
		}
		return new GenericResponse<>(expenses, getEnrichmentMessage(count, expenses.size()), errors);
	}

	//FIXME this is related to vendor and not category/subCategory
	private Map<String, CatSubCatIdModel> getUpiIdToCategoryMappingFromPastExpenses() {
		Map<String, CatSubCatIdModel> map = new HashMap<>();

		List<ExpenseModel> a = expService.listExpenses();
		for (ExpenseModel e: a) {
			// TODO try to infer the type for other types like card swipe
			if(!StringUtils.startsWith(e.getTransactionDetail(), UPI)) {
				continue;
			}
			// FIXME for heavens sake do not hardcode!!!!!
			log.warn("replace the hardcoded bank name to format: {}", e.getBankFormat());
			String upiKey = getUpiIdFromTransaction(BANK_ICICI, e.getTransactionDetail());
			if (null != e.getCategory()) {
				Long id = e.getCategory();
				map.compute(upiKey, (k, v) ->  null != v ? v.setCatId(id) : new CatSubCatIdModel(id, null));
			}
			if (null != e.getSubCategory()) {
				Long id = e.getSubCategory();
				map.compute(upiKey, (k, v) ->  null != v ? v.setSubcatId(id) : new CatSubCatIdModel(null, id));
			}
		}
		return map;
	}

	private String getEnrichmentMessage(int enrichCount, int overallCount) {
		return enrichCount != 0 ? String.format(enrichmentMessage, enrichCount, overallCount - enrichCount) : noEnrichmentMessage;
	}

}
