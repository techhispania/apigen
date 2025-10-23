package com.tech.hispania.apigen.app.services;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;

public interface FileSystemService {

	/**
	 * Create one directory.
	 * This method is used to create directories during the API REST generation
	 * 
	 * @param path The full path of the directory to be created
	 * @throws ApiGenException
	 */
	void createDirectory(String path) throws ApiGenException;
}
