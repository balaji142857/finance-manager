package com.kb.fm.service.impl.importers;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.service.BankStatementImporter;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.imports.ColumnModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public abstract class BaseBankStatementImporter implements BankStatementImporter {

    @Override
    public List<ExpenseModel> importBankStatement(MultipartFile file) throws BankStatementImportException {
        try {
            return importStatement(file);
        } catch(BankStatementImportException e) {
            throw e;
        } catch(Exception e) {
            throw new BankStatementImportException(file.getName(), "Error occurred while importing bank statement", e);
        }
    }

    abstract List<ExpenseModel> importStatement(MultipartFile files) throws BankStatementImportException;

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
