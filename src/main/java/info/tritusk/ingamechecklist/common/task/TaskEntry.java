package info.tritusk.ingamechecklist.common.task;

import info.tritusk.ingamechecklist.api.ITask;

public class TaskEntry implements ITask {
	
	protected final String name;
	protected final String description;
	
	public TaskEntry(String name) {
		this.name = name;
		this.description = "";
	}
	
	public TaskEntry(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@Override
	public String name() {
		return this.name;
	}
	
	@Override
	public String description() {
		return this.description;
	}

}
