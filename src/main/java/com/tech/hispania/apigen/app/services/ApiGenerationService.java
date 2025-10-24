package com.tech.hispania.apigen.app.services;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;

public interface ApiGenerationService {

	/**
	 * Method that initialize the process to generate the 
	 * API REST
	 * 
	 * @param apiName The name of the REST API to be generated
	 * @return String with the path of the .zip file generated
	 * @throws ApiGenException
	 */
	String generate(String apiName) throws ApiGenException;
}
