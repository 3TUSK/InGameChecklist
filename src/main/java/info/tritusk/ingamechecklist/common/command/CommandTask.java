package info.tritusk.ingamechecklist.common.command;

import java.util.Iterator;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.task.TaskEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTask extends CommandTreeBase {

	public CommandTask() {
		this.addSubcommand("add", "", (aSender, aTaskName, aTaskDesc) -> {
			if (IClProxy.manager.getTasks().contains(aTaskName)) {
				this.sendTextMessage(aSender, "Task with same name is disallowed!");
				return;
			}
			IClProxy.manager.getTasks().add(new TaskEntry(aTaskName, this.stringArrayToString(aTaskDesc)));
		});
		this.addSubcommand("remove", "", (aSender, aTaskName, aTaskDesc) -> {
			Iterator<ITask> iterator = IClProxy.manager.getTasks().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().name().equals(aTaskName))
					iterator.remove();
			}
		});
		this.addSubcommand("show", "", (aSender, aTaskName, aTaskDesc) -> {
			String info = "";
			Iterator<ITask> iterator = IClProxy.manager.getTasks().iterator();
			while (iterator.hasNext()) {
				ITask yetAnotherTask = iterator.next();
				if (yetAnotherTask.name().equals(aTaskName))
					info = yetAnotherTask.description();
			}
			this.sendTextMessage(aSender, info);
		});
		this.addSubcommand("update", "", (aSender, aTaskName, aTaskDesc) -> {
			if (IClProxy.manager.getTasks().contains(aTaskDesc)) {
				Iterator<ITask> iterator = IClProxy.manager.getTasks().iterator();
				while (iterator.hasNext()) {
					ITask yetAnotherTask = iterator.next();
					if (yetAnotherTask.name().equals(aTaskName)) {
						iterator.remove();
						IClProxy.manager.getTasks().add(new TaskEntry(aTaskName, this.stringArrayToString(aTaskDesc)));
						break;
					}
				}
			} else {
				this.sendTextMessage(aSender, "Failed to find requested task entry.");
			}
		});
		this.addSubcommand("help", "", (aSender, aTaskName, aTaskDesc) -> {
			this.sendTextMessage(aSender, this.getCommandUsage(aSender));
			this.sendTextMessage(aSender, "    " + "help: Show this help");
			this.sendTextMessage(aSender, "    " + "add: Add a new task to checklist");
			this.sendTextMessage(aSender, "    " + "remove: Remove an existed task from checklist");
			this.sendTextMessage(aSender, "    " + "show: Show description of a task");
			this.sendTextMessage(aSender, "    " + "update: Update the description for an existed task");
		});
	}
	
	@Override
	public String getCommandName() {
		return "checklist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/checklist <help|add|remove|show|update> [task] [task description]";
	}
	
	private void sendTextMessage(ICommandSender sender, String message) {
		sender.addChatMessage(new TextComponentString(message));
	}
	
	private String stringArrayToString(String[] array) {
		StringBuilder builder = new StringBuilder();
		for (String str : array) {
			builder.append(str).append(' ');
		}
		return builder.toString();
	}
	
	public void addSubcommand(final String name, final String usage, final ITaskCommandLogic logic) {
		this.addSubcommand(new CommandBase() {
			@Override
			public String getCommandName() {
				return name;
			}
			@Override
			public String getCommandUsage(ICommandSender sender) {
				return usage;
			}
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				final int l = args.length;
				if (l < 1)
					throw new CommandException("Insufficent arguments");
				String[] subArgs = new String[args.length - 1];
				System.arraycopy(args, 1, subArgs, 0, args.length);
				logic.execute(sender, args[0], subArgs);
			}
		});
	}
	
	public static interface ITaskCommandLogic {
		
		void execute(ICommandSender sender, String taskName, String[] taskDesc) throws CommandException;

	}

}
