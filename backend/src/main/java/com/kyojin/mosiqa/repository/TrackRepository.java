package com.kyojin.mosiqa.repository;

import com.kyojin.mosiqa.entity.MusicCategory;
import com.kyojin.mosiqa.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {

    List<Track> findByCategory(MusicCategory category);

    List<Track> findByArtistContainingIgnoreCase(String artist);

    List<Track> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT t FROM Track t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.artist) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Track> searchByTitleOrArtist(@Param("query") String query);

    List<Track> findAllByOrderByCreatedAtDesc();
}
