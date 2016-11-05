package info.tritusk.ingamechecklist.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.api.TaskManagerGetter;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

public enum IClInterModManager {
	
	INSTANCE;
	
	private static final String SIG = "(Linfo/tritusk/ingamechecklist/api/ITaskManager;)V";
	
	private Map<String, String> requesterCache = new HashMap<>();
	
	public void retriveTaskManagerRequest(ASMDataTable dataTable) {
		Set<ASMDataTable.ASMData> allManagers = dataTable.getAll(TaskManagerGetter.class.getCanonicalName());
		allManagers.forEach(data -> requesterCache.put(data.getClassName(), data.getObjectName().replace(SIG, "")));	
	}
	
	public void dispatchTaskManager() {
		for (final Map.Entry<String, String> entry : requesterCache.entrySet()) {
			try {
				Method method = Class.forName(entry.getKey()).getMethod(entry.getValue(), ITaskManager.class);
				method.invoke(null, IClProxy.manager);
			} catch (Exception e) {
				IClProxy.log.error("Something went wrong when dispatching task manager to %1$s#%2$s.", entry.getKey(), entry.getValue());
				e.printStackTrace();
			}
		}
	}

}
