package com.kb.fm.service.impl;

import static com.kb.fm.util.UpiUtil.getUpiIdFromTransaction;
import static com.kb.fm.util.DateUtil.convertDateToDatePickerFormat;
import static com.kb.fm.util.DateUtil.convertToDate;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kb.fm.config.ImportConfig;
import com.kb.fm.entities.Asset;
import com.kb.fm.entities.CatSubCatIdModel;
import com.kb.fm.entities.Category;
import com.kb.fm.entities.Expense;
import com.kb.fm.entities.SubCategory;
import com.kb.fm.exceptions.FinanceManagerException;
import com.kb.fm.service.AssetService;
import com.kb.fm.service.CategoryService;
import com.kb.fm.service.ExpenseService;
import com.kb.fm.service.ImportService;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.GenericResponse;
import com.kb.fm.web.model.imports.ColumnModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportServivceImpl implements ImportService {

	
	private final AssetService assetService;
	private final CategoryService catService;
	private final ExpenseService expService;
	private final ImportConfig importConfig;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void configureApplication() { }
	
	@Override
	public GenericResponse<List<ExpenseModel>> loadExpensesFromFile(MultipartFile[] uploadedFiles) throws FinanceManagerException {
		List<ExpenseModel> expenseList;
		try {
			expenseList = loadExpenses(uploadedFiles);
			return enrichExpenses(expenseList);
		} catch (FinanceManagerException | ParseException e) {
			throw new FinanceManagerException(e);
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
	
	private List<ExpenseModel> loadExpenses(MultipartFile[] uploadedFiles) throws FinanceManagerException, ParseException {
		//TODO:BALAJI -- derive from request
		String bankFormat = "ICICI";
		if (uploadedFiles == null || uploadedFiles.length == 0) {
			return Collections.emptyList();
		}
		Map<String, ColumnModel> importFormat = importConfig.getImportFormats().get(bankFormat);		
		List<ExpenseModel> expenses = new ArrayList<>();
		try (Workbook wb = WorkbookFactory.create(uploadedFiles[0].getInputStream())) {
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.rowIterator();
			int rowIndex = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (rowIndex == 0) {
					rowIndex++;
					continue;
				}
				ExpenseModel e = new ExpenseModel();
				Asset asset = assetService.getAsset(bankFormat);
				e.setAsset(null != asset ? asset.getId() : null);
				e.setBankFormat(bankFormat);
				for(Entry<String, ColumnModel> s : importFormat.entrySet()) {
					switch (s.getKey()) {
						case "date":
							e.setTransactionDate(convertDateToDatePickerFormat(convertToDate(row.getCell(s.getValue().getColumnIndex()).getStringCellValue(), s.getValue().getColumnFormat())));
							break;
						case "transactionDetail":
							e.setTransactionDetail(getStringValue(row.getCell(s.getValue().getColumnIndex())));
							break;
						case "amount":
							BigDecimal amount = BigDecimal.valueOf(getDoubleValue(row.getCell(s.getValue().getColumnIndex())));											
							e.setAmount(null != amount ? amount.doubleValue() : 0);
							break;
						case "category":
							String cat = getStringValue(row.getCell(s.getValue().getColumnIndex()));
							Category category = StringUtils.hasText(cat) ? null : catService.findCategory(cat);
							e.setCategory(null != category ? category.getId() : null);
							break;
						case "subCategory":
							String subCat = getStringValue(row.getCell(s.getValue().getColumnIndex()));
							SubCategory subCategory = null != subCat ? catService.findSubCategory(subCat) : null;
							e.setSubCategory(null != subCategory ? subCategory.getId() : null);
							break;
						case "comment":
							e.setComment(getStringValue(row.getCell(s.getValue().getColumnIndex())));
							break;
					}
				}							
				rowIndex++;
				log.warn("processing completed for row {}", rowIndex);
				expenses.add(e);
			}
			return expenses;
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			log.error("Error occured while parsing the expenses file", e);
			// TODO:BALAJI pass some useful info to client as what caused the failure
			throw new FinanceManagerException("File import failed");
		}
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

	private static String getStringValue(Cell cell) {
		return null != cell ? cell.getStringCellValue() : null;
	}

	private static double getDoubleValue(Cell cell) {
		return null != cell ? cell.getNumericCellValue() : null;
	}

}
