package com.ziqi.mlplatform.dto;

import com.ziqi.mlplatform.Model.TrainData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveModelRequest {
    private List<String> modelName;
    private List<String> modelClass;
    private List<String> modelDescription;
    private String existedTrainDataPath;
    private TrainData trainData;

}
