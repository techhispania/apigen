package com.tech.hispania.apigen.app.services;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;

public interface FileTemplateService {

	/**
	 * Copy one template in one empty file
	 * 
	 * @param template The name of the template to be copied
	 * @param path The full path to the file where the template must be copy
	 * @throws ApiGenException
	 */
	void copyTemplateInFile(String template, String path) throws ApiGenException;
}
