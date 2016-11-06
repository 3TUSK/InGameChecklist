package info.tritusk.ingamechecklist.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.minecraftforge.common.config.Config;

public final class IClConfig {
	
	public static enum MergePolicy {
		MERGE, 
		OVERRIDE, 
		DISABLE
	}
	
	@Config(modid = "ingame_checklist", name = "InGameChecklist", type = Config.Type.INSTANCE)
	public static class Options {
		@Config.Comment("Set to true to enable remote tasks retrieval")
		@Config.LangKey("ingame_checklist.config.remoteTasksRetrive")
		public static boolean enableRemoteTasksRetrive = false;
		
		@Config.Comment("The url that points to remote tasks file. Has no effect when remote retrieval is disabled")
		@Config.LangKey("ingame_checklist.config.remoteTasksURL")
		public static String remoteTaskURL = "";
		
		@Config.Comment("Merging policy being applied during retrieval. Has no effect when remote retrieval is disabled")
		@Config.LangKey("ingame_checklist.config.mergePolicy")
		public static String mergePolicy = "DISABLE";
		
		@Config.Comment("Enable debug mode to see verbose output")
		@Config.LangKey("ingame_checklist.config.debug")
		public static boolean debug = false;
	}
	
	static MergePolicy policy;
	
	static File remoteTasksDump;
	static InputStream remoteTasks;
	
	public static void initConfig(File file) {
		policy = MergePolicy.valueOf(Options.mergePolicy);
		if (policy == null)
			policy = MergePolicy.DISABLE;
		
		if (Options.remoteTaskURL != null && !Options.remoteTaskURL.equals("")) {
			new Thread(() -> {
				try {
					URLConnection connection = new URL(Options.remoteTaskURL).openConnection();
					connection.setReadTimeout(10000);
					remoteTasks = connection.getInputStream();
					remoteTasksDump = new File(file.getParentFile(), Options.remoteTaskURL);
					FileOutputStream out = new FileOutputStream(remoteTasksDump);
					out.flush();
					out.close();
				} catch (MalformedURLException e) {
					IClProxy.log.error("The input url is wrongly formatted!");
				} catch (IOException e) {
					IClProxy.log.error("Fail to load tasks file from remote server.");
					e.printStackTrace();
				}
			}).start();
		}
	}

}
