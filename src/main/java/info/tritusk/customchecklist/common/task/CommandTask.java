package info.tritusk.customchecklist.common.task;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandTask extends CommandBase {

	@Override
	public String getCommandName() {
		return "/customchecklist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/customchecklist [add|remove|show] [task]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		switch (args[0]) {
		case("add"):
			break;
		case("remove"):
			break;
		case("show"):
			break;
		default:
			throw new CommandException("Invalid parameter, please double check");
		}
	}

}
