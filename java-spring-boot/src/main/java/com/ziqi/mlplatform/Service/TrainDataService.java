package com.ziqi.mlplatform.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziqi.mlplatform.Model.PredictData;
import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Repository.TrainDataRepository;
import com.ziqi.mlplatform.dto.TrainDataResponse;
import com.ziqi.mlplatform.exception.OperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainDataService implements ITrainDataService {
    private final TrainDataRepository trainDataRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public TrainData createTrainData(TrainData uploadFile) {
        try {
            return trainDataRepository.save(uploadFile);
        } catch (Exception e){
            throw new OperationException(e.getMessage());
        }
    }

    @Override
    public List<TrainDataResponse> getAllTrainData(){
        try {
            List<TrainData> uploadFile = trainDataRepository.findAll();
            return uploadFile.stream().map(this::mapToUploadFileResponse).toList();
        } catch (Exception e) {
            throw new OperationException("No upload file found");
        }

    }

    private TrainDataResponse mapToUploadFileResponse(TrainData uploadFile) {
        return TrainDataResponse.builder()
                .id(uploadFile.getId())
                .fileName(uploadFile.getFileName())
                .filePath(uploadFile.getFilePath())
                .templateName(uploadFile.getTemplateName())
                .templatePath(uploadFile.getTemplatePath())
                .models(uploadFile.getModels())
                .build();
    }

    @Override
    public void deleteTrainData(Long id) {
        try {
            Optional<TrainData> trainData = trainDataRepository.findById(id);
            if (trainData.isEmpty()) {
                throw new OperationException("Predict data not found with id: " + id);
            }
            String fileName = trainData.get().getFileName();
            Map<String, String> body = new HashMap<>();
            body.put("fileName", fileName);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/delete-train-data",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                if (trainData.map(TrainData::getModels).isPresent()) {
                    if (!trainData.get().getTemplateName().isEmpty()) {
                        trainData.map(TrainData::getModels).get().forEach(model -> {
                            model.setTrainData(null);
                        });
                        trainData.map(trainData1 -> {
                            trainData1.setFilePath(null);
                            trainData1.setFilePath(null);
                            return null;
                        });
                    } else {
                        trainData.map(TrainData::getModels).get().forEach(model -> {
                            model.setTrainData(null);
                        });
                        trainDataRepository.deleteById(id);
                    }
                } else {
                    if (!trainData.get().getTemplateName().isEmpty()) {
                        trainData.map(trainData1 -> {
                            trainData1.setFilePath(null);
                            trainData1.setFilePath(null);
                            return null;
                        });
                    } else {
                        trainDataRepository.deleteById(id);
                    }
                }
            } else {
                throw new OperationException("Error deleting train data with id: " + id);
            }
        } catch (Exception e) {
            throw new OperationException(e.getMessage() + "Error deleting train data with id: " + id);
        }
    }

    @Override
    public TrainData updateFileName(Long id, String newName) {
        try {
            Optional<TrainData> trainData = trainDataRepository.findById(id);
            if (trainData.isEmpty()) {
                throw new OperationException("Predict data not found with id: " + id);
            }
            String fileName = trainData.get().getFileName();
            Map<String, String> body = new HashMap<>();
            body.put("fileName", fileName);
            body.put("newName", newName);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/rename-train-data",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String newPath = jsonNode.get("newPath").asText();
                return trainDataRepository.findById(id)
                        .map(model1 -> {
                            model1.setFileName(newName);
                            model1.setFilePath(newPath);
                            return trainDataRepository.save(model1);
                        })
                        .orElseThrow(() -> new OperationException("Predict data not found with id: " + id));
            }
        } catch (Exception e) {
            throw new OperationException("Predict data not found with id: " + id);
        }
        return null;
    }

    @Override
    public List<TrainDataResponse> getTrainDataById(Long id) {
        try {
            Optional<TrainData> uploadFile = trainDataRepository.findById(id);
            return uploadFile.stream().map(this::mapToUploadFileResponse).toList();
        } catch (Exception e) {
            throw new OperationException("No upload file found");
        }
    }

    @Override
    public TrainData getUploadFileByName(String modelName) {
        return trainDataRepository.findByFileName(modelName)
                .orElseThrow(() -> new OperationException("Model not found with model name: " + modelName));
    }

    @Override
    public Optional<TrainData> findByFileName(String fileName) {
        return trainDataRepository.findByFileName(fileName);
    }

    @Override
    public TrainData updateTemplateName(Long id, String newName) {
        try {
            Optional<TrainData> trainData = trainDataRepository.findById(id);
            if (trainData.isEmpty()) {
                throw new OperationException("Predict data not found with id: " + id);
            }
            String fileName = trainData.get().getFileName();
            Map<String, String> body = new HashMap<>();
            body.put("fileName", fileName);
            body.put("newName", newName);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/rename-template",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String newPath = jsonNode.get("newPath").asText();
                return trainDataRepository.findById(id)
                        .map(model1 -> {
                            model1.setTemplateName(newName);
                            model1.setTemplatePath(newPath);
                            return trainDataRepository.save(model1);
                        })
                        .orElseThrow(() -> new OperationException("Predict data not found with id: " + id));
            }
        } catch (Exception e) {
            throw new OperationException("Predict data not found with id: " + id);
        }
        return null;
    }

    @Override
    public TrainData getTemplateByName(String templateName) {
        return trainDataRepository.findByTemplateName(templateName)
                .orElseThrow(() -> new OperationException("Model not found with model name: " + templateName));
    }
}
