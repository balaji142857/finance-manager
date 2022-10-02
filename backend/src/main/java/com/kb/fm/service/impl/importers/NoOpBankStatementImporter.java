package com.kb.fm.service.impl.importers;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoOpBankStatementImporter extends BaseBankStatementImporter {

    @Override
    List<ExpenseModel> importStatement(BankMultipartFileWrapper fileWrapper) throws BankStatementImportException {
        throw new BankStatementImportException(fileWrapper.getFile().getOriginalFilename(), "No importer registered to process the statements of this bank");
    }

    @Override
    public String getBankName() {
        return "UNKNOWN";
    }
}
