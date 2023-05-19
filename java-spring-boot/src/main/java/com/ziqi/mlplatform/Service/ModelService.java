package com.ziqi.mlplatform.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Repository.ModelRepository;
import com.ziqi.mlplatform.dto.ModelResponse;
import com.ziqi.mlplatform.dto.SaveModelRequest;
import com.ziqi.mlplatform.exception.OperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class ModelService implements IModelService {
    private final ModelRepository repository;
    private final ITrainDataService uploadFileService;
    @Autowired
    private RestTemplate restTemplate;

    public Model createModelWithFile(Model model) {
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

    public Model createModel(SaveModelRequest saveModelRequest) {
        try {
            String flaskUrlSaveModel = "http://flask:5001/save-model";
            System.out.println(saveModelRequest);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    flaskUrlSaveModel,
                    saveModelRequest,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Flask API调用成功，进行其他操作
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                String filePath = jsonNode.get("filePath").asText();
                String templatePath = jsonNode.get("templatePath").asText();
                System.out.println(filePath.length());
                if (saveModelRequest.getUploadFile().getFileName().length() == 0) {
                    saveModelRequest.getUploadFile().setFileName(null);
                    saveModelRequest.getUploadFile().setFilePath(null);
                } else {
                    saveModelRequest.getUploadFile().setFilePath(filePath);
                }

                if (saveModelRequest.getUploadFile().getTemplateName().length() == 0) {
                    saveModelRequest.getUploadFile().setTemplatePath(null);
                    saveModelRequest.getUploadFile().setTemplateName(null);
                } else {
                    saveModelRequest.getUploadFile().setTemplatePath(templatePath);
                }

                JsonNode modelPathsNode = jsonNode.get("modelPaths");
                List<String> modelPaths = new ArrayList<>();
                for (JsonNode pathNode : modelPathsNode) {
                    modelPaths.add(pathNode.asText());
                }

                if (saveModelRequest.getUploadFile().getFileName() != null | saveModelRequest.getUploadFile().getTemplateName() != null) {
                    TrainData uploadFile = uploadFileService.createTrainData(saveModelRequest.getUploadFile());
                    System.out.println(uploadFile);

                    for (int i=0; i<saveModelRequest.getModelName().size(); i++) {

                        Model model = Model.builder()
                                .modelName(saveModelRequest.getModelName().get(i))
                                .modelClass(saveModelRequest.getModelClass().get(i))
                                .modelPath(modelPaths.get(i))
                                .uploadFile(uploadFile)
                                .build();
                        System.out.println(model);
                        repository.save(model);
                    }
                } else {
                    for (int i=0; i<saveModelRequest.getModelName().size(); i++) {

                        Model model = Model.builder()
                                .modelName(saveModelRequest.getModelName().get(i))
                                .modelClass(saveModelRequest.getModelClass().get(i))
                                .modelPath(modelPaths.get(i))
                                .build();
                        System.out.println(model);
                        repository.save(model);
                    }
                }
                return null;
            } else {
                // Flask API调用失败，处理错误
                throw new OperationException("Flask API call failed with status code: " + response.getStatusCode());
            }
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
                .modelPath(model.getModelPath())
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

