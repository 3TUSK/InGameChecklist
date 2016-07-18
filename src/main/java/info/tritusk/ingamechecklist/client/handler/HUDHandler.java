package info.tritusk.ingamechecklist.client.handler;

import info.tritusk.ingamechecklist.common.task.TaskEntry;
import info.tritusk.ingamechecklist.common.task.TaskEntryLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDHandler {

	public static int posX = 10, posY = 45;

	@SubscribeEvent
	public void onRenderingScreen(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.DEBUG)
			return;
		
		if (event.getType() == ElementType.CHAT) {
			// something
			// ScaledResolution resolution = event.getResolution();
			// int width = resolution.getScaledWidth(), height =
			// resolution.getScaledHeight();
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

			Gui.drawRect(posX - 5, posY - 5, posX + 105, posY + 125, 1342177280);

			fontRenderer.drawString("Test context", posX, posY, 0x34CD9BF, true);
			fontRenderer.drawString("Loaded " + TaskEntryLoader.localEntryList.size() + " entries", posX, posY + 10, 0xCCDDFF, true);
			
			fontRenderer.drawString("Current task: ", posX, posY + 30, 0x3034930, true);
			
			int posYOffset = 40;
			TaskEntry entry = TaskEntryLoader.localEntryList.get(/*new java.util.Random().nextInt(TaskEntryLoader.localEntryList.size())*/0);
			fontRenderer.drawString(entry.getName(), posX, posY + posYOffset, 0xCCDDFF, true);
			for (String line : fontRenderer.listFormattedStringToWidth(entry.getDescription(), 100)) {
				posYOffset += 10;
				fontRenderer.drawString(line, posX, posY + posYOffset, 0xCCDDFF, true);
			}
		}
	}
}
