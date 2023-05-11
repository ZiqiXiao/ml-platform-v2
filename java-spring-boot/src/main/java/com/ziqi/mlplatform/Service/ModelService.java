package com.ziqi.mlplatform.Service;

import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Repository.ModelRepository;
import com.ziqi.mlplatform.dto.ModelResponse;
import com.ziqi.mlplatform.exception.OperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModelService implements IModelService {
    private final ModelRepository repository;
    private final ITrainDataService uploadFileService;

    public Model createModel(Model model) {
        try {
            Optional<TrainData> existingUploadFile = uploadFileService.findByFileName(model.getUploadFile().getFileName());
            if (existingUploadFile.isEmpty()) {
                TrainData uploadFile = uploadFileService.createTrainData(model.getUploadFile());
                model.setUploadFile(uploadFile);
            }

            existingUploadFile.ifPresent(model::setUploadFile);
            return repository.save(model);
        } catch (Exception e) {
            throw new OperationException(e.getMessage());
        }

    }

    public List<ModelResponse> getAllData(){
        try {
            List<Model> models = repository.findAll();
            return models.stream().map(this::mapToModelResponse).toList();
        } catch (Exception e) {
            throw new OperationException("No model found");
        }

    }

    private ModelResponse mapToModelResponse(Model model) {
        return ModelResponse.builder()
                .id(model.getId())
                .modelName(model.getModelName())
                .modelClass(model.getModelClass())
                .uploadFile(model.getUploadFile())
                .build();
    }

    public void deleteModel(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new OperationException("Model not found with id: " + id);
        }
    }

    public Model updateModelName(Long id, String modelName) {
        return repository.findById(id)
                .map(model1 -> {
                    model1.setModelName(modelName);
                    return repository.save(model1);
                })
                .orElseThrow(() -> new OperationException("Model not found with id: " + id));
    }

    public Model getModelById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OperationException("Model not found with id: " + id));
    }

    public Model getModelByName(String modelName) {
        return repository.findByModelName(modelName)
                .orElseThrow(() -> new OperationException("Model not found with model name: " + modelName));
    }

}

