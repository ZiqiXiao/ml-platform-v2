package com.ziqi.mlplatform.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Repository.ModelRepository;
import com.ziqi.mlplatform.dto.ModelResponse;
import com.ziqi.mlplatform.dto.SaveModelRequest;
import com.ziqi.mlplatform.exception.OperationException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ModelService implements IModelService {
    public static final Logger log = LoggerFactory.getLogger(ModelService.class);
    private final ModelRepository modelRepository;
    private final ITrainDataService trainDataService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EntityManager entityManager;

    public Model createModelWithFile(Model model) {
        try {
            Optional<TrainData> existingUploadFile = trainDataService.findByFileName(model.getTrainData().getFileName());
            if (existingUploadFile.isEmpty()) {
                TrainData uploadFile = trainDataService.createTrainData(model.getTrainData());
                model.setTrainData(uploadFile);
            }

            existingUploadFile.ifPresent(model::setTrainData);
            return modelRepository.save(model);
        } catch (Exception e) {
            throw new OperationException(e.getMessage());
        }

    }

    public Model createModel(SaveModelRequest saveModelRequest) {
        try {
            String flaskUrlSaveModel = "http://flask:5001/save-model";
            log.info("Create model with SaveModelRequest : {}", saveModelRequest);
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

                JsonNode modelPathsNode = jsonNode.get("modelPaths");
                List<String> modelPaths = new ArrayList<>();
                for (JsonNode pathNode : modelPathsNode) {
                    modelPaths.add(pathNode.asText());
                }

                if (saveModelRequest.getExistedTrainDataPath().length() != 0) {
                    log.info(saveModelRequest.getExistedTrainDataPath());
                    TrainData trainData = trainDataService.findByFilePath(saveModelRequest.getExistedTrainDataPath());
                    log.info("TrainData : {}", trainData);
                    for (int i=0; i<saveModelRequest.getModelName().size(); i++) {

                        Model model = Model.builder()
                                .modelName(saveModelRequest.getModelName().get(i))
                                .modelClass(saveModelRequest.getModelClass().get(i))
                                .modelPath(modelPaths.get(i))
                                .modelDescription(saveModelRequest.getModelDescription().get(i))
                                .trainData(trainData)
                                .build();
                        System.out.println(model);
                        modelRepository.save(model);
                    }}
                else {
                    if (saveModelRequest.getTrainData().getFileName().length() == 0) {
                        saveModelRequest.getTrainData().setFileName(null);
                        saveModelRequest.getTrainData().setFilePath(null);
                    } else {
                        saveModelRequest.getTrainData().setFilePath(filePath);
                    }

                    if (saveModelRequest.getTrainData().getTemplateName().length() == 0) {
                        saveModelRequest.getTrainData().setTemplatePath(null);
                        saveModelRequest.getTrainData().setTemplateName(null);
                    } else {
                        saveModelRequest.getTrainData().setTemplatePath(templatePath);
                    }

                    if (saveModelRequest.getTrainData().getFileName() != null | saveModelRequest.getTrainData().getTemplateName() != null) {
                        TrainData trainData = trainDataService.createTrainData(saveModelRequest.getTrainData());

                        for (int i=0; i<saveModelRequest.getModelName().size(); i++) {

                            Model model = Model.builder()
                                    .modelName(saveModelRequest.getModelName().get(i))
                                    .modelClass(saveModelRequest.getModelClass().get(i))
                                    .modelPath(modelPaths.get(i))
                                    .modelDescription(saveModelRequest.getModelDescription().get(i))
                                    .trainData(trainData)
                                    .build();
                            System.out.println(model);
                            modelRepository.save(model);
                        }
                    }
                    else {
                        for (int i=0; i<saveModelRequest.getModelName().size(); i++) {

                            Model model = Model.builder()
                                    .modelName(saveModelRequest.getModelName().get(i))
                                    .modelClass(saveModelRequest.getModelClass().get(i))
                                    .modelPath(modelPaths.get(i))
                                    .modelDescription(saveModelRequest.getModelDescription().get(i))
                                    .build();
                            System.out.println(model);
                            modelRepository.save(model);
                        }
                    }
                    return null;
                }
            } else {
                // Flask API调用失败，处理错误
                throw new OperationException("Flask API call failed with status code: " + response.getStatusCode());
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new OperationException(e.getMessage());
        }
        return null;
    }

    public List<ModelResponse> getAllData(){
        try {
            List<Model> models = modelRepository.findAll();
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
                .modelDescription(model.getModelDescription())
                .uploadFile(model.getTrainData())
                .build();
    }

    @Override
    @Transactional
    public void deleteModel(Long id) {
        try {
            Optional<Model> model = modelRepository.findById(id);
            if (model.isEmpty()) {
                throw new OperationException("Predict data not found with id: " + id);
            }
            String modelName = model.get().getModelName();
            String modelClass = model.get().getModelClass();
            String modelPath = model.get().getModelPath();
            Map<String, String> body = new HashMap<>();
            body.put("modelClass", modelClass);
            body.put("modelName", modelName);
            body.put("modelPath", modelPath);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/delete-model",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                modelRepository.deleteModelById(id);
            } else {
                throw new OperationException("Error deleting model with id: " + id);
            }
        } catch (Exception e) {
            throw new OperationException("Error deleting model with id: " + id + e);
        }
    }

    @Override
    public Model updateModelName(Long id, String newName) {
        try {
            Optional<Model> model = modelRepository.findById(id);
            if (model.isEmpty()) {
                throw new OperationException("Model not found with id: " + id);
            }
            String modelName = model.get().getModelName();
            String modelClass = model.get().getModelClass();
            String modelPath = model.get().getModelPath();
            Map<String, String> body = new HashMap<>();
            body.put("modelName", modelName);
            body.put("newName", newName);
            body.put("modelClass", modelClass);
            body.put("modelPath", modelPath);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/rename-model",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String newPath = jsonNode.get("newPath").asText();
                return modelRepository.findById(id)
                        .map(model1 -> {
                            model1.setModelName(newName);
                            model1.setModelPath(newPath);
                            return modelRepository.save(model1);
                        })
                        .orElseThrow(() -> new OperationException("Model not found with id: " + id));
            }
        } catch (Exception e) {
            throw new OperationException("Model not found with id: " + id + e);
        }
        return null;
    }

    public Model getModelById(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new OperationException("Model not found with id: " + id));
    }

    public Model getModelByName(String modelName) {
        return modelRepository.findByModelName(modelName)
                .orElseThrow(() -> new OperationException("Model not found with model name: " + modelName));
    }

}

