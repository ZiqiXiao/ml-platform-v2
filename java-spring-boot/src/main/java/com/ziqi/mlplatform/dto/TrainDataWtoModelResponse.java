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
public class TrainDataWtoModelResponse {
    private Long id;
    private String fileName;
    private String filePath;
    private String templateName;
    private String templatePath;

    public TrainDataWtoModelResponse(TrainData trainData) {
        this.id = trainData.getId();
        this.fileName = trainData.getFileName();
        this.filePath = trainData.getFilePath();
        this.templateName = trainData.getTemplateName();
        this.templatePath = trainData.getTemplatePath();
    }
}
