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
	
	@Override
	public String buildEntityTableName(String text) {
		text = text.trim();
		text = new StringBuilder(text.substring(0, 1).toLowerCase()).append(text.substring(1)).toString();
		
		boolean isLowerCasePresent = false;
		for (int i = 1; i < text.length(); i++) {
			if (Character.isLowerCase(text.charAt(i))) {
				isLowerCasePresent = true;
				break;
			}
		}
		
		if (!isLowerCasePresent) {
			text = text.toLowerCase();
		}
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (Character.isUpperCase(character)) {
				result.append("_").append(Character.toString(character).toLowerCase());
			} else {
				result.append(Character.toString(character));
			}
		}
		if (result.substring(result.length() - 1).equalsIgnoreCase("y")) {
			result = new StringBuilder(result.substring(0, result.length() - 1)).append("ies");
		} else {
			result = new StringBuilder(result.substring(0, result.length())).append("s");
		}
		return result.toString();
	}
}
