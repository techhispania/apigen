package com.tech.hispania.apigen.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.FileSystemService;
import com.tech.hispania.apigen.app.services.FileTemplateService;
import com.tech.hispania.apigen.app.services.impl.FileSystemServiceImpl;
import com.tech.hispania.apigen.app.services.impl.FileTemplateServiceImpl;

public class FileTemplateServiceTest {

	@TempDir
	private Path tempDirectory;
	
	private FileTemplateService fileTemplateService;
	
	private FileSystemService fileSystemService;
	
	@Test
	void givenValidTemplateAndValidFileThenCopyContentInFile() throws ApiGenException {
		
		String template = "main";
		String path = "test/file.txt";
	
		fileSystemService = new FileSystemServiceImpl();
		fileSystemService.setTempDirectory(tempDirectory.toString());
		fileSystemService.createDirectory("test");
		fileSystemService.createFile("test", "file.txt");
		
		fileTemplateService = new FileTemplateServiceImpl();
		fileTemplateService.copyTemplateInFile(template, tempDirectory.toString() + "/" + path);
	}
	
	@Test
	void givenInvalidTemplateAndValidFileThenThrowException() throws ApiGenException {
		
		String template = "fake";
		String path = "test/file.txt";
	
		fileSystemService = new FileSystemServiceImpl();
		fileSystemService.setTempDirectory(tempDirectory.toString());
		fileSystemService.createDirectory("test");
		fileSystemService.createFile("test", "file.txt");
		
		fileTemplateService = new FileTemplateServiceImpl();
		Exception exception = assertThrows(ApiGenException.class, () -> {
			fileTemplateService.copyTemplateInFile(template, tempDirectory.toString() + "/" + path);	
		});
		
		assertTrue(exception instanceof ApiGenException);
		ApiGenException apiGenException = (ApiGenException) exception;
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiGenException.getCode());
		assertTrue(apiGenException.getMessage().contains("Unexpected error copying from template 'fake.template' into file"));
	}
}
