package com.kb.fm.service;

import com.kb.fm.web.model.imports.BankMultipartFileWrapper;

import java.util.List;

public interface FileImportTrackerService {


    void trackImport(List<BankMultipartFileWrapper> files);
    void trackVerification(List<Long> fileIds);
}
