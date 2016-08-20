package info.tritusk.ingamechecklist.client.handler;

import java.util.Iterator;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.api.ITaskTranslatable;
import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDHandler {

	public static int posX = 10, posY = 45;
	
	static boolean showDescription = true;

	@SubscribeEvent
	public void onRenderingScreen(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.DEBUG)
			return;
		
		if (event.getType() == ElementType.CHAT) {
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

			Gui.drawRect(posX - 5, posY - 5, posX + 105, posY + 125, 1342177280);

			fontRenderer.drawString("Test context", posX, posY, 0x34CD9BF, true);
			fontRenderer.drawString("Loaded " + IClProxy.localTaskManager.getTasks().size() + " entries", posX, posY + 10, 0xCCDDFF, true);
			
			fontRenderer.drawString("Current task: ", posX, posY + 30, 0x9A66CCFF, true);
			
			int posYOffset = 40;
			Iterator<ITask> iterator = IClProxy.localTaskManager.getTasks().iterator();
			while (iterator.hasNext()) {
				ITask entry = iterator.next();
				fontRenderer.drawString(entry.name(), posX, posY + posYOffset, 0xCCDDFF, true);
				if (showDescription) {
					if (entry instanceof ITaskTranslatable)
						for (String line : fontRenderer.listFormattedStringToWidth(((ITaskTranslatable)entry).getTranslation(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode()), 100)) {
							posYOffset += 10;
							fontRenderer.drawString(line, posX, posY + posYOffset, 0xCCDDFF, true);
						}
					else
						for (String line : fontRenderer.listFormattedStringToWidth(entry.description(), 100)) {
							posYOffset += 10;
							fontRenderer.drawString(line, posX, posY + posYOffset, 0xCCDDFF, true);
						}
				} else {
					fontRenderer.drawString("[Press L to show more]", posX, posY + posYOffset + 10, 0xCCDDFF, true);
				} 
			}
		}
	}
}
