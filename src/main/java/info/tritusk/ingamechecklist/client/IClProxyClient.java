package info.tritusk.ingamechecklist.client;

import org.lwjgl.input.Keyboard;

import info.tritusk.ingamechecklist.client.handler.HUDHandler;
import info.tritusk.ingamechecklist.client.handler.KeyBindingHandler;
import info.tritusk.ingamechecklist.common.IClConfig;
import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IClProxyClient extends IClProxy {
	
	public static final KeyBinding KEY_SHOW_TASK_DESC = new KeyBinding("key.showTaskDesc", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, Keyboard.KEY_L, "key.category.icl");
	
	public static RemoteChecklistManager manager;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);	
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ClientRegistry.registerKeyBinding(KEY_SHOW_TASK_DESC);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
		MinecraftForge.EVENT_BUS.register(new KeyBindingHandler());
		if (IClConfig.debug)
			ClientCommandHandler.instance.registerCommand(new CommandTask());
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		String url = null;
		if (Minecraft.getMinecraft().getCurrentServerData() != null) {
			if (IClConfig.enableRemoteTasksRetrive && IClConfig.remoteTaskURL != null) {
				url = IClConfig.remoteTaskURL;
			} else {
				url = Minecraft.getMinecraft().getCurrentServerData().serverIP;
			} 
		}
		if (url != null)
			manager = new RemoteChecklistManager(url);
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		manager = null;
	}

}
