package com.kb.fm.service.impl.importers;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.service.BankStatementImporter;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import com.kb.fm.web.model.imports.ColumnModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
public abstract class BaseBankStatementImporter implements BankStatementImporter {

    @Override
    public List<ExpenseModel> importBankStatement(BankMultipartFileWrapper fileWrapper) throws BankStatementImportException {
        try {
            if (null == fileWrapper.getFile()) {
                log.warn("Input file {} is null, returning empty response", fileWrapper.getBankName());
                return Collections.emptyList();
            }
            return importStatement(fileWrapper);
        } catch(BankStatementImportException e) {
            throw e;
        } catch(Exception e) {
            throw new BankStatementImportException(fileWrapper.getFile().getName(), "Error occurred while importing bank statement", e);
        }
    }

    abstract List<ExpenseModel> importStatement(BankMultipartFileWrapper files) throws BankStatementImportException;

    protected boolean isAllBlank(Row row, List<Integer> columnIndexes) {
        if(CollectionUtils.isEmpty(columnIndexes)) {
            return true;
        }
        return columnIndexes
                .stream()
                .allMatch(item -> isBlank(row,item));
    }

    protected boolean isBlank(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return Objects.isNull(cell) || !StringUtils.hasText(row.getCell(colIndex).getStringCellValue());
    }

    protected static String getStringValue(Cell cell) {
        return null != cell ? cell.getStringCellValue() : null;
    }

    //TODO BAKRISHN the whole point of using a big decimal is lost if we first try to get this a double from string value
    protected static Double getDoubleValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return null;
        }
        CellType type = cell.getCellTypeEnum();
        if (type.equals(CellType.BLANK)) {
            throw new NumberFormatException("Empty content, cannot convert to a double value");
        }
        if (type.equals(CellType.STRING)) {
            return Double.parseDouble(cell.getStringCellValue());
        }
        return cell.getNumericCellValue();
    }

    protected Iterable<? extends Map.Entry<String, ColumnModel>> sort(Set<Map.Entry<String, ColumnModel>> entrySet) {
        var list = new ArrayList<>(entrySet);
        list.sort(Map.Entry.comparingByValue(Comparator.comparing(ColumnModel::getReadOrder, Comparator.nullsLast(Comparator.naturalOrder()))));
        return list;
    }
}
