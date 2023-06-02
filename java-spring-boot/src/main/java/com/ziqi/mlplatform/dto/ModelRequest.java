package com.ziqi.mlplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelRequest {
    private String modelName;
    private String modelClass;
    private String modelPath;
    private String modelDescription;
}
