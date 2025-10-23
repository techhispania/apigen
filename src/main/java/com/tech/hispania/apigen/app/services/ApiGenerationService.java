package com.tech.hispania.apigen.app.services;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;

public interface ApiGenerationService {

	/**
	 * Method that initialize the process to generate the 
	 * API REST
	 * 
	 * @return String with the path of the .zip file generated
	 * @throws ApiGenException
	 */
	String generate() throws ApiGenException;
}
