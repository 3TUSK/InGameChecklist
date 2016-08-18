package info.tritusk.ingamechecklist.common.command;

import java.util.Arrays;
import java.util.Iterator;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.task.TaskEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandTask extends CommandBase {

	@Override
	public String getCommandName() {
		return "checklist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/checklist <help|add|remove|show|update> [task] [task description]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args == null || args.length == 0) {
			this.showHelpMessage(sender);
			return;
		}
		String taskName = args[1];
		switch (args[0]) {
		case("add"): {
			String[] taskDesc = Arrays.copyOfRange(args, 2, args.length);
			this.execute0(sender, taskName, taskDesc, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				if (IClProxy.localTaskManager.getAll().contains(aTaskName)) {
						this.sendTextMessage(aSender, "Task with same name is disallowed!");
						return;
				}
				IClProxy.localTaskManager.getAll().add(new TaskEntry(taskName, this.stringArrayToString(aTaskDesc)));
			});
			break;
		}
		case("remove"): {
			this.execute0(sender, taskName, null, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				Iterator<ITask> iterator = IClProxy.localTaskManager.getAll().iterator();
				while (iterator.hasNext()) {
					if (iterator.next().name().equals(aTaskName))
						iterator.remove();
				}
			});
			break;
		}
		case("show"): {
			this.execute0(sender, taskName, null, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				String info = "";
				Iterator<ITask> iterator = IClProxy.localTaskManager.getAll().iterator();
				while (iterator.hasNext()) {
					ITask yetAnotherTask = iterator.next();
					if (yetAnotherTask.name().equals(aTaskName))
						info = yetAnotherTask.description();
				}
				this.sendTextMessage(aSender, info);
			});
			break;
		}
		case("update"): {
			String[] taskDesc = Arrays.copyOfRange(args, 2, args.length);
			this.execute0(sender, taskName, taskDesc, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				if (IClProxy.localTaskManager.getAll().contains(aTaskDesc)) {
					Iterator<ITask> iterator = IClProxy.localTaskManager.getAll().iterator();
					while (iterator.hasNext()) {
						ITask yetAnotherTask = iterator.next();
						if (yetAnotherTask.name().equals(aTaskName)) {
							iterator.remove();
							IClProxy.localTaskManager.getAll().add(new TaskEntry(taskName, this.stringArrayToString(aTaskDesc)));
							break;
						}
					}
				} else {
					this.sendTextMessage(aSender, "Failed to find requested task entry.");
				}
			});
			break;
		}
		case("help"): {
			this.showHelpMessage(sender);
			break;
		}
		default:
			throw new CommandException("Invalid parameter, please double check.");
		}
	}
	
	private void execute0(ICommandSender sender, String taskName, String[] taskDesc, ITaskCommandLogic logic) throws CommandException {
		logic.execute(sender, taskName, taskDesc);
	}
	
	private void showHelpMessage(ICommandSender sender) {
		this.sendTextMessage(sender, this.getCommandUsage(sender));
		this.sendTextMessage(sender, "    " + "help: Show this help");
		this.sendTextMessage(sender, "    " + "add: Add a new task to checklist");
		this.sendTextMessage(sender, "    " + "remove: Remove an existed task from checklist");
		this.sendTextMessage(sender, "    " + "show: Show description of a task");
		this.sendTextMessage(sender, "    " + "update: Update the description for an existed task");
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
	
	public static interface ITaskCommandLogic {
		
		void execute(ICommandSender sender, String taskName, String[] taskDesc) throws CommandException;

	}

}
