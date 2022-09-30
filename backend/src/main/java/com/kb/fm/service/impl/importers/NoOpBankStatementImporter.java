package com.kb.fm.service.impl.importers;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.web.model.ExpenseModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NoOpBankStatementImporter extends BaseBankStatementImporter {

    @Override
    List<ExpenseModel> importStatement(MultipartFile file) throws BankStatementImportException {
        throw new BankStatementImportException(file.getName(), "No importer registered to process the statements of this bank");
    }

    @Override
    public String getBankName() {
        return "UNKNOWN";
    }
}
