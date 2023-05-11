package com.ziqi.mlplatform.dto;

import com.ziqi.mlplatform.Model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainDataResponse {
    private Long id;
    private String fileName;
    private String filePath;
    private String templateName;
    private String templatePath;
    // private List<Model> models;
}
