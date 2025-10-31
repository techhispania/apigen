package com.tech.hispania.apigen.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.tech.hispania.apigen.app.services.TextUtilsService;
import com.tech.hispania.apigen.app.services.impl.TextUtilsServiceImpl;

public class TextUtilsServiceTest {
	
	private TextUtilsService textUtilsService = new TextUtilsServiceImpl();
	
	@Test
	void givenDifferentTextsThenCapitalizeThem() {
		
		String result1 = textUtilsService.capitalize("Test");
		String result2 = textUtilsService.capitalize("test");
		String result3 = textUtilsService.capitalize("tEsT");
		String result4 = textUtilsService.capitalize(" test");
		String result5 = textUtilsService.capitalize("tEst ");
		String result6 = textUtilsService.capitalize(" test ");
		String result7 = textUtilsService.capitalize("test test");
		
		assertEquals("Test", result1);
		assertEquals("Test", result2);
		assertEquals("Test", result3);
		assertEquals("Test", result4);
		assertEquals("Test", result5);
		assertEquals("Test", result6);
		assertEquals("Test_test", result7);
	}
}
