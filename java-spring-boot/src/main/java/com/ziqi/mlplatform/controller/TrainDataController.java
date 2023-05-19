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
    public ResponseEntity<TrainData> getUploadFileById(@PathVariable Long id) {
        TrainData uploadFile = trainDataService.getTrainDataById(id);
        return ResponseEntity.ok(uploadFile);
    }

    @GetMapping("/get-by-name/{fileName}")
    public ResponseEntity<?> getUploadFileByName(@PathVariable String fileName) {
        try{
            TrainData uploadFile = trainDataService.getUploadFileByName(fileName);
            return ResponseEntity.ok(uploadFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found with model name: " + fileName);
        }
    }

    @GetMapping("/get-template-by-name/{templateName}")
    public ResponseEntity<?> getTemplateByName(@PathVariable String templateName) {
        try{
            TrainData uploadFile = trainDataService.getTemplateByName(templateName);
            return ResponseEntity.ok(uploadFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found with model name: " + templateName);
        }
    }

    @PostMapping("/update-model-name")
    public ResponseEntity<TrainData> updateModelName(@RequestBody UpdateNameRequest request) {
        TrainData uploadFile = trainDataService.updateFileName(Long.valueOf(request.getId()), request.getNewName());
        return ResponseEntity.ok(uploadFile);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        trainDataService.deleteTrainData(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-template-name")
    public ResponseEntity<TrainData> updateTemplateName(@RequestBody UpdateNameRequest request) {
        TrainData updateModel = trainDataService.updateTemplateName(Long.valueOf(request.getId()), request.getNewName());
        return ResponseEntity.ok(updateModel);
    }

}
