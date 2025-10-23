package com.tech.hispania.apigen.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.FileSystemService;
import com.tech.hispania.apigen.app.services.impl.FileSystemServiceImpl;

public class FileSystemServiceTest {

	@TempDir
	private Path tempDirectory;
	
	private FileSystemService fileSystemService = new FileSystemServiceImpl();
	
	@Test
	void givenOneValidPathThenCreatesTheDirectory() throws ApiGenException {
		
		String path = "test_path";
		
		fileSystemService.setTempDirectory(tempDirectory.toString());
		
		fileSystemService.createDirectory(path);
	}
	
	@Test
	void givenOneValidPathWhenAlreadyExistsThenThrowsException() throws ApiGenException {
		
		String path = "test_path";
		
		fileSystemService.setTempDirectory(tempDirectory.toString());
		
		fileSystemService.createDirectory(path);
		
		Exception exception = assertThrows(Exception.class, () -> {
			fileSystemService.createDirectory(path);
	    });
		
		assertTrue(exception instanceof ApiGenException);
		ApiGenException apiGenException = (ApiGenException) exception;
		assertEquals(HttpStatus.CONFLICT.value(), apiGenException.getCode());
		assertEquals("The path '" + path + "' already exists", apiGenException.getMessage());
	}
	
	@Test
	void givenOneInvalidPathThenThrowsException() {
		
		String path = "/root/invalid_path";
		
		fileSystemService.setTempDirectory(tempDirectory.toString());
		
		Exception exception = assertThrows(Exception.class, () -> {
			fileSystemService.createDirectory(path);
	    });
		
		assertTrue(exception instanceof ApiGenException);
		ApiGenException apiGenException = (ApiGenException) exception;
		assertEquals(HttpStatus.BAD_REQUEST.value(), apiGenException.getCode());
		assertEquals("Invalid path '" + path + "', can't be created", apiGenException.getMessage());
	}
	
	@Test
	void givenOneNullPathThenThrowsException() {
		
		String path = null;
		
		fileSystemService.setTempDirectory(tempDirectory.toString());
		
		Exception exception = assertThrows(Exception.class, () -> {
			fileSystemService.createDirectory(path);
	    });
		
		assertTrue(exception instanceof ApiGenException);
		ApiGenException apiGenException = (ApiGenException) exception;
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiGenException.getCode());
		assertEquals("Unexpected error generating directory. null", apiGenException.getMessage());
	}
	
	@Test
	void givenAFullPathThenCreateAllDirectories() throws ApiGenException {
		
		String path = "one/two/three";
		
		fileSystemService.setTempDirectory(tempDirectory.toString());
		
		fileSystemService.createRecursiveDirectory(path);
	}
}
