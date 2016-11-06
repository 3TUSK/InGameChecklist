package info.tritusk.ingamechecklist.client.handler;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDHandler {

	public static int posX = 10, posY = 45;
	public static String currentPointingTask;
	
	static boolean showDescription = true;
	
	private int posYOffset = 40;

	@SubscribeEvent
	public void onRenderingScreen(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
			String strToDraw = "";
			Gui.drawRect(posX - 5, posY - 5, posX + 105, posY + 125, 1342177280);
			
			strToDraw = String.format("Loaded %d entries", IClProxy.manager.getTasks().size());
			fontRenderer.drawString(strToDraw, posX, posY, 0xCCDDFF, true);
			
			ITask task = IClProxy.manager.getByName(currentPointingTask);
			if (task != null) {
				strToDraw = String.format("Pinned task: %s", currentPointingTask);
				fontRenderer.drawString(strToDraw, posX, posY + 30, 0x9A66CCFF, true);
				if (showDescription) {
					fontRenderer.listFormattedStringToWidth(task.description(), 100).forEach(line -> {
						fontRenderer.drawString(line, posX, posY + posYOffset, 0xCCDDFF, true);
						posYOffset += 10;
					});
				} else {
					fontRenderer.drawString("[Press L to show more]", posX, posY + posYOffset, 0xCCDDFF, true);
				}
			} else {
				fontRenderer.drawString("No pinned task", posX, posY + 30, 0x9A66CCFF, true);
			}

			posYOffset = 40; //Reset after drawing finished
		}
	}
	
	public static boolean safeSetPinnedTask(String taskName) {
		if (IClProxy.manager.getByName(taskName) != null) {
			currentPointingTask = taskName;
			return true;
		} else {
			return false;
		}
	}
}
