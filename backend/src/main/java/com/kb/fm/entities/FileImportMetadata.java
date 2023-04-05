package com.kb.fm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@Table(name = "BANK_STATEMENT_FILE_IMPORTS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileImportMetadata {
    @Id
    @GeneratedValue(generator = "seq_file_imports_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_file_imports_id", sequenceName = "seq_file_imports_id", allocationSize = 1)
    private Long id;
    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;
    private String fileName;
    @CreatedDate
    @Column(name="import_time")
    private Date importTime;
    private Date verifiedTime;
    private String tempStorage;
    private String isDataImported;
    private String storageLocation;

}
