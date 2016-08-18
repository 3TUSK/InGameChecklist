package info.tritusk.ingamechecklist.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.api.TaskManager;
import info.tritusk.ingamechecklist.common.command.CommandTask;
import info.tritusk.ingamechecklist.common.config.ConfigMain;
import info.tritusk.ingamechecklist.common.task.TaskEntryLoader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class IClProxy {
	
	public static Logger log;
	
	@TaskManager(name = "InGameChecklist-Local")
	public static ITaskManager localTaskManager = new TaskEntryLoader();
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		ASMDataTable asmData = event.getAsmData();
		Set<ASMDataTable.ASMData> allManagers = asmData.getAll(TaskManager.class.getCanonicalName());
		for (ASMDataTable.ASMData data : allManagers) {
			data.getObjectName();
		}
		File mainDir = new File(event.getModConfigurationDirectory(), "CustomChecklist");
		if (!mainDir.exists() || !mainDir.isDirectory())
			mainDir.mkdir();
		ConfigMain.initConfig(new File(mainDir, "CustomChecklist.cfg"));
		ConfigMain.initGlobalTasks(new File(mainDir, "GlobalTasks.xml"));
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTask());
		try {
			localTaskManager.loadFrom(new FileInputStream(ConfigMain.globalTasks));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		log.info("Loaded local checklist.");
	}
	
	public void onServerStopping(FMLServerStoppingEvent event) {
		localTaskManager.saveTo(ConfigMain.globalTasks);
	}

}
