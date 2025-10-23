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
	
	/**
	 * Constructor where you use the Java tmp directory as base path to
	 * create a directory or a file
	 */
	public FileSystemServiceImpl() {
		this.tempDirectory = new File(System.getProperty("java.io.tmpdir"));
	}
	
	/**
	 * Constructor where you can set the base path from where you want to
	 * create a directory or a file
	 * 
	 * @param tempDirectory
	 */
	public FileSystemServiceImpl(String tempDirectory) {
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
}
