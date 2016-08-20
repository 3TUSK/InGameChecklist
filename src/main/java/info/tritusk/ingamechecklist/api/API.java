package info.tritusk.ingamechecklist.api;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum API {
	
	INSTANCE;
	
	public boolean register(@Nonnull String name, @Nonnull ITaskManager manager) {
		if (registry.containsKey(name)) {
			registry.putIfAbsent(name, manager);
			return true;
		} else {
			return false;
		}
	}
	
	@Nullable
	public ITaskManager get(@Nonnull String name) {
		if (registry.containsKey(name))
			return registry.get(name);
		else
			return null;
	}
	
	private final Map<String, ITaskManager> registry = new LinkedHashMap<>();

}
