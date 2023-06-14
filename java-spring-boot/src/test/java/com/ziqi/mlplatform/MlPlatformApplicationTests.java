package com.ziqi.mlplatform;

import com.ziqi.mlplatform.Repository.ModelRepository;
import com.ziqi.mlplatform.dto.ModelRequest;
import com.ziqi.mlplatform.dto.TrainDataRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class MlPlatformApplicationTests {

//	@Container
//	public static final MySQLContainer<?> mySQLContainer = new MySQLContainer<> ("mysql:8.0");
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Autowired
//	private ModelRepository repository;
//
//	@DynamicPropertySource
//	public static void setDatasourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
//		dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//		dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
//		dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
//	}
//
//	private ModelRequest getModelRequest() {
//		return ModelRequest.builder()
//				.modelName("myModel")
//				.modelClass("XGBoost")
//				.modelPath("myModelPath")
//				.build();
//	}
//
//	// private TrainDataRequest getUploadFileRequest() {
//	// 	return TrainDataRequest.builder()
//	// 			.fileName("myFile")
//	// 			.filePath("myFilePath")
//	// 			.build();
//	// }
//
//	@Test
//	void controllerHealthCheck() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.get("/model/health-check"))
//				.andExpect(status().isOk());
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/train-data/health-check"))
//				.andExpect(status().isOk());
//	}
//
//	@Test
//	void shouldCreateModel() throws Exception {
//		ModelRequest modelRequest = getModelRequest();
//		String modelRequestString = null;
//		try {
//			 modelRequestString = objectMapper.writeValueAsString(modelRequest);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		assert modelRequestString != null;
//		mockMvc.perform(MockMvcRequestBuilders.post("/model/create")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(modelRequestString))
//		 		.andExpect(status().isCreated());
//	}
//
//
//	@Test
//	void shouldGetModel() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/model/get/{id}", 1)
//					.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/model/get-by-name/{modelName}", "myModel")
//						.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//	}
//
//
//	@Test
//	void shouldCreateUploadFile() throws Exception {
//		ModelRequest modelRequest = getModelRequest();
//		String modelRequestString = null;
//		try {
//			modelRequestString = objectMapper.writeValueAsString(modelRequest);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		assert modelRequestString != null;
//		mockMvc.perform(MockMvcRequestBuilders.post("/train-data/create")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(modelRequestString))
//				.andExpect(status().isCreated());
//	}
//
//
//	@Test
//	void shouldGetUploadFile() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/train-data/get/{id}", 1)
//						.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/train-data/get-by-name/{modelName}", "myUploadFile")
//						.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//	}

}
