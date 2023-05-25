package com.ziqi.mlplatform.Service;

import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.dto.TrainDataResponse;

import java.util.List;
import java.util.Optional;

public interface ITrainDataService {
    TrainData createTrainData(TrainData uploadFile);

    void deleteTrainData(Long id);

    List<TrainDataResponse> getAllTrainData();

    TrainData updateFileName(Long id, String fileName);

    List<TrainDataResponse> getTrainDataById(Long id);

    TrainData getUploadFileByName(String fileName);

    Optional<TrainData> findByFileName(String fileName);

    TrainData updateTemplateName(Long id, String templateName);

    TrainData getTemplateByName(String templateName);
}
