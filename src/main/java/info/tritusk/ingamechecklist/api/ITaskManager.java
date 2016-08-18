package info.tritusk.ingamechecklist.api;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

public interface ITaskManager {
	
	boolean init();
	
	boolean isInitialized();
	
	boolean loadFrom(final InputStream input);
	
	boolean saveTo(final File file);
	
	Collection<ITask> getTasks();

}
