package com.kb.fm.web.model.imports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class BankMultipartFileWrapper {
    private final String bankName;
    private final MultipartFile file;
    private Long importId;
}
