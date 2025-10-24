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
	
	/**
	 * Remove a directory and all the files and sub folders that are inside that directory
	 * 
	 * @param path The path of the directory to be removed
	 * @throws ApiGenException
	 */
	void removeDirectory(String path) throws ApiGenException;
	
	/**
	 * Create a file in one specific path
	 * 
	 * @param path The path where the file must be created
	 * @param filename The name of the file to be created
	 * @throws ApiGenException
	 */
	void createFile(String path, String filename) throws ApiGenException;
}
