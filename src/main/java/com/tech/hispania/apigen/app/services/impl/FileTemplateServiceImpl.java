package com.tech.hispania.apigen.app.services.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.FileTemplateService;

@Service
public class FileTemplateServiceImpl implements FileTemplateService {

	private static final Logger logger = LogManager.getLogger(FileTemplateServiceImpl.class);
	
	private static final String TEMPLATE_BASE_PATH = "src/main/resources/apigen_templates";
	
	@Override
	public void copyTemplateInFile(String template, String path) throws ApiGenException {
		logger.debug("Copying template '{}' in file '{}'", template, path);
		
		if (!template.contains(".template"))
			template = new StringBuilder(template).append(".template").toString();
		
		try {
			File templateFile = new File(TEMPLATE_BASE_PATH, template);
			File file = new File(path);
		
			Files.copy(templateFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			logger.error("Unexpected error copying from template '{}' into file '{}'", template, path, e);
			throw new ApiGenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error copying from template '" + template + "' into file '" + path + "'. " + e.getMessage());
		}
	}
}
