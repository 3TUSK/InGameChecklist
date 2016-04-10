package info.tritusk.customchecklist.common.command;

import java.util.Arrays;
import java.util.Iterator;

import info.tritusk.customchecklist.common.task.TaskEntry;
import info.tritusk.customchecklist.common.task.TaskEntryLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandTask extends CommandBase {

	@Override
	public String getCommandName() {
		return "customchecklist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/customchecklist <add|remove|show> [task]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2)
			throw new CommandException("Insufficient parameters. Please double check.");
		String taskName = args[1];
		switch (args[0]) {
		case("add"): {
			String[] taskDesc = Arrays.copyOfRange(args, 2, args.length);
			this.execute(sender, taskName, taskDesc, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				if (TaskEntryLoader.globalEntryList.contains(aTaskName)) {
						aSender.addChatMessage(new ChatComponentText("Task with same name is disallowed!"));
						return;
				}
				TaskEntryLoader.globalEntryList.add(new TaskEntry(taskName, this.stringArrayToString(aTaskDesc)));
			});
			break;
		}
		case("remove"): {
			this.execute(sender, taskName, null, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				Iterator<TaskEntry> iterator = TaskEntryLoader.globalEntryList.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getName().equals(aTaskName))
						iterator.remove();
				}
			});
			break;
		}
		case("show"): {
			this.execute(sender, taskName, null, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				String info = "";
				Iterator<TaskEntry> iterator = TaskEntryLoader.globalEntryList.iterator();
				while (iterator.hasNext()) {
					TaskEntry yetAnotherTask = iterator.next();
					if (yetAnotherTask.getName().equals(aTaskName))
						info = yetAnotherTask.getDescription();
				}
				aSender.addChatMessage(new ChatComponentText(info));
			});
			break;
		}
		case("update"): {
			String[] taskDesc = Arrays.copyOfRange(args, 2, args.length);
			this.execute(sender, taskName, taskDesc, (ICommandSender aSender, String aTaskName, String[] aTaskDesc) -> {
				if (TaskEntryLoader.globalEntryList.contains(aTaskDesc)) {
					Iterator<TaskEntry> iterator = TaskEntryLoader.globalEntryList.iterator();
					while (iterator.hasNext()) {
						TaskEntry yetAnotherTask = iterator.next();
						if (yetAnotherTask.getName().equals(aTaskName)) {
							iterator.remove();
							TaskEntryLoader.globalEntryList.add(new TaskEntry(taskName, this.stringArrayToString(aTaskDesc)));
							break;
						}
					}
				} else {
					aSender.addChatMessage(new ChatComponentText("Failed to find requested task entry, making new one instead."));
					TaskEntryLoader.globalEntryList.add(new TaskEntry(taskName, this.stringArrayToString(aTaskDesc)));
				}
			});
		}
		default:
			throw new CommandException("Invalid parameter, please double check.");
		}
	}
	
	private void execute(ICommandSender sender, String taskName, String[] taskDesc, ITaskCommandLogic logic) throws CommandException {
		logic.execute(sender, taskName, taskDesc);
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
