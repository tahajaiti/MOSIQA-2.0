package com.kyojin.mosiqa.repository;

import com.kyojin.mosiqa.entity.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, String> {
}
