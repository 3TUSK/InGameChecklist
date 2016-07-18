package info.tritusk.ingamechecklist.common.command;

import info.tritusk.ingamechecklist.client.handler.HUDHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandAdjustPos extends CommandBase {

	@Override
	public String getCommandName() {
		return "customchecklistpos";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/customchecklistpos <x> <y>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2) {
			sendTextMessage(sender, "Invalid arguments deteched...");
			return;
		}
		
		HUDHandler.posX = Integer.parseInt(args[0]);
		HUDHandler.posY = Integer.parseInt(args[1]);
		
	}
	
	private void sendTextMessage(ICommandSender sender, String message) {
		sender.addChatMessage(new TextComponentString(message));
	}

	
}
