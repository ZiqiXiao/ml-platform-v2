package com.ziqi.mlplatform.controller;

import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Service.IModelService;
import com.ziqi.mlplatform.Service.ITrainDataService;
import com.ziqi.mlplatform.dto.ModelResponse;
import com.ziqi.mlplatform.dto.SaveModelRequest;
import com.ziqi.mlplatform.dto.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {

    private final IModelService modelService;

    private final ITrainDataService uploadFileService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Model Service is up and running!");
    }

    @PostMapping("/create-with-file")
    @ResponseStatus(HttpStatus.CREATED)
    public Model createModelWithFile(@RequestBody Model model) {
        return modelService.createModelWithFile(model);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createModel(@RequestBody SaveModelRequest saveModelRequest) {
        Model model = modelService.createModel(saveModelRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<ModelResponse> getAllModels() {
        return modelService.getAllData();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Model> getModel(@PathVariable Long id) {
        Model model = modelService.getModelById(id);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/get-by-name/{modelName}")
    public ResponseEntity<Model> getModelByModelName(@PathVariable String modelName) {
        Model model = modelService.getModelByName(modelName);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/update-model-name")
    public ResponseEntity<Model> updateModelName(@RequestBody UpdateNameRequest request) {
        Model updateModel = modelService.updateModelName(Long.valueOf(request.getId()), request.getNewName());
        return ResponseEntity.ok(updateModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
}
