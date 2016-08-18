package info.tritusk.ingamechecklist.api;

import java.io.InputStream;
import java.util.Collection;

public interface ITaskManager {
	
	boolean init();
	
	boolean loadFrom(InputStream input);
	
	boolean save(Collection<ITask> tasks);
	
	Collection<ITask> getAll();

}
