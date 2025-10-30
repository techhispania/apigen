package com.tech.hispania.apigen.ui.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.ApiGenerationService;
import com.tech.hispania.apigen.domain.model.ApiGenEntity;
import com.tech.hispania.apigen.domain.model.ApiGenProperty;
import com.tech.hispania.apigen.domain.model.PropertyType;
import com.tech.hispania.apigen.ui.dto.GenerateRequestDTO;
import com.tech.hispania.apigen.ui.dto.GenerateResponseDTO;
import com.tech.hispania.apigen.ui.dto.ResponseDTO;

@RestController
@RequestMapping("/api")
public class ApiController {

	private static final Logger logger = LogManager.getLogger(ApiController.class);
	
	@Autowired
	private ApiGenerationService apiGenerationService;
	
	@PostMapping("/generate")
	public ResponseEntity<ResponseDTO> generate(@RequestBody GenerateRequestDTO dto) {
		logger.info("Generate REST API request received. {}", dto);
		
		try {
			// =======================================
			// TEST DATA
			String apiName = "Planes";
			Set<ApiGenEntity> entities = new HashSet<>();
			
			ApiGenProperty modelProp = new ApiGenProperty("model", PropertyType.TEXT);
			ApiGenProperty companyProp = new ApiGenProperty("company", PropertyType.TEXT);
			ApiGenProperty dateProp = new ApiGenProperty("createdAt", PropertyType.TIMESTAMP);
			
			List<ApiGenProperty> planeEntityProperties = List.of(modelProp, companyProp, dateProp);
			ApiGenEntity planeEntity = new ApiGenEntity("plane", planeEntityProperties);
			
			entities.add(planeEntity);

			// END TEST DATA
			// =======================================

			String result = apiGenerationService.generate(apiName, entities);
		} catch (ApiGenException e) {
			logger.error("Error generating REST API.", e);
		}
		
		GenerateResponseDTO response = new GenerateResponseDTO();
		response.setResult("success");
		return ResponseEntity.ok(response);
	}
}
