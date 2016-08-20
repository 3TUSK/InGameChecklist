package info.tritusk.ingamechecklist.client;

import org.lwjgl.input.Keyboard;

import info.tritusk.ingamechecklist.client.handler.HUDHandler;
import info.tritusk.ingamechecklist.client.handler.KeyBindingHandler;
import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.command.CommandAdjustPos;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IClProxyClient extends IClProxy {
	
	public static final KeyBinding KEY_SHOW_TASK_DESC = new KeyBinding("key.showTaskDesc", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, Keyboard.KEY_L, "key.category.icl");
	
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
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
		MinecraftForge.EVENT_BUS.register(new KeyBindingHandler());
		
		ClientCommandHandler.instance.registerCommand(new CommandAdjustPos());
	}

}
