package com.tech.hispania.apigen.app.service;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tech.hispania.apigen.app.exceptions.ApiGenException;
import com.tech.hispania.apigen.app.services.ApiGenerationService;
import com.tech.hispania.apigen.app.services.FileSystemService;
import com.tech.hispania.apigen.app.services.FileTemplateService;
import com.tech.hispania.apigen.app.services.impl.ApiGenerationServiceImpl;
import com.tech.hispania.apigen.domain.model.ApiGenEntity;
import com.tech.hispania.apigen.domain.model.ApiGenProperty;
import com.tech.hispania.apigen.domain.model.PropertyType;

@ExtendWith(MockitoExtension.class)
public class ApiGenerationServiceTest {

	@Mock
	private FileSystemService fileSystemService;
	
	@Mock
	private FileTemplateService fileTemplateService;
	
	@InjectMocks
	private ApiGenerationService apiGenerationService = new ApiGenerationServiceImpl();
	
	@Test
	void whenGenerateIsExecutedThenFinishOk() throws ApiGenException {

		String apiName = "Planes";
		Set<ApiGenEntity> entities = new HashSet<>();
		
		ApiGenProperty modelProp = new ApiGenProperty("model", PropertyType.TEXT);
		ApiGenProperty companyProp = new ApiGenProperty("company", PropertyType.TEXT);
		
		List<ApiGenProperty> planeEntityProperties = List.of(modelProp, companyProp);
		ApiGenEntity planeEntity = new ApiGenEntity("plane", planeEntityProperties);
		
		entities.add(planeEntity);
		
		doNothing().when(fileSystemService).setTempDirectory(anyString());
		doNothing().when(fileSystemService).createRecursiveDirectory(anyString());
		doNothing().when(fileSystemService).removeDirectory(anyString());
		doNothing().when(fileTemplateService).copyTemplateInFile(anyString(), anyString());
		doNothing().when(fileTemplateService).replacePlaceholders(anyString(), anyMap());

		apiGenerationService.generate(apiName, entities);
		
		verify(fileSystemService, times(1)).setTempDirectory(anyString());
		verify(fileSystemService, times(2)).createRecursiveDirectory(anyString());
		verify(fileSystemService, times(1)).removeDirectory(anyString());
		verify(fileTemplateService, times(1)).copyTemplateInFile(anyString(), anyString());
		verify(fileTemplateService, times(1)).replacePlaceholders(anyString(), anyMap());
	}
}
