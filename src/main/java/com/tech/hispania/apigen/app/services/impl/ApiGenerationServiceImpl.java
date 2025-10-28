package com.tech.hispania.apigen.app.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.ApiGenerationService;
import com.tech.hispania.apigen.app.services.FileSystemService;
import com.tech.hispania.apigen.app.services.FileTemplateService;

@Service
public class ApiGenerationServiceImpl implements ApiGenerationService {

	private static final Logger logger = LogManager.getLogger(ApiGenerationServiceImpl.class);
	
	@Autowired
	private FileSystemService fileSystemService;
	
	@Autowired
	private FileTemplateService fileTemplateService;
	
	@Override
	public String generate(String apiName) throws ApiGenException {
		
		fileSystemService.setTempDirectory(".");
		
		logger.info("Creating API packages structure");
		fileSystemService.createRecursiveDirectory("api-rest/src/main/java/com/apigen/" + apiName);
		fileSystemService.createRecursiveDirectory("api-rest/src/main/resources");
		
		logger.info("Creating Main file");
		fileSystemService.createFile("api-rest/src/main/java/com/apigen/" + apiName, "App.java");
		
		logger.info("Copying content from template");
		fileTemplateService.copyTemplateInFile("main", "api-rest/src/main/java/com/apigen/" + apiName + "/App.java");
		
		logger.info("Cleaning temporal directory");
		//fileSystemService.removeDirectory("api-rest");
		
		return null;
	}
}
