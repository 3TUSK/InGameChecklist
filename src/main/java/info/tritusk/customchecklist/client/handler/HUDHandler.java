package info.tritusk.customchecklist.client.handler;

import info.tritusk.customchecklist.common.task.TaskEntryLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDHandler {
	
	public static int posX = 10, posY = 100;

	@SubscribeEvent
	public void onRenderingScreen(RenderGameOverlayEvent.Post event) {
		//something
		//ScaledResolution resolution = event.getResolution();
		//int width = resolution.getScaledWidth(), height = resolution.getScaledHeight();
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		
		fontRenderer.drawString("Test context", posX, posY, 0x34CD9BF, true);
		
		fontRenderer.drawString("Loaded " + TaskEntryLoader.localEntryList.size() + " entries", posX, posY + 30, 0xCCDDFF, true);
	}
}
