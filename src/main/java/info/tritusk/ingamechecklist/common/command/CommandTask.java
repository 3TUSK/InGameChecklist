package info.tritusk.ingamechecklist.common.command;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.client.handler.HUDHandler;
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
		this.addSubcommand("add", "/checklist add [task] [description]", (aSender, aTaskName, aTaskDesc) -> IClProxy.manager.addTask(new TaskEntry(aTaskName, buildString(aTaskDesc, 0))));
		this.addSubcommand("remove", "/checklist remove [task]", (aSender, aTaskName, aTaskDesc) -> {
			ITask task = IClProxy.manager.getByName(aTaskName);
			if (task != null)
				IClProxy.manager.removeTask(task);
		});
		this.addSubcommand("show", "/checklist show [task]", (aSender, aTaskName, aTaskDesc) -> HUDHandler.safeSetPinnedTask(aTaskName));
		this.addSubcommand("update", "/checklist update [task] [description]", (aSender, aTaskName, aTaskDesc) -> {
			ITask task = IClProxy.manager.getByName(aTaskName);
			if (task != null)
				IClProxy.manager.removeTask(task);
			IClProxy.manager.addTask(new TaskEntry(aTaskName, buildString(aTaskDesc, 0)));
		});
		this.addSubcommand("help", "/checklist help - show help message", (aSender, aTaskName, aTaskDesc) -> sendTextMessage(aSender, this.getCommandUsage(aSender)));
	}
	
	@Override
	public String getCommandName() {
		return "checklist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/checklist <help|add|remove|show|update>";
	}
	
	private static void sendTextMessage(ICommandSender sender, String message) {
		sender.addChatMessage(new TextComponentString(message));
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
				if (args.length == 0 || args[0].equals("help")) {
					sendTextMessage(sender, getCommandUsage(sender));
					return;
				}
				String[] subArgs;
				if (args.length > 1) {
					subArgs = new String[args.length - 1];
					System.arraycopy(args, 1, subArgs, 0, args.length - 1);
				} else {
					subArgs = new String[0];
				}	
				logic.execute(sender, args[0], subArgs);
			}
		});
	}
	
	public static interface ITaskCommandLogic {
		
		void execute(ICommandSender sender, String taskName, String[] taskDesc) throws CommandException;

	}

}
