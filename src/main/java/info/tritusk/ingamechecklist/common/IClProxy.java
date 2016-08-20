package info.tritusk.ingamechecklist.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import info.tritusk.ingamechecklist.api.API;
import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.api.TaskManager;
import info.tritusk.ingamechecklist.common.command.CommandTask;
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
	
	@TaskManager(name = "InGameChecklist-Remote")
	public static ITaskManager remoteTaskManager = new TaskEntryLoader();
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		
		ASMDataTable asmData = event.getAsmData();
		Set<ASMDataTable.ASMData> allManagers = asmData.getAll(TaskManager.class.getCanonicalName());
		for (ASMDataTable.ASMData data : allManagers) {
			try {
				ITaskManager manager = (ITaskManager) Class.forName(data.getClassName()).getDeclaredField(data.getObjectName()).get(null);
				API.INSTANCE.register(String.valueOf(data.getAnnotationInfo().get("name")), manager);
				log.info("Loaded manager: {}", String.valueOf(data.getAnnotationInfo().get("name")));
			} catch (Exception e) {
				log.error("Something went wrong when loading a task manager.");
				e.printStackTrace();
			}
		}
		
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
			localTaskManager.init();
			localTaskManager.loadFrom(new FileInputStream(IClConfig.localTasks));
		} catch (Exception e) {
			log.error("Something went extremely wrong...");
			e.printStackTrace();
		}
		log.info("Loaded local checklist.");
	}
	
	public void onServerStopping(FMLServerStoppingEvent event) {
		localTaskManager.saveTo(IClConfig.localTasks);
	}

}
