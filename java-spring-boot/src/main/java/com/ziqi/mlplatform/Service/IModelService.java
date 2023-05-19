package com.ziqi.mlplatform.Service;

import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.dto.ModelResponse;
import com.ziqi.mlplatform.dto.SaveModelRequest;

import java.util.List;

public interface IModelService {
    Model createModelWithFile(Model model);

    Model createModel(SaveModelRequest model);

    void deleteModel(Long id);

    List<ModelResponse> getAllData();

    Model updateModelName(Long id, String modelName);

    Model getModelById(Long id);

    Model getModelByName(String modelName);

}
