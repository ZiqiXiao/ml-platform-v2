package com.ziqi.mlplatform.controller;

import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Service.ITrainDataService;
import com.ziqi.mlplatform.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/train-data")
@RequiredArgsConstructor
public class TrainDataController {

    private final ITrainDataService trainDataService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Upload File Service is up and running!");
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainData createUploadFile(@RequestBody TrainData uploadFile) {
        return trainDataService.createTrainData(uploadFile);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainDataResponse> getAllData() {
        return trainDataService.getAllTrainData();
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainDataResponse> getUploadFileById(@PathVariable Long id) {
        return trainDataService.getTrainDataById(id);
    }

    @GetMapping("/get-by-name/{fileName}")
    public ResponseEntity<?> getUploadFileByName(@PathVariable String fileName) {
        try{
            TrainData trainData = trainDataService.getUploadFileByName(fileName);
            return ResponseEntity.ok(trainData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found with model name: " + fileName);
        }
    }

    @GetMapping("/get-template-by-name/{templateName}")
    public ResponseEntity<?> getTemplateByName(@PathVariable String templateName) {
        try{
            TrainData trainData = trainDataService.getTemplateByName(templateName);
            return ResponseEntity.ok(trainData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found with model name: " + templateName);
        }
    }

    @PostMapping("/update-file-name")
    public ResponseEntity<TrainData> updateModelName(@RequestBody UpdateNameRequest request) {
        TrainData trainData = trainDataService.updateFileName(Long.valueOf(request.getId()), request.getNewName());
        return ResponseEntity.ok(trainData);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        trainDataService.deleteTrainData(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-template-name")
    public ResponseEntity<TrainData> updateTemplateName(@RequestBody UpdateNameRequest request) {
        TrainData trainData = trainDataService.updateTemplateName(Long.valueOf(request.getId()), request.getNewName());
        return ResponseEntity.ok(trainData);
    }

}
