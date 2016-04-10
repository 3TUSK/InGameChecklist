package info.tritusk.customchecklist.common.task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TaskEntry {
	
	private final String name;
	private final String description;
	
	public Map<String, String> translation = new HashMap<String, String>();
	
	public TaskEntry(String name) {
		this.name = name;
		this.description = "";
	}
	
	public TaskEntry(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getDescription(String langCode) {
		if ("".equals(description))
			return "";
		
		if ("en_US".equals(langCode))
			return this.getDescription();
		else
			try {
				return translation.get(langCode);
			} catch (Exception e) {
				return this.getDescription();
			}
	}
	
	public TaskEntry setTranslation(String langCode, String translation) {
		if ("".equals(this.description))
			return this;
		
		if ("en_US".equals(langCode))
			throw new IllegalArgumentException("Sorry but American English is the language used for fallback!");
		if (!Pattern.matches("[a-z]{2}_{1}[A-Z]{2}", langCode))
			throw new IllegalArgumentException("Invalid language code detected!!!");
		this.translation.put(langCode, translation);
		return this;
	}

}
