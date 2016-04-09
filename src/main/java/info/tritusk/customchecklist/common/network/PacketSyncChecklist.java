package info.tritusk.customchecklist.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncChecklist implements IMessage, IMessageHandler<PacketSyncChecklist, IMessage>{

	@Override
	public IMessage onMessage(PacketSyncChecklist message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
	}

}
