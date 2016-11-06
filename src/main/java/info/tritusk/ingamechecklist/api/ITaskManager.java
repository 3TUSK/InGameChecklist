package info.tritusk.ingamechecklist.api;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import javax.annotation.Nullable;

public interface ITaskManager {
	
	boolean init();
	
	boolean isInitialized();
	
	boolean loadFrom(final InputStream input);
	
	boolean saveTo(final File file);
	
	Collection<ITask> getTasks();
	
	boolean addTask(ITask task);
	
	boolean removeTask(ITask task);
	
	@Nullable
	ITask getByName(String name);

}
