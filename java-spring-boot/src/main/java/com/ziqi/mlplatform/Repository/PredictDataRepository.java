package com.ziqi.mlplatform.Repository;

import com.ziqi.mlplatform.Model.PredictData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PredictDataRepository extends JpaRepository<PredictData, Long> {
    Optional<PredictData> findByFileName(String fileName);
}
