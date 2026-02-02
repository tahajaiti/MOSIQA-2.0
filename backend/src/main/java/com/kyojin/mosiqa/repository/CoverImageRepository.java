package com.kyojin.mosiqa.repository;

import com.kyojin.mosiqa.entity.CoverImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverImageRepository extends JpaRepository<CoverImage, String> {
}
