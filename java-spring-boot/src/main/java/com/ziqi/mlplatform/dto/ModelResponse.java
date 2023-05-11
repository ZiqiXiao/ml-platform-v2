package com.ziqi.mlplatform.dto;

import com.ziqi.mlplatform.Model.TrainData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelResponse {
    private Long id;
    private String modelName;
    private String modelClass;
    private TrainData uploadFile;
}
