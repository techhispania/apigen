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
				try {
					result.append(getAttributeType(property.type())).append(property.name()).append(", ");
				} catch (Exception e) {
					logger.error("Entity {} can't be added to placeholder", property.name());
				}
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
	
	@Override
	public String buildCreateResponseDTOAttributes(List<ApiGenProperty> properties) {
		StringBuilder result = new StringBuilder();
		properties.forEach(property -> {
			try {
				result.append("private ").append(getAttributeType(property.type())).append(property.name()).append(";\n\t");
			} catch (Exception e) {
				logger.error("Entity {} can't be added to the placeholder", property.name());
			}
		});
		result.append("\n");
		properties.forEach(property -> {
			try {
				result.append("\tpublic ").append(getAttributeType(property.type())).append("get").append(textUtilsService.capitalize(property.name())).append("() {\n");
				result.append("\t\treturn this.").append(property.name()).append(";\n");
				result.append("\t}\n\n");
				
				result.append("\tpublic void ").append("set").append(textUtilsService.capitalize(property.name())).append("(").append(getAttributeType(property.type())).append(property.name()).append(") {\n");
				result.append("\t\tthis.").append(property.name()).append(" = ").append(property.name()).append(";\n");
				result.append("\t}\n\n");
			} catch (Exception e) {
				logger.error("Entity methods {} can't be added to the placeholder", property.name());
			}
		});		
		return result.toString().substring(0, result.toString().length() - 2);
	}
	
	private String getAttributeType(PropertyType type) throws Exception {
		String result = "";
		switch (type) {
		case PropertyType.TEXT:
		case PropertyType.BIG_TEXT:
			result = "String ";
			break;
		case PropertyType.INTEGER:
			result = "int ";
			break;
		case PropertyType.LONG_INTEGER:
			result = "long ";
			break;
		case PropertyType.FLOAT:
			result = "float ";
			break;
		case PropertyType.BOOLEAN:
			result = "boolean ";
			break;
		case PropertyType.DATE:
		case PropertyType.TIMESTAMP:
			result = "LocalDateTime ";
			break;
		default:
			logger.error("Invalid type '{}'", type);
			throw new IllegalStateException("Invalid type '" + type + "'");
		}
		return result;
	}
}
