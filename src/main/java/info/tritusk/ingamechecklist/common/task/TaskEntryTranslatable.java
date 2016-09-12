package info.tritusk.ingamechecklist.common.task;

import java.util.HashMap;
import java.util.Map;

import info.tritusk.ingamechecklist.api.ITaskTranslatable;

public class TaskEntryTranslatable extends TaskEntry implements ITaskTranslatable {
	
	public TaskEntryTranslatable(String name) {
		super(name);
	}
	
	public TaskEntryTranslatable(String name, String description) {
		super(name, description);
	}
	
	public Map<String, String> translation = new HashMap<String, String>();
	
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
			throw new IllegalArgumentException("No, empty language code is not allowed");
		
		if ("en_US".equals(langCode))
			throw new IllegalArgumentException("Sorry but American English is the language used for fallback!");
		
		this.translation.putIfAbsent(langCode, translation);
		return this;
	}
	
	@Override
	public Map<String, String> getAllTranslations() {
		return new HashMap<>(translation);
	}

}
