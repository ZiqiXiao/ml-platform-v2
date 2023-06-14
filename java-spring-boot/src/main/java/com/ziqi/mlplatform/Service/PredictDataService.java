package com.ziqi.mlplatform.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.Model.PredictData;
import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Repository.PredictDataRepository;
import com.ziqi.mlplatform.dto.PredictDataRequest;
import com.ziqi.mlplatform.dto.PredictDataResponse;
import com.ziqi.mlplatform.dto.TrainDataResponse;
import com.ziqi.mlplatform.exception.OperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PredictDataService implements IPredictDataService {
    private final PredictDataRepository predictDataRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public PredictData createPredictData(PredictDataRequest predictDataRequest) {
        try {
            String flaskUrlSaveModel = "http://flask:5001/save-predict-data";
            ResponseEntity<String> response = restTemplate.postForEntity(
                    flaskUrlSaveModel,
                    predictDataRequest,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Flask API调用成功，进行其他操作
                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String filePath = jsonNode.get("filePath").asText();

                PredictData predictData = PredictData.builder()
                        .fileName(predictDataRequest.getFileName())
                        .filePath(filePath)
                        .build();
                return predictDataRepository.save(predictData);
            } else {
                // Flask API调用失败，处理错误
                throw new OperationException("Flask API call failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new OperationException(e.getMessage());
        }
    }

    @Override
    public void deletePredictData(Long id) {
        try {
            Optional<PredictData> predictData = predictDataRepository.findById(id);
            if (predictData.isEmpty()) {
                throw new OperationException("Predict data not found with id: " + id);
            }
            String fileName = predictData.get().getFileName();
            String filePath = predictData.get().getFilePath();
            Map<String, String> body = new HashMap<>();
            body.put("fileName", fileName);
            body.put("filePath", filePath);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/delete-predict-data",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println(response);
                predictDataRepository.deleteById(id);
            } else {
                throw new OperationException("Error deleting predict data with id: " + id);
            }
        } catch (Exception e) {
            throw new OperationException("Error deleting predict data with id: " + id);
        }
    }

    @Override
    public PredictData getPredictDataById(Long id) {
        try {
            return predictDataRepository.findById(id)
                    .orElseThrow(() -> new OperationException("Predict data not found with id: " + id));
        } catch (Exception e) {
            throw new OperationException("Predict data not found with id: " + id);
        }
    }

    @Override
    public ResponseEntity<PredictData> checkPredictDataExist(String fileName){
        Optional<PredictData> optionalPredictData = predictDataRepository.findByFileName(fileName);
        return optionalPredictData.map(predictData -> new ResponseEntity<>(predictData, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public PredictData getPredictDataByName(String fileName) {
        try {
            return predictDataRepository.findByFileName(fileName)
                    .orElseThrow(() -> new OperationException("Predict data not found with name: " + fileName));
        } catch (Exception e) {
            throw new OperationException("Predict data not found with name: " + fileName);
        }
    }

    @Override
    public PredictData updateFileName(Long id, String newName) {
        try {
            Optional<PredictData> predictData = predictDataRepository.findById(id);
            if (predictData.isEmpty()) {
                throw new OperationException("Predict data not found with id: " + id);
            }
            String fileName = predictData.get().getFileName();
            String filePath = predictData.get().getFilePath();
            Map<String, String> body = new HashMap<>();
            body.put("fileName", fileName);
            body.put("newName", newName);
            body.put("filePath", filePath);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://flask:5001/rename-predict-data",
                    body,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String newPath = jsonNode.get("newPath").asText();
                return predictDataRepository.findById(id)
                        .map(model1 -> {
                            model1.setFileName(newName);
                            model1.setFilePath(newPath);
                            return predictDataRepository.save(model1);
                        })
                        .orElseThrow(() -> new OperationException("Predict data not found with id: " + id));
            }
        } catch (Exception e) {
            throw new OperationException("Predict data not found with id: " + id);
        }
        return null;
    }

    @Override
    public PredictData updateFilePath(Long id, String filePath) {
        try {
            return predictDataRepository.findById(id)
                    .map(model1 -> {
                        model1.setFilePath(filePath);
                        return predictDataRepository.save(model1);
                    })
                    .orElseThrow(() -> new OperationException("Predict data not found with id: " + id));
        } catch (Exception e) {
            throw new OperationException("Predict data not found with file path: " + filePath);
        }
    }

    @Override
    public List<PredictDataResponse> getAllPredictData() {
        try {
            List<PredictData> predictData = predictDataRepository.findAll();
            return predictData.stream().map(this::mapToPredictDataResponse).toList();
        } catch (Exception e) {
            throw new OperationException("No predict data found");
        }
    }

    private PredictDataResponse mapToPredictDataResponse(PredictData predictData) {
        return PredictDataResponse.builder()
                .id(predictData.getId())
                .fileName(predictData.getFileName())
                .filePath(predictData.getFilePath())
                .build();
    }
}
