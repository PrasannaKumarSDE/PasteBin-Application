package com.example.pastebin.repository;

import com.example.pastebin.entity.Paste;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface PasteRepository extends JpaRepository<Paste, String> {

    // Increment view count safely
    @Modifying
    @Transactional
    @Query("""
        update Paste p
        set p.views = p.views + 1
        where p.id = :id
          and (p.maxViews is null or p.views < p.maxViews)
          and (p.expiresAt is null or p.expiresAt > :now)
    """)
    int incrementViewIfAllowed(
            @Param("id") String id,
            @Param("now") Instant now
    );

    Optional<Paste> findById(String id);
}
