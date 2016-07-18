package info.tritusk.customchecklist.common.command;

import info.tritusk.customchecklist.common.network.NetworkHandler;
import info.tritusk.customchecklist.common.network.PacketSyncChecklist;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandRemoteTasksSync extends CommandBase {

	@Override
	public String getCommandName() {
		return "customchecklistsync";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/customchecklistsync - Keep your checklist synchronize with remote server";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		//Blame @bakaxyf, the following code WILL CRASH GAME, and it will be updated soon
		NetworkHandler.INSTANCE.sendToAll(new PacketSyncChecklist(args[0], args[1]));
	}

}
