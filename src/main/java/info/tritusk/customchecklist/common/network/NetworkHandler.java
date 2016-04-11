package info.tritusk.customchecklist.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("CustomChecklist");

	public static void init() {
		INSTANCE.registerMessage(PacketSyncChecklist.class, PacketSyncChecklist.class, 0, Side.CLIENT);
	}
}
