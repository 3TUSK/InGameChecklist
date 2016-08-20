package info.tritusk.ingamechecklist.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraftforge.common.config.Configuration;

public final class IClConfig {
	
	public static enum MergePolicy {
		MERGE, 
		OVERRIDE, 
		DISABLE
	}
	
	public static MergePolicy policy;
	
	public static File localTasks;
	public static File remoteTasksDump;
	public static InputStream remoteTasks;
	
	public static void initConfig(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		String urlString = config.getString("RemoteTaskList", "Remote", "", "Fill this with a url to let InGameChecklist download task file from other server.");
		
		policy = MergePolicy.valueOf(config.getString("TaskOverridePolicy", "Remote", "DISABLE", "Decide how to deal with remote tasks. Merge = merge into local tasks; Override = Only remote tasks will present; Disable = Only local tasks will present.").toUpperCase());
		if (policy == null)
			policy = MergePolicy.DISABLE;
		
		if (urlString != null && !urlString.equals("")) {
			new Thread(() -> {
				try {
					remoteTasks = new URL(urlString).openConnection().getInputStream();
					remoteTasksDump = new File(file.getParentFile(), urlString);
				} catch (MalformedURLException e) {
					IClProxy.log.error("The input url is wrongly formatted!");
				} catch (IOException e) {
					IClProxy.log.error("Fail to load tasks file from remote server.");
					e.printStackTrace();
				}
			}).start();
		}
		
		if (config.hasChanged())
			config.save();
	}
	
	public static void initLocalTasks(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		localTasks = file;
	}

}
