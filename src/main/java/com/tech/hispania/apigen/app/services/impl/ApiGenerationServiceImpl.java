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
import com.tech.hispania.apigen.app.services.PlaceholdersService;
import com.tech.hispania.apigen.app.services.TextUtilsService;
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
	
	@Autowired
	private TextUtilsService textUtilsService;
	
	@Autowired
	private PlaceholdersService placeholdersService;
	
	@Override
	public String generate(String apiName, Set<ApiGenEntity> entities) throws ApiGenException {
		
		String packageName = apiName.toLowerCase().replace(" ", "/");
		String apiNameCapitalized = textUtilsService.capitalize(apiName);
		
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
		fileSystemService.createFile(javaPackage, new StringBuilder(apiNameCapitalized).append("Application.java").toString());
		fileTemplateService.copyTemplateInFile("application", new StringBuilder(javaPackage).append(apiNameCapitalized).append("Application.java").toString());
		Map<String, String> placeholders = new HashMap<>();
		placeholders.put("package_name", packageName);
		placeholders.put("api_name", apiNameCapitalized);
		fileTemplateService.replacePlaceholders(new StringBuilder(javaPackage).append(apiNameCapitalized).append("Application.java").toString(), placeholders);
		entities.forEach(entity -> {
			try {
				logger.info("===============================");
				logger.info("Creating files for entity '{}'", entity);
				logger.info("===============================");
				String entityNameCapitalized = textUtilsService.capitalize(entity.name());
				String entityTableName = textUtilsService.buildEntityTableName(entity.name());
				
				fileSystemService.createFile(entitiesPackage, new StringBuilder(entityNameCapitalized).append(".java").toString());
				fileSystemService.createFile(controllersPackage, new StringBuilder(entityNameCapitalized).append("Controller.java").toString());
				fileSystemService.createFile(dtoPackage, new StringBuilder(entityNameCapitalized).append("CreateRequestDTO.java").toString());
				fileSystemService.createFile(dtoPackage, new StringBuilder(entityNameCapitalized).append("CreateResponseDTO.java").toString());
				fileSystemService.createFile(dtoMappingPackage, new StringBuilder(entityNameCapitalized).append("Mapping.java").toString());
				fileSystemService.createFile(controllersPackage, new StringBuilder(entityNameCapitalized).append("Controller.java").toString());
				fileSystemService.createFile(persistencePackage, new StringBuilder(entityNameCapitalized).append("Repository.java").toString());
				fileSystemService.createFile(servicesPackage, new StringBuilder(entityNameCapitalized).append("Service.java").toString());
				fileSystemService.createFile(servicesImplPackage, new StringBuilder(entityNameCapitalized).append("ServiceImpl.java").toString());
				
				
				logger.info("===============================");
				logger.info("Copying content from templates");
				logger.info("===============================");
				fileTemplateService.copyTemplateInFile("entity", new StringBuilder(entitiesPackage)
																							.append("/")
																							.append(entityNameCapitalized)
																							.append(".java")
																							.toString());
				fileTemplateService.copyTemplateInFile("controller", new StringBuilder(controllersPackage)
																							.append("/")
																							.append(entityNameCapitalized)
																							.append("Controller.java")
																							.toString());
				fileTemplateService.copyTemplateInFile("create_request_dto", new StringBuilder(dtoPackage)
																							.append("/")
																							.append(entityNameCapitalized)
																							.append("CreateRequestDTO.java")
																							.toString());
				fileTemplateService.copyTemplateInFile("create_response_dto", new StringBuilder(dtoPackage)
																							.append("/")
																							.append(entityNameCapitalized)
																							.append("CreateResponseDTO.java")
																							.toString());
				fileTemplateService.copyTemplateInFile("mapping", new StringBuilder(dtoMappingPackage)
																							.append("/")
																							.append(entityNameCapitalized)
																							.append("Mapping.java")
																							.toString());
				logger.info("===============================");
				logger.info("Replacing placeholders");
				logger.info("===============================");
				Map<String, String> entityPlaceholders = new HashMap<>();
				entityPlaceholders.put("package_name", packageName);
				entityPlaceholders.put("api_name", apiNameCapitalized);
				entityPlaceholders.put("entity_name", entityNameCapitalized);
				entityPlaceholders.put("create_dto_parameters", placeholdersService.buildCreateDTOParameters(entity.properties()));
				entityPlaceholders.put("entity_properties_imports", placeholdersService.buildEntityPropertiesImports(entity.properties()));
				entityPlaceholders.put("mapping_imports", placeholdersService.buildMappingImports(packageName, entityNameCapitalized));
				entityPlaceholders.put("create_request_mapping_to_entity_setters", placeholdersService.buildCreateRequestMappingToEntitySetters(entity.properties()));
				entityPlaceholders.put("create_response_mapping_to_dto_setters", placeholdersService.buildCreateResponseMappingToDtoSetters(entity.properties()));
				entityPlaceholders.put("create_response_dto_attributes", placeholdersService.buildCreateResponseDTOAttributes(entity.properties()));
				entityPlaceholders.put("entity_table_name", entityTableName);
				entityPlaceholders.put("entity_attributes", placeholdersService.buildEntityAttributes(entity.properties()));
				entityPlaceholders.put("entity_getters_setters", placeholdersService.buildEntityGettersSetters(entity.properties()));
				entityPlaceholders.put("entity_to_string", placeholdersService.buildEntityToString(entity));
				fileTemplateService.replacePlaceholders(new StringBuilder(entitiesPackage)
																				.append("/")
																				.append(entityNameCapitalized)
																				.append(".java")
																				.toString(), entityPlaceholders);
				fileTemplateService.replacePlaceholders(new StringBuilder(controllersPackage)
																				.append("/")
																				.append(entityNameCapitalized)
																				.append("Controller.java")
																				.toString(), entityPlaceholders);
				
				fileTemplateService.replacePlaceholders(new StringBuilder(dtoPackage)
																				.append("/")
																				.append(entityNameCapitalized)
																				.append("CreateRequestDTO.java")
																				.toString(), entityPlaceholders);
				fileTemplateService.replacePlaceholders(new StringBuilder(dtoPackage)
																				.append("/")
																				.append(entityNameCapitalized)
																				.append("CreateResponseDTO.java")
																				.toString(), entityPlaceholders);
				fileTemplateService.replacePlaceholders(new StringBuilder(dtoMappingPackage)
																				.append("/")
																				.append(entityNameCapitalized)
																				.append("Mapping.java")
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
}