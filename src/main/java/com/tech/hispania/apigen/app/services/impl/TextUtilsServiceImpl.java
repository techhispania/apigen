package com.tech.hispania.apigen.app.services.impl;

import org.springframework.stereotype.Service;

import com.tech.hispania.apigen.app.services.TextUtilsService;

@Service
public class TextUtilsServiceImpl implements TextUtilsService {

	@Override
	public String capitalize(String text) {
		String result = text.trim();
		result = new StringBuilder(result.substring(0, 1).toUpperCase()).append(result.substring(1)).toString();
		result = result.replace(" ", "_");
		
		return result;
	}
}
