package info.tritusk.ingamechecklist.common;

import net.minecraftforge.common.config.Config;

@Config(modid = "ingame_checklist", name = "InGameChecklist", type = Config.Type.INSTANCE)
public final class IClConfig {
	
	@Config.Comment("Set to true to enable remote tasks retrieval")
	@Config.LangKey("ingame_checklist.config.remoteTasksRetrive")
	public static boolean enableRemoteTasksRetrive = false;
		
	@Config.Comment("The url that points to remote tasks file. Has no effect when remote retrieval is disabled")
	@Config.LangKey("ingame_checklist.config.remoteTasksURL")
	public static String remoteTaskURL = "";
		
	@Config.Comment("Enable debug mode to see verbose output")
	@Config.LangKey("ingame_checklist.config.debug")
	public static boolean debug = false;

}
