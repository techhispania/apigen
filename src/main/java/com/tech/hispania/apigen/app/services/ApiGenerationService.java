package com.tech.hispania.apigen.app.services;

import java.util.Set;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.domain.model.ApiGenEntity;

public interface ApiGenerationService {

	/**
	 * Method that initialize the process to generate the 
	 * API REST
	 * 
	 * @param apiName The name of the REST API to be generated
	 * @param entities The entities that has to be generated in the model
	 * @return String with the path of the .zip file generated
	 * @throws ApiGenException
	 */
	String generate(String apiName, Set<ApiGenEntity> entities) throws ApiGenException;
}
