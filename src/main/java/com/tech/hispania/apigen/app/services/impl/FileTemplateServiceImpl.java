package com.tech.hispania.apigen.app.services.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
	@Override
	public void replacePlaceholders(String path, Map<String, String> placeholders) throws ApiGenException {
		logger.debug("Replacing placeholders in file '{}'", path);
		
		try {
			File file = new File(path);
			List<String> lines = Files.readAllLines(file.toPath());
		
			List<String> modifiedLines = new ArrayList<>();
			
			lines.forEach(line -> {
				logger.debug("Processing line: {}", line);
				for (Entry<String, String> placeholder : placeholders.entrySet()) {
					String key = "{" + placeholder.getKey() + "}";
					String value = placeholder.getValue();
					
					if (line.contains(key)) {
						logger.debug("Replacing placeholder: '{}' with value '{}'", key, value);
						line = line.replace(key, value);
					}
				}
				modifiedLines.add(line);
			});
			
			logger.debug("All lines processed. Writing in the file");
			Files.write(file.toPath(), modifiedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			logger.error("Unexpected error replacing placeholders in file '{}'", path, e);
			throw new ApiGenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error replacing placeholders in file '" + path + "'. " + e.getMessage());
		}
	}
}
