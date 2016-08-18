package info.tritusk.ingamechecklist.api;

import java.util.Map;

public interface ITaskTranslatable extends ITask {
	
	String getTranslation(String langCode);
	
	ITaskTranslatable setTranslation(String langCode, String text);
	
	Map<String, String> getAllTranslations();

}
