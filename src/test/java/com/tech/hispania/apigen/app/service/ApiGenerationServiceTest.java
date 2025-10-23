package com.tech.hispania.apigen.app.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.ApiGenerationService;
import com.tech.hispania.apigen.app.services.FileSystemService;
import com.tech.hispania.apigen.app.services.impl.ApiGenerationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ApiGenerationServiceTest {

	@Mock
	private FileSystemService fileSystemService;
	
	@InjectMocks
	private ApiGenerationService apiGenerationService = new ApiGenerationServiceImpl();
	
	@Test
	void whenGenerateIsExecutedThenFinishOk() throws ApiGenException {
	
		doNothing().when(fileSystemService).setTempDirectory(anyString());
		doNothing().when(fileSystemService).createDirectory(anyString());

		apiGenerationService.generate();
		
		verify(fileSystemService, times(1)).setTempDirectory(anyString());
		verify(fileSystemService, times(5)).createDirectory(anyString());
	}
}
