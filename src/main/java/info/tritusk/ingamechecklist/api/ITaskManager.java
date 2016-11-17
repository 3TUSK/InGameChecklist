package info.tritusk.ingamechecklist.api;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import javax.annotation.Nullable;
/**
 * A <code>TaskManager</code> is an object that capable to (de)serialize 
 * tasks from/into {@link InputStream stream}/{@link File file}, as well 
 * as add/get/modify {@link ITask tasks}.
 * @author 3TUSK
 * @since 0.1.0
 */
public interface ITaskManager {
	
	/**
	 * Call for initialize the manager instance.
	 * @return True if initialization is successful, false for otherwise.
	 */
	boolean init();
	
	/**
	 * Convenient method for availability checking. 
	 * @return True if the manager is currently available to handle {@link ITask tasks}, false for otherwise.
	 */
	boolean isInitialized();
	
	/**
	 * Deserialize tasks from given input stream.
	 * @param input Serialized tasks, may not be null
	 * @return True if load tasks successfully, false for otherwise
	 */
	boolean loadFrom(final InputStream input);
	
	/**
	 * Serialize all tasks into given {@link File file} instance.
	 * @param file Destination of tasks.
	 * @return True if tasks are successfully saved, false for otherwise
	 */
	boolean saveTo(final File file);
	
	/**
	 * A convenient method for iterating all tasks.
	 * @return An immutable collection that contains all {@link ITask tasks}.
	 */
	Collection<ITask> getTasks();
	
	/**
	 * Add a {@link ITask tasks} instance to the manager instance.
	 * @param task Task to add
	 * @return True if the process is successful
	 */
	boolean addTask(ITask task);
	
	/**
	 * Remove a {@link ITask tasks} instance from the manager instance.
	 * @param task Task to remove
	 * @return True if the process is successful
	 */
	boolean removeTask(ITask task);
	
	/**
	 * Get a task from this manager based on given name.
	 * @param name The name of request task
	 * @return A {@link ITask task} with its {@link ITask#name() name} of given name, or null if such task does not exist
	 */
	@Nullable
	ITask getByName(String name);

}
