package com.ziqi.mlplatform.Repository;

import com.ziqi.mlplatform.Model.TrainData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainDataRepository extends JpaRepository<TrainData, Long> {
    Optional<TrainData> findByFileName(String fileName);

    Optional<TrainData> findByTemplateName(String templateName);

    Optional<TrainData> findByFilePath(String filePath);
}
