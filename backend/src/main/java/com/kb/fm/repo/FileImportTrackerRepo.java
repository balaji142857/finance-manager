package com.kb.fm.repo;

import com.kb.fm.entities.FileImportMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportTrackerRepo extends JpaRepository<FileImportMetadata, Long> {
}
