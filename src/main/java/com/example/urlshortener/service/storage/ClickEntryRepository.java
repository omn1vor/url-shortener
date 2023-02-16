package com.example.urlshortener.service.storage;

import com.example.urlshortener.model.ClickEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickEntryRepository extends JpaRepository<ClickEntry, Long> {
    List<ClickEntry> findTop100ByOrderByDateDesc();
}
