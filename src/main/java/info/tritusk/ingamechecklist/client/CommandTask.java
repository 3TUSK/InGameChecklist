package info.tritusk.ingamechecklist.client;

import info.tritusk.ingamechecklist.client.handler.HUDHandler;
import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.task.TaskEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTask extends CommandTreeBase {

	public CommandTask() {
		this.addSubcommand("add", "/checklist add [task] [description]", (aSender, args) -> IClProxy.manager.addTask(new TaskEntry(args[0], buildString(args, 1))));
		this.addSubcommand("remove", "/checklist remove [task]", (aSender, args) -> IClProxy.manager.removeTaskByName(args[0]));
		this.addSubcommand("show", "/checklist show [task]", (aSender, args) -> HUDHandler.safeSetPinnedTask(args[0]));
		this.addSubcommand("update", "/checklist update [task] [description]", (aSender, args) -> {
			IClProxy.manager.removeTaskByName(args[0]);
			IClProxy.manager.addTask(new TaskEntry(args[0], buildString(args, 1)));
		});
		this.addSubcommand("help", "/checklist help - show help message", (aSender, args) -> sendTextMessage(aSender, this.getUsage(aSender)));
		this.addSubcommand("refresh", "/checklist refresh - reload tasks", (aSender, args) -> {
			try {
				IClProxy.manager.loadFrom(new java.io.FileInputStream(new java.io.File(DimensionManager.getCurrentSaveRootDirectory(), "InGameChecklist.xml")));
			} catch (Exception e) {
				//Impossible
			}
		});
	}
	
	@Override
	public String getName() {
		return "checklist";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/checklist <help|add|remove|show|update>";
	}
	
	private static void sendTextMessage(ICommandSender sender, String message) {
		sender.sendMessage(new TextComponentString(message));
	}
	
	public void addSubcommand(final String name, final String usage, final ITaskCommandLogic logic) {
		this.addSubcommand(new CommandBase() {
			@Override
			public String getName() {
				return name;
			}
			@Override
			public String getUsage(ICommandSender sender) {
				return usage;
			}
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length == 0) {
					sendTextMessage(sender, getUsage(sender));
					return;
				}
				try {
					logic.execute(sender, args);
				} catch (Exception e) {
					throw new CommandException("Invalid Arguments. Please refer to usage for correct arguments.");
				}
			}
		});
	}
	
	public static interface ITaskCommandLogic {
		
		void execute(ICommandSender sender, String... args);

	}

}
