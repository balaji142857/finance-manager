package com.kb.fm.service.impl.importers;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.service.BankStatementImporter;
import com.kb.fm.web.model.ExpenseModel;
import com.kb.fm.web.model.imports.BankMultipartFileWrapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankStatementImportHelper {

    private final List<BankStatementImporter> bankStatementImporters;
    private final NoOpBankStatementImporter defaultImporter;
    private final ConcurrentMap<String, BankStatementImporter> registry = new ConcurrentHashMap<>();


    public Collection<ExpenseModel> importStatements(BankMultipartFileWrapper file) throws BankStatementImportException {
        String bankName = file.getBankName();
        BankStatementImporter importer = registry.getOrDefault(bankName, defaultImporter);
        return importer.importBankStatement(file);
    }

    @PostConstruct
    public void initialize() {
        if (CollectionUtils.isEmpty(bankStatementImporters)) {
            log.warn("No Bank statement importers are registered. Bank statement import functionality will not work");
            return;
        }
        registerImporters(bankStatementImporters);
    }

    private void registerImporters(List<BankStatementImporter> importersList) {
        importersList.forEach(importer -> registry.put(importer.getBankName(), importer));
    }

}
