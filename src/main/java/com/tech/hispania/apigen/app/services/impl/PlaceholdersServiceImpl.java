package com.tech.hispania.apigen.app.services.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.PlaceholdersService;
import com.tech.hispania.apigen.app.services.TextUtilsService;
import com.tech.hispania.apigen.domain.model.ApiGenProperty;
import com.tech.hispania.apigen.domain.model.PropertyType;

@Service
public class PlaceholdersServiceImpl implements PlaceholdersService {

	private static final Logger logger = LogManager.getLogger(PlaceholdersServiceImpl.class);
	
	@Autowired
	private TextUtilsService textUtilsService;
	
	@Override
	public String buildCreateDTOParameters(List<ApiGenProperty> properties) throws ApiGenException {
		StringBuilder result = new StringBuilder();
		try {
			properties.forEach(property -> {
				switch (property.type()) {
				case PropertyType.TEXT:
				case PropertyType.BIG_TEXT:
					result.append("String ");
					break;
				case PropertyType.INTEGER:
					result.append("int ");
					break;
				case PropertyType.LONG_INTEGER:
					result.append("long ");
					break;
				case PropertyType.FLOAT:
					result.append("float ");
					break;
				case PropertyType.BOOLEAN:
					result.append("boolean ");
					break;
				case PropertyType.DATE:
				case PropertyType.TIMESTAMP:
					result.append("LocalDateTime ");
					break;
				default:
					logger.error("Invalid type '{}'", property.type());
					throw new IllegalStateException("Invalid type '" + property.type() + "'");
				}
				result.append(property.name()).append(", ");
			});
		} catch (IllegalStateException e) {
			throw new ApiGenException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
		return result.toString().substring(0, result.toString().length() - 2);
	}
	
	@Override
	public String buildEntityPropertiesImports(List<ApiGenProperty> properties) {
		StringBuilder result = new StringBuilder();
		properties.forEach(property -> {
			switch (property.type()) {
			case PropertyType.DATE:
			case PropertyType.TIMESTAMP:
				result.append("import java.time.LocalDateTime;");
				break;
			default:
				logger.debug("No import to add");
			}
		});
		return result.toString();
	}
	
	@Override
	public String buildCreateRequestMappingToEntitySetters(List<ApiGenProperty> properties) {
		StringBuilder result = new StringBuilder();
		properties.forEach(property -> {
			String propertyNameCapitalized = textUtilsService.capitalize(property.name());
			result.append("entity.set").append(propertyNameCapitalized).append("(").append("dto.get").append(propertyNameCapitalized).append("());").append("\n\t\t");
		});
		return result.toString().substring(0, result.toString().length() - 3);
	}
	
	@Override
	public String buildMappingImports(String apiName, String entityNameCapitalized) {
		StringBuilder result = new StringBuilder();		
		result.append("import com.apigen.").append(apiName).append(".ui.dto.").append(entityNameCapitalized).append("CreateRequestDTO;");
		return result.toString();
	}
}
