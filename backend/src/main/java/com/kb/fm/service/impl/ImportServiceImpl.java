package com.kb.fm.service.impl;

import static com.kb.fm.util.UpiUtil.getUpiIdFromTransaction;
import static com.kb.fm.util.DateUtil.convertToDate;

import java.math.BigDecimal;
import java.util.*;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.service.impl.importers.BankStatementImportHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kb.fm.entities.Asset;
import com.kb.fm.entities.CatSubCatIdModel;
import com.kb.fm.entities.Expense;
import com.kb.fm.service.AssetService;
import com.kb.fm.service.CategoryService;
import com.kb.fm.service.ExpenseService;
import com.kb.fm.service.ImportService;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.GenericResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {


	private final AssetService assetService;
	private final CategoryService catService;
	private final ExpenseService expService;
	private final BankStatementImportHelper importHelper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void configureApplication() { }


	@Override
	public GenericResponse<List<ExpenseModel>> loadExpensesFromFile(MultipartFile[] uploadedFiles) throws BankStatementImportException {
		//TODO BALAJI -- deduce the bank name from the uploaded file
		List<ExpenseModel> expenseList = new ArrayList<>();
		try {
			for(MultipartFile file: uploadedFiles) {
				expenseList.addAll(importHelper.importStatements(file));
			}
			return enrichExpenses(expenseList);
		} catch (BankStatementImportException e) {
			throw e;
		} catch(Exception e) {
			throw new BankStatementImportException("Error occurred while importing the bank statement. Please check the logs", e);
		}
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
			//TOOD:BALAJI -- why not save it in bulk towards the end.. would greatly minimize db calls
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

	//TODO:BALAJI nice starting point, but needs lots of improvement
	/**
	 * 1. loads everything from DB on every import call!!!!!
	 * 2. use streams	 
	 * 3. provide a feature for user preference.. lets say same UPI id has been stored with two categories... which one to choose ??
	 */	
	private GenericResponse<List<ExpenseModel>> enrichExpenses(List<ExpenseModel> expenses) {
		Map<String, CatSubCatIdModel> map = new HashMap<>();
		
		List<Expense> a = expService.getAllExpenses();
		for (Expense e: a) {
			//TODO:BALAJI - move UPI to constants -- also try to infer the type for other types like card swipe
			if(ObjectUtils.isEmpty(e.getTransactionDetail()) || !e.getTransactionDetail().startsWith("UPI")) {
				continue;
			}
			//TODO:BALAJI for heavens sake do not hardcode!!!!!
			String upiKey = getUpiIdFromTransaction("ICICI", e.getTransactionDetail());
			if (null != e.getCategory()) {
				Long id = e.getCategory().getId();
				map.compute(upiKey, (k, v) ->  null != v ? v.setCatId(id) : new CatSubCatIdModel(id, null));
			}
			if (null != e.getSubCategory()) {
				Long id = e.getSubCategory().getId();
				map.compute(upiKey, (k, v) ->  null != v ? v.setSubcatId(id) : new CatSubCatIdModel(null, id));				
			}
		}		
		int count = 0;
		for(ExpenseModel e: expenses) {
			if( null != e.getCategory() || null != e.getSubCategory()) {
				log.debug("No enriching category & subCategory provided by user for: {}", e);
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
		return new GenericResponse<>(expenses, getEnrichmentMessage(count, expenses.size()));
	}
	
	private String getEnrichmentMessage(int enrichCount, int overallCount) {
		//TODO:BALAJI move the messages to constants or atleast yaml
		String s = "Enriched %d records. Rest %d records have been flagged for review";
		return enrichCount != 0 ? String.format(s, enrichCount, overallCount - enrichCount) : "No enrichmetns done";
		
	}

}
