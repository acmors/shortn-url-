package dev.shortn.repository;

import dev.shortn.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    @Modifying
    @Query("UPDATE Url u SET u.clicks = u.clicks + :clicks WHERE u.shortCode = :shortCode")
    void incrementClicks(@Param("shortCode") String shortCode, @Param("clicks") Long clicks);
}
