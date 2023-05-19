package com.ziqi.mlplatform.Service;

import com.ziqi.mlplatform.Model.TrainData;
import com.ziqi.mlplatform.Repository.UploadFileRepository;
import com.ziqi.mlplatform.dto.TrainDataResponse;
import com.ziqi.mlplatform.exception.OperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainDataService implements ITrainDataService {
    private final UploadFileRepository repository;

    @Override
    public TrainData createTrainData(TrainData uploadFile) {
        try {
            return repository.save(uploadFile);
        } catch (Exception e){
            throw new OperationException(e.getMessage());
        }
    }

    @Override
    public List<TrainDataResponse> getAllTrainData(){
        try {
            List<TrainData> uploadFile = repository.findAll();
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
                .build();
    }

    @Override
    public void deleteTrainData(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new OperationException("Upload file not found with id: " + id);
        }
    }

    @Override
    public TrainData updateFileName(Long id, String modelName) {
        return repository.findById(id)
                .map(model1 -> {
                    model1.setFileName(modelName);
                    return repository.save(model1);
                })
                .orElseThrow(() -> new OperationException("Upload file not found with id: " + id));
    }

    @Override
    public TrainData getTrainDataById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OperationException("Upload file not found with id: " + id));
    }

    @Override
    public TrainData getUploadFileByName(String modelName) {
        return repository.findByFileName(modelName)
                .orElseThrow(() -> new OperationException("Model not found with model name: " + modelName));
    }

    @Override
    public Optional<TrainData> findByFileName(String fileName) {
        return repository.findByFileName(fileName);
    }

    @Override
    public TrainData updateTemplateName(Long id, String templateName) {
        return repository.findById(id)
                .map(model1 -> {
                    model1.setTemplateName(templateName);
                    return repository.save(model1);
                })
                .orElseThrow(() -> new OperationException("Model not found with id: " + id));
    }

    @Override
    public TrainData getTemplateByName(String templateName) {
        return repository.findByTemplateName(templateName)
                .orElseThrow(() -> new OperationException("Model not found with model name: " + templateName));
    }
}
