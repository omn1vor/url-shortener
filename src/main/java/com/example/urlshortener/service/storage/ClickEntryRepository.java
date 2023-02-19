package com.example.urlshortener.service.storage;

import com.example.urlshortener.model.ClickEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClickEntryRepository extends JpaRepository<ClickEntry, Long> {
    Page<ClickEntry> findAllByUrl_Code(String code, Pageable pageable);

    boolean existsByUrl_Code(String code);
}
