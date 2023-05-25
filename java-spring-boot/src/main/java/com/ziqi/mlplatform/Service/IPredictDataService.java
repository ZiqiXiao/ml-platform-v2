package com.ziqi.mlplatform.Service;

import com.ziqi.mlplatform.Model.PredictData;
import com.ziqi.mlplatform.dto.PredictDataRequest;
import com.ziqi.mlplatform.dto.PredictDataResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPredictDataService {
    PredictData createPredictData(PredictDataRequest predictDataRequest);

    void deletePredictData(Long id);

    PredictData getPredictDataById(Long id);

    ResponseEntity<PredictData> checkPredictDataExist(String fileName);

    PredictData getPredictDataByName(String fileName);

    PredictData updateFileName(Long id, String fileName);

    PredictData updateFilePath(Long id, String filePath);

    List<PredictDataResponse> getAllPredictData();

}
