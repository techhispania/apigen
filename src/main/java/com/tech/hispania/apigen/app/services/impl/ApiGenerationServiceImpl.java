package com.tech.hispania.apigen.app.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.ApiGenerationService;
import com.tech.hispania.apigen.app.services.FileSystemService;
import com.tech.hispania.apigen.app.services.FileTemplateService;
import com.tech.hispania.apigen.domain.model.ApiGenEntity;

@Service
public class ApiGenerationServiceImpl implements ApiGenerationService {

	private static final Logger logger = LogManager.getLogger(ApiGenerationServiceImpl.class);
	
	private static final String PROJECT_BASE_PATH = "api-rest/src/main/";
	private static final String JAVA_BASE_PATH = new StringBuilder(PROJECT_BASE_PATH).append("java/com/apigen/").toString();
	private static final String RESOURCES_BASE_PATH = new StringBuilder(PROJECT_BASE_PATH).append("resources/").toString();
	
	@Autowired
	private FileSystemService fileSystemService;
	
	@Autowired
	private FileTemplateService fileTemplateService;
	
	@Override
	public String generate(String apiName, Set<ApiGenEntity> entities) throws ApiGenException {
		
		String packageName = apiName.toLowerCase().replace(" ", "/");
		
		String javaPackage = new StringBuilder(JAVA_BASE_PATH).append(packageName).append("/").toString();
		String controllersPackage = new StringBuilder(javaPackage).append("ui/controllers").toString();
		String dtoPackage = new StringBuilder(javaPackage).append("ui/dto").toString();
		String dtoMappingPackage = new StringBuilder(javaPackage).append("ui/dto/mapping").toString();
		String entitiesPackage = new StringBuilder(javaPackage).append("domain/model/entities").toString();
		String servicesPackage = new StringBuilder(javaPackage).append("app/services").toString();
		String servicesImplPackage = new StringBuilder(javaPackage).append("app/services/impl").toString();
		String exceptionsPackage = new StringBuilder(javaPackage).append("app/exceptions").toString();
		String persistencePackage = new StringBuilder(javaPackage).append("infrastructure/persistence").toString();
		
		fileSystemService.setTempDirectory(".");
		
		logger.info("===============================");
		logger.info("Creating API packages structure");
		logger.info("===============================");
		fileSystemService.createRecursiveDirectory(javaPackage);
		fileSystemService.createRecursiveDirectory(controllersPackage);
		fileSystemService.createRecursiveDirectory(dtoPackage);
		fileSystemService.createRecursiveDirectory(dtoMappingPackage);
		fileSystemService.createRecursiveDirectory(entitiesPackage);
		fileSystemService.createRecursiveDirectory(servicesImplPackage);
		fileSystemService.createRecursiveDirectory(exceptionsPackage);
		fileSystemService.createRecursiveDirectory(persistencePackage);
		fileSystemService.createRecursiveDirectory(RESOURCES_BASE_PATH);
		
		logger.info("===============================");
		logger.info("Creating files");
		logger.info("===============================");
		fileSystemService.createFile(javaPackage, new StringBuilder(capitalize(apiName)).append("Application.java").toString());
		fileTemplateService.copyTemplateInFile("application", new StringBuilder(javaPackage).append(capitalize(apiName)).append("Application.java").toString());
		Map<String, String> placeholders = new HashMap<>();
		placeholders.put("package_name", packageName);
		placeholders.put("api_name", capitalize(apiName));
		fileTemplateService.replacePlaceholders(new StringBuilder(javaPackage).append(capitalize(apiName)).append("Application.java").toString(), placeholders);
		entities.forEach(entity -> {
			try {
				logger.info("===============================");
				logger.info("Creating files for entity '{}'", entity);
				logger.info("===============================");
				fileSystemService.createFile(entitiesPackage, new StringBuilder(capitalize(entity.name())).append(".java").toString());
				fileSystemService.createFile(controllersPackage, new StringBuilder(capitalize(entity.name())).append("Controller.java").toString());
				fileSystemService.createFile(dtoPackage, new StringBuilder(capitalize(entity.name())).append("CreateRequestDTO.java").toString());
				fileSystemService.createFile(dtoPackage, new StringBuilder(capitalize(entity.name())).append("CreateResponseDTO.java").toString());
				fileSystemService.createFile(dtoMappingPackage, new StringBuilder(capitalize(entity.name())).append("Mapping.java").toString());
				fileSystemService.createFile(controllersPackage, new StringBuilder(capitalize(entity.name())).append("Controller.java").toString());
				fileSystemService.createFile(persistencePackage, new StringBuilder(capitalize(entity.name())).append("Repository.java").toString());
				fileSystemService.createFile(servicesPackage, new StringBuilder(capitalize(entity.name())).append("Service.java").toString());
				fileSystemService.createFile(servicesImplPackage, new StringBuilder(capitalize(entity.name())).append("ServiceImpl.java").toString());
				
				
				logger.info("===============================");
				logger.info("Copying content from templates");
				logger.info("===============================");
				fileTemplateService.copyTemplateInFile("controller", new StringBuilder(controllersPackage)
																							.append("/")
																							.append(capitalize(entity.name()))
																							.append("Controller.java")
																							.toString());
				
				logger.info("===============================");
				logger.info("Replacing placeholders");
				logger.info("===============================");
				Map<String, String> entityPlaceholders = new HashMap<>();
				entityPlaceholders.put("package_name", packageName);
				entityPlaceholders.put("api_name", capitalize(apiName));
				entityPlaceholders.put("entity_name", capitalize(entity.name()));
				fileTemplateService.replacePlaceholders(new StringBuilder(controllersPackage)
																				.append("/")
																				.append(capitalize(entity.name()))
																				.append("Controller.java")
																				.toString(), entityPlaceholders);
				
			} catch (ApiGenException e) {
				logger.warn("Error creating files for entity '{}'.", entity.name(), e);
			}
		});
		
		logger.info("===============================");
		logger.info("Cleaning temporal directory");
		logger.info("===============================");
//		fileSystemService.removeDirectory("api-rest");
		
		return null;
	}
	
	private String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
	}
}
