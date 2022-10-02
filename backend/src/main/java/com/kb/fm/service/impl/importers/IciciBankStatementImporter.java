package com.kb.fm.service.impl.importers;

import com.kb.fm.config.ImportConfig;
import com.kb.fm.entities.Asset;
import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.exceptions.DateFormatException;
import com.kb.fm.service.AssetService;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import com.kb.fm.web.model.imports.ColumnModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.kb.fm.util.DateUtil.convertDateToDatePickerFormat;
import static com.kb.fm.util.DateUtil.convertToDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class IciciBankStatementImporter extends BaseBankStatementImporter {

    private static final String BANK = "ICICI";
    private final AssetService assetService;
    private final ImportConfig importConfig;

    @Override
    public List<ExpenseModel> importStatement(BankMultipartFileWrapper fileWrapper) throws BankStatementImportException {
        MultipartFile file = fileWrapper.getFile();
        String fileName = file.getOriginalFilename();
        //TODO this should be from the DB instead
        Map<String, ColumnModel> importFormat = importConfig.getImportFormats().get(fileWrapper.getBankName());
        List<ExpenseModel> expenses = new ArrayList<>();
        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                log.info("staring to process row: {}", rowIndex+1);
                Row row = rowIterator.next();
                if (rowIndex < 14) {//TODO this should be from config
                    rowIndex++;
                    continue;
                }
                ExpenseModel e = new ExpenseModel();
                Asset asset = assetService.getAsset(fileWrapper.getBankName());
                e.setAsset(null != asset ? asset.getId() : null);
                e.setBankFormat(fileWrapper.getBankName());
                e.setImportFileId(fileWrapper.getImportId());
                Set<Map.Entry<String, ColumnModel>> keys = importFormat.entrySet();
                Set<String> ignore= Set.of("serial","transactionDetail");
                var iciciRowOverflowValidationColumns = keys.stream()
                        .filter(ent -> !ignore.contains(ent.getKey()))
                        .map(ent -> ent.getValue().getColumnIndex())
                        .collect(Collectors.toList());
                for(Map.Entry<String, ColumnModel> s : sort(importFormat.entrySet())) {
                    log.info("key: {}, colIndex: {}, val: {}", s.getKey(), s.getValue().getColumnIndex(), row.getCell(s.getValue().getColumnIndex()));
                    if("serial".equals(s.getKey())) {
                        Double sno = null;
                        try{
                            sno = getDoubleValue(row.getCell(s.getValue().getColumnIndex()));
                        } catch(NumberFormatException nfe) {
                            log.info("Invalid value present in serial number field. ");
                            log.warn("There is an issue with ICICI exports where transaction remarks overflows" +
                                    " to the next row and other cells are blank. Validating if that's the case here");
                            ColumnModel transDetailCol = importFormat.get("transactionDetail");
                            if (isAllBlank(row, iciciRowOverflowValidationColumns)
                                    && !isBlank(row, transDetailCol.getColumnIndex())) {
                                expenses.get(expenses.size() -1).setTransactionDetail(expenses.get(expenses.size() -1).getTransactionDetail() + getStringValue(row.getCell(transDetailCol.getColumnIndex())));
                                log.info("Indeed an issue with the bank statement. Appened the transaction detail with previous record");
                                rowIndex++;
                                break;
                            } else {
                                return expenses;
                            }
                        }
                        log.info("serial number is: {}", sno);
                    } else if("date".equals(s.getKey())) {
                        e.setTransactionDate(convertDateToDatePickerFormat(convertToDate(row.getCell(s.getValue().getColumnIndex()).getStringCellValue(), s.getValue().getColumnFormat())));
                    } else if("transactionDetail".equals(s.getKey())) {
                        e.setTransactionDetail(getStringValue(row.getCell(s.getValue().getColumnIndex())));
                    } else if("amount".equals(s.getKey())) {
                        BigDecimal amount = BigDecimal.valueOf(getDoubleValue(row.getCell(s.getValue().getColumnIndex())));
                        e.setAmount(amount.doubleValue());
                    } else {
                        //TODO
                        log.info("unknown column {}", s.getKey());
                    }
                }
                rowIndex++;
                log.info("processing completed for row {}", rowIndex);
                if (e.getAmount() == null || e.getAmount() == 0) {
                    log.warn("Ignoring row which is not an expenditure {}", e);
                    continue;
                }
                expenses.add(e);
            }
            return expenses;
        } catch (EncryptedDocumentException e) {
            throw new BankStatementImportException(fileName, "Please upload an unencrypted/non password protected file", e);
        }  catch( IOException e) {
            throw new BankStatementImportException(fileName, "Unable to read the bank statement", e);
        } catch (InvalidFormatException e) {
            throw new BankStatementImportException(fileName, "Ensure the file is indeed an excel file (not just extension)", e);
        } catch (DateFormatException e) {
            throw new BankStatementImportException(fileName, "Error occurred while reading date from the bank statement", e);
        }
    }

    @Override
    public String getBankName() {
        return BANK;
    }
}