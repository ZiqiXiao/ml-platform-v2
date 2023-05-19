package com.ziqi.mlplatform.Service;

import com.ziqi.mlplatform.Model.PredictData;
import com.ziqi.mlplatform.dto.PredictDataRequest;
import com.ziqi.mlplatform.dto.PredictDataResponse;

import java.util.List;

public interface IPredictDataService {
    PredictData createPredictData(PredictDataRequest predictDataRequest);

    void deletePredictData(Long id);

    PredictData getPredictDataById(Long id);

    PredictData getPredictDataByName(String fileName);

    PredictData updateFileName(Long id, String fileName);

    PredictData updateFilePath(Long id, String filePath);

    List<PredictDataResponse> getAllPredictData();
}
