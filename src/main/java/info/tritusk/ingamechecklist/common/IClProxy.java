package info.tritusk.ingamechecklist.common;

import java.io.File;
import java.io.FileInputStream;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Logger;

import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.common.command.CommandTask;
import info.tritusk.ingamechecklist.common.task.TaskEntryManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class IClProxy {
	
	@Nonnull
	public static Logger log;

	public static ITaskManager manager = new TaskEntryManager();
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		
		IClInterModManager.INSTANCE.retriveTaskManagerRequest(event.getAsmData());
		
		File mainDir = new File(event.getModConfigurationDirectory(), "InGameChecklist");
		if (!mainDir.exists() || !mainDir.isDirectory())
			mainDir.mkdir();
		IClConfig.initConfig(new File(mainDir, "InGameChecklist.cfg"));
		IClConfig.initLocalTasks(new File(mainDir, "LocalTasks.xml"));
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTask());
		try {
			manager.init();
			manager.loadFrom(new FileInputStream(IClConfig.localTasks));
			log.info("Loaded local checklist.");
		} catch (Exception e) {
			log.error("Something went extremely wrong...");
			e.printStackTrace();
		}
	}
	
	public void onServerStopping(FMLServerStoppingEvent event) {
		manager.saveTo(IClConfig.localTasks);
	}

}
