package com.ziqi.mlplatform.controller;

import com.ziqi.mlplatform.Model.PredictData;
import com.ziqi.mlplatform.Service.PredictDataService;
import com.ziqi.mlplatform.dto.PredictDataRequest;
import com.ziqi.mlplatform.dto.PredictDataResponse;
import com.ziqi.mlplatform.dto.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/predict-data")
@RequiredArgsConstructor
public class PredictDataController {
    private final PredictDataService predictDataService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Predict Data Service is up and running!");
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPredictData(@RequestBody PredictDataRequest predictDataRequest) {
        PredictData predictData1 = predictDataService.createPredictData(predictDataRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(predictData1);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<PredictDataResponse> getAllData() {
        return predictDataService.getAllPredictData();
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredictData getPredictDataById(@PathVariable Long id) {
        return predictDataService.getPredictDataById(id);
    }

    @GetMapping("/get-by-name/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public PredictData getPredictDataByName(@PathVariable String fileName) {
        return predictDataService.getPredictDataByName(fileName);
    }

    @PostMapping("/update-file-name")
    public ResponseEntity<?> updateFileName(@RequestBody UpdateNameRequest request) {
        PredictData predictData = predictDataService.updateFileName(Long.valueOf(request.getId()), request.getNewName());
        if (predictData != null) {
            return ResponseEntity.ok(predictData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model name update failed.");
        }

    }

    @GetMapping("/check/{fileName}")
    public ResponseEntity<PredictData> checkPredictDataExist(@PathVariable String fileName) {
        return predictDataService.checkPredictDataExist(fileName);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePredictDataById(@PathVariable Long id) {
        predictDataService.deletePredictData(id);
        return ResponseEntity.noContent().build();
    }
}
