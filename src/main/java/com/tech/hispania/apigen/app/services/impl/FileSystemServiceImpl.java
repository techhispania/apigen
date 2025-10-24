package com.tech.hispania.apigen.app.services.impl;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.FileSystemService;

@Service
public class FileSystemServiceImpl implements FileSystemService {

	private static final Logger logger = LogManager.getLogger(FileSystemServiceImpl.class);
	
	private File tempDirectory;
	
	public FileSystemServiceImpl() {
		this.tempDirectory = new File(System.getProperty("java.io.tmpdir"));
	}
	
	@Override
	public void setTempDirectory(String tempDirectory) {
		this.tempDirectory = new File(tempDirectory);
	}
	
	@Override
	public void createDirectory(String path) throws ApiGenException {
		
		try {
			File newDirectory = new File(tempDirectory, path);
			
			if (newDirectory.exists())
				throw new ApiGenException(HttpStatus.CONFLICT.value(), "The path '" + path + "' already exists");
			
			if (!newDirectory.mkdir())
				throw new ApiGenException(HttpStatus.BAD_REQUEST.value(), "Invalid path '" + path + "', can't be created");
		} catch (Exception e) {
			if (e instanceof ApiGenException)
				throw e;
			
			logger.error("Unexpected error generating directory.", e);
			throw new ApiGenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error generating directory. " + e.getMessage());
		}
	}
	
	@Override
	public void createRecursiveDirectory(String path) throws ApiGenException {
		
		String[] tree = path.split("/");
		
		StringBuilder processedPath = new StringBuilder();
		for(String directory : tree) {
			if (processedPath.toString().length() == 0) {
				try {
					createDirectory(directory);
					processedPath.append(directory);
				} catch (ApiGenException e) {
					if (HttpStatus.CONFLICT.value() == e.getCode()) {
						logger.info("The first directory '{}' already exists. Continue...", directory);
						processedPath.append(directory);
						continue;
					} else {
						logger.error("Unexpected error creating the first directory '{}'", directory, e);
						throw e;
					}
				}
			} else {
				processedPath.append("/").append(directory);
				try {
					createDirectory(processedPath.toString());	
				} catch (ApiGenException e) {
					if (HttpStatus.CONFLICT.value() == e.getCode()) {
						logger.info("The directory '{}' already exists. Continue...", directory);
						continue;
					} else {
						logger.error("Unexpected error creating the directory '{}'", directory, e);
						throw e;
					}
				}
			}
		}
	}
	
	@Override
	public void removeDirectory(String path) throws ApiGenException {
		logger.debug("Removing directory '{}'", path);
		File directory = new File(tempDirectory, path);
		
		if (!removeFilesRecursive(directory)) {
			logger.error("The full tree for path '{}' can't be removed", path);
			throw new ApiGenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The full tree for path '" + path + "' can't be removed");
		}
		logger.debug("Directory '{}' removed", path);
	}
	
	private boolean removeFilesRecursive(File directory) throws ApiGenException {
		if (!directory.exists()) {
		    logger.debug("File or directory '{}' does not exist. Skipping.", directory);
		    return true;
		}
		
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			
			if (files != null) {
				for (File file : files) {
					if (!removeFilesRecursive(file)) {
						logger.error("Failed to remove '{}'", file);
						throw new ApiGenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete '" + file.getAbsolutePath() + "'");
					}
				}
			}			
		}
		
		if (!directory.delete()) {
			logger.error("Failed to delete '{}'", directory.toString());
			throw new ApiGenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete '" + directory.getAbsolutePath() + "'");
		}
		return true;
	}
}
