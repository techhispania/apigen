package com.tech.hispania.apigen.ui.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {

	private static final Logger logger = LogManager.getLogger(WebController.class);
	
	@GetMapping
	public String index(Model model) {
		logger.debug("Index request received");
		
		model.addAttribute("title", "ApiGen 1.0");
		
		return "index";
	}
}
