package com.ziqi.mlplatform.Repository;

import com.ziqi.mlplatform.Model.TrainData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TrainDataRepository extends JpaRepository<TrainData, Long> {
    Optional<TrainData> findByFileName(String fileName);

    Optional<TrainData> findByTemplateName(String templateName);

    Optional<TrainData> findByFilePath(String filePath);

    @Modifying
    @Transactional
    @Query(value = "UPDATE TrainData t SET t.fileName = null WHERE t.id = ?1")
    void deleteTrainDataById(Long id);
}
