package com.tech.hispania.apigen.app.services;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;

public interface FileSystemService {

	/**
	 * Set the base path from where the directories and
	 * files will be created.
	 * By default, if no tempDirectory is set, it will be used 
	 * the Java tmp directory "java.io.tmpdir"
	 * 
	 * @param tempDirectory The base path to be set
	 */
	void setTempDirectory(String tempDirectory);
	
	/**
	 * Create one directory.
	 * This method is used to create directories during the API REST generation
	 * 
	 * @param path The full path of the directory to be created
	 * @throws ApiGenException
	 */
	void createDirectory(String path) throws ApiGenException;

	/**
	 * Create one directory and the parents if they not exists.
	 * This method is used to create directories during the API REST generation
	 * 
	 * @param path The full path of the directory to be created
	 * @throws ApiGenException
	 */
	void createRecursiveDirectory(String path) throws ApiGenException;
}
