package com.kb.fm.web.model.imports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@Setter
public class BankMultipartFileWrapper {
    private String bankName;
    private MultipartFile file;
}
