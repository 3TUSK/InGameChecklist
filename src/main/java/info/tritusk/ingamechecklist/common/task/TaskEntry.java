package info.tritusk.ingamechecklist.common.task;

import java.util.HashMap;
import java.util.Map;

import info.tritusk.ingamechecklist.api.ITaskTranslatable;

public class TaskEntry implements ITaskTranslatable {
	
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
	
	@Override
	public String name() {
		return this.name;
	}
	
	@Override
	public String description() {
		return this.description;
	}
	
	@Override
	public String getTranslation(String langCode) {
		try {
			return translation.get(langCode);
		} catch (Exception e) {
			return this.description();
		}
	}
	
	@Override
	public ITaskTranslatable setTranslation(String langCode, String translation) {
		if ("".equals(this.description))
			throw new IllegalArgumentException("No, String with length of 0 is not allowed");
		
		if ("en_US".equals(langCode))
			throw new IllegalArgumentException("Sorry but American English is the language used for fallback!");
		
		this.translation.put(langCode, translation);
		return this;
	}
	
	@Override
	public Map<String, String> getAllTranslations() {
		return translation;
	}

}
