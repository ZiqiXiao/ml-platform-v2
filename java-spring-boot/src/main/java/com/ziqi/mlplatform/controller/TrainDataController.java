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

    @GetMapping("/get-by-name/{modelName}")
    public ResponseEntity<TrainData> getModelByModelName(@PathVariable String modelName) {
        TrainData uploadFile = trainDataService.getUploadFileByName(modelName);
        return ResponseEntity.ok(uploadFile);
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
