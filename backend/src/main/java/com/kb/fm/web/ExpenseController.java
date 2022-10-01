package com.kb.fm.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletResponse;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kb.fm.exceptions.FinanceManagerException;
import com.kb.fm.service.ExpenseService;
import com.kb.fm.service.ImportService;
import com.kb.fm.util.DateUtil;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.ExpenseSearchModel;
import com.kb.fm.web.model.GenericResponse;
import com.kb.fm.web.model.SearchModel;
import com.kb.fm.web.model.SearchResponseModel;

@RestController
@RequestMapping("/expenses")
@Slf4j
public class ExpenseController {
	
	@Autowired
	private ExpenseService service;
	
	@Autowired
	private ImportService impService;

	@PostMapping
	public void addExpense(@RequestBody List<ExpenseModel> expenses) {
		if (CollectionUtils.isEmpty(expenses)) {
			return;
		}
		service.addExpenses(expenses);
	}
	
	@GetMapping
	public List<ExpenseModel> listExpenses() {
		return service.listExpenses();
	}
	
	@PostMapping("/delete/{id}")
	public void deleteExpense(@PathVariable("id") Long id) {
		service.deleteExpense(id);
	}
	
	@PostMapping("/import")
	public GenericResponse<List<ExpenseModel>> onboard(@RequestParam("files") MultipartFile[] uploadedFiles,
													   @RequestParam("bankNames") List<String> bankNames) throws BankStatementImportException {
		log.info("Bank statement import request received, bankNames: {}", bankNames);
		if (bankNames.size() != uploadedFiles.length) {
			throw new BankStatementImportException("unknown","Bank name not specified all the uploaded files");
		}
		List<BankMultipartFileWrapper> files = new ArrayList<>(bankNames.size());
		for (var i =0; i < bankNames.size(); i++) {
			files.add(new BankMultipartFileWrapper(bankNames.get(i), uploadedFiles[i]));
		}
		return impService.readBankStatements(files);
	}

	@PostMapping("/import/save")
	public GenericResponse<List<ExpenseModel>> importExpenses(@RequestBody List<ExpenseModel> expenseList) throws FinanceManagerException {
		log.info("received request {}", expenseList);
		service.addExpenses(expenseList);
		return null;
	}
	
	@PostMapping("/filter")
	public SearchResponseModel<ExpenseModel> filterExpense(@RequestBody SearchModel<ExpenseSearchModel> filterObj) {
		return service.filterExpenses(filterObj);
	}
	
	@PostMapping("/download")
	public void  downloadExpenses(HttpServletResponse response) throws IOException {
		try (Workbook wb = new XSSFWorkbook()){
			Sheet sheet = wb.createSheet("Expenses");
			List<com.kb.fm.entities.Expense> expenses = service.getAllExpenses();
			ListIterator<com.kb.fm.entities.Expense> iterator = expenses.listIterator();
			createHeadingRow(sheet);
			while(iterator.hasNext()) {
				com.kb.fm.entities.Expense exp = iterator.next();
				Row row = sheet.createRow(iterator.nextIndex());
				Cell cell = row.createCell(0);
				cell.setCellValue(exp.getAsset().getName());
				cell = row.createCell(1);
				cell.setCellValue(null != exp.getCategory() ? exp.getCategory().getName() :  "");
				cell = row.createCell(2);
				cell.setCellValue(null != exp.getSubCategory() ? exp.getSubCategory().getName() : "");
				cell = row.createCell(3);
				cell.setCellValue(null != exp.getAmount() ? exp.getAmount().doubleValue() : 0);
				cell = row.createCell(4);
				cell.setCellValue(DateUtil.convertDateToDatePickerFormat(exp.getTransactionDate()));
				cell = row.createCell(5);
				cell.setCellValue(exp.getTransactionDetail());
				cell = row.createCell(6);
				cell.setCellValue(exp.getComment());
			}
			wb.write(response.getOutputStream());
		}
	}

	private void createHeadingRow(Sheet sheet) {
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Asset");
		cell = row.createCell(1);
		cell.setCellValue("Category");
		cell = row.createCell(2);
		cell.setCellValue("SubCategory");
		cell = row.createCell(3);
		cell.setCellValue("Amount");
		cell = row.createCell(4);
		cell.setCellValue("date");
		cell = row.createCell(5);
		cell.setCellValue("transaction detail");
		cell = row.createCell(6);
		cell.setCellValue("comment");
	}
	
	

}
