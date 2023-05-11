package com.ziqi.mlplatform.Repository;

import com.ziqi.mlplatform.Model.TrainData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<TrainData, Long> {
    Optional<TrainData> findByFileName(String fileName);
}
