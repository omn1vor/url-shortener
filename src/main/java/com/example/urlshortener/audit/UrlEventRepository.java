package com.example.urlshortener.audit;

import com.example.urlshortener.audit.event.UrlEventEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UrlEventRepository extends JpaRepository<UrlEventEntry, Long> {
    List<UrlEventEntry> findTop100ByUrl_CodeOrderByDateDesc(String code);

    List<UrlEventEntry> findAllByDateBetweenOrderByDate(LocalDateTime from, LocalDateTime to);

    List<UrlEventEntry> findTop100ByOrderByDateDesc();

    boolean existsByUrl_Code(String code);
}
