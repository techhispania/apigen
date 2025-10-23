package com.tech.hispania.apigen.app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.ApiGenerationService;
import com.tech.hispania.apigen.app.services.FileSystemService;

@Service
public class ApiGenerationServiceImpl implements ApiGenerationService {

	@Autowired
	private FileSystemService fileSystemService;
	
	@Override
	public String generate() throws ApiGenException {
		
		fileSystemService.setTempDirectory(".");
		
		fileSystemService.createDirectory("api-rest");
		fileSystemService.createDirectory("api-rest/src");
		fileSystemService.createDirectory("api-rest/src/main");
		fileSystemService.createDirectory("api-rest/src/main/java");
		fileSystemService.createDirectory("api-rest/src/main/resources");
		
		return null;
	}
}
