package com.tech.hispania.apigen.app.services;

import java.util.List;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.domain.model.ApiGenProperty;

public interface PlaceholdersService {

	/**
	 * Method to build the placeholder value to be used in the classes generated from
	 * the template create_request_dto.template
	 * 
	 * @param properties List of properties of one Entity defined by the user. 
	 * @return The string to be replaced in the class by the placeholder "{create_dto_parameters}"
	 * @throws ApiGenException
	 */
	String buildCreateDTOParameters(List<ApiGenProperty> properties) throws ApiGenException;
	
	/**
	 * Method to build the placeholder value to be used in the classes generated from 
	 * the templates related to DTOs. Basically this method detects if an entity is a 
	 * a date, to import the LocalDateTime.
	 * 
	 * @param properties List of properties of one Entity defined by the user.
	 * @return The string to be replaced in the class by the placeholder "{entity_properties_imports}"
	 */
	String buildEntityPropertiesImports(List<ApiGenProperty> properties);
	
	/**
	 * Method to build the placeholder value to be used in the classes generated from
	 * the template "mapping.template". 
	 * This method generates the code to transform a dto into one entity.
	 * 
	 * @param properties List of properties of one Entity defined by the user
	 * @return The string to be replaced in the class by the placeholder "{create_request_mapping_to_entity_setters}"
	 */
	String buildCreateRequestMappingToEntitySetters(List<ApiGenProperty> properties);
	
	/**
	 * Method to build the placeholder value to be used in the classes generated from 
	 * the template "mapping.template".
	 * It generates the import to be added that corresponds to the DTO class used in the
	 * mapping class
	 * 
	 * @param apiName The name of the API defined by the user
	 * @param entityNameCapitalized The name of the Entity capitalized.
	 * @return The string to be replaced in the class by the placeholder "{mapping_imports}"
	 */
	String buildMappingImports(String apiName, String entityNameCapitalized);
	
	/**
	 * Method to build the placeholder value to be used in the classes generated from 
	 * the template "create_response_dto.template".
	 * 
	 * @param properties List of properties of one Entity defined by the user
	 * @return The string to be replaced in the class by the placeholder "{create_response_dto_attributes}"
	 */
	String buildCreateResponseDTOAttributes(List<ApiGenProperty> properties);
}
