package info.tritusk.ingamechecklist.api;

import java.io.InputStream;
import java.util.Collection;

public interface ITaskManager {
	
	boolean init();
	
	Collection<ITask> loadFrom(InputStream input);
	
	void save(Collection<ITask> tasks);

}
