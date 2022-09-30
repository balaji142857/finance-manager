package com.kb.fm.service;

import com.kb.fm.exceptions.BankStatementImportException;
import com.kb.fm.web.model.ExpenseModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BankStatementImporter {

    List<ExpenseModel> importBankStatement(MultipartFile file) throws BankStatementImportException;

    String getBankName();
}
