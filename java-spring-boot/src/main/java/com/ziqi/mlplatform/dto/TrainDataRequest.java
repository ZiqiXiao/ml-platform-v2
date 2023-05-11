package com.ziqi.mlplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainDataRequest {
    private String fileName;
    private String filePath;
    private String templateName;
    private String templatePath;
    private ModelRequest models;
}
