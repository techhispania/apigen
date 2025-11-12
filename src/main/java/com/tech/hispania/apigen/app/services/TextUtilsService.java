package com.tech.hispania.apigen.app.services;

public interface TextUtilsService {

	/**
	 * Method that convert a text in lower case and put 
	 * the first letter in upper case
	 * 
	 * @param text The text to be converted
	 * @return The text converted
	 */
	String capitalize(String text);
	
	/**
	 * Method that create a table name from an entity name.
	 * The table name starts in lowercase, and replace the capital letters with
	 * underscore. It also convert the entity name to plural.<br>
	 * For example:<br>
	 * User = users<br>
	 * Catalog = catalogs<br>
	 * Company = companies<br>
	 * TestUser = test_users<br>
	 * 
	 * @param text The entity name
	 * @return The table name
	 */
	String buildEntityTableName(String text);
}
