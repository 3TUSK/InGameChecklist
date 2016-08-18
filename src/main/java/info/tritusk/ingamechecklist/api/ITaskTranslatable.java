package info.tritusk.ingamechecklist.api;

public interface ITaskTranslatable extends ITask {
	
	String getTranslation(String langCode);
	
	ITaskTranslatable setTranslation(String langCode, String text);

}
