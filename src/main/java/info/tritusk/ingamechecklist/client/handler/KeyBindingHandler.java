package info.tritusk.ingamechecklist.client.handler;

import info.tritusk.ingamechecklist.client.IClProxyClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyBindingHandler {
	
	@SubscribeEvent
	public void onKeyPressing(InputEvent.KeyInputEvent event) {
		if (IClProxyClient.KEY_SHOW_TASK_DESC.isPressed()) {
			HUDHandler.showDescription = !HUDHandler.showDescription;
		}
	}

}
