package info.tritusk.customchecklist.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncChecklist implements IMessage, IMessageHandler<PacketSyncChecklist, IMessage>{

	public String name, description;
	
	public PacketSyncChecklist() {}
	
	public PacketSyncChecklist(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@Override
	public IMessage onMessage(PacketSyncChecklist message, MessageContext ctx) {
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		this.name = packet.readStringFromBuffer(32);
		this.description = packet.readStringFromBuffer(512);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeString(name);
		packet.writeString(description);
	}

}
