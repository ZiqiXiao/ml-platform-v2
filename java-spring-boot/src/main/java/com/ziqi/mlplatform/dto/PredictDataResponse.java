package com.ziqi.mlplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PredictDataResponse {
    private Long id;
    private String fileName;
    private String filePath;
}
