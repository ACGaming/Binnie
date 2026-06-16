package binnie.extrabees.gui;

import binnie.core.api.gui.IWidget;
import binnie.core.gui.Attribute;
import binnie.core.gui.CraftGUI;
import binnie.core.gui.ITooltip;
import binnie.core.gui.Tooltip;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.geometry.Area;
import binnie.core.gui.resource.textures.CraftGUITexture;
import binnie.core.util.I18N;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ControlBiomes extends Control implements ITooltip {
	private final List<Integer> tolerated;

	public ControlBiomes(IWidget parent, int x, int y, int width, int height) {
		super(parent, x, y, width * 16, height * 16);
		tolerated = new ArrayList<>();
		addAttribute(Attribute.MOUSE_OVER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getTooltip(Tooltip tooltip, ITooltipFlag tooltipFlag) {
		if (tolerated.isEmpty()) {
			return;
		}

		int x = (int) (getRelativeMousePosition().xPos() / 16.0f);
		int y = (int) (getRelativeMousePosition().yPos() / 16.0f);
		int i = x + y * 8;
		if (i >= tolerated.size()) {
			return;
		}

		Biome biome = Biome.getBiome(tolerated.get(i));
		if (biome != null) {
			tooltip.add(I18N.localise("biome." + biome.getRegistryName().toString().replace(":", ".") + ".name"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderForeground(int guiWidth, int guiHeight) {
		for (int i = 0; i < tolerated.size(); ++i) {
			int x = i % 8 * 16;
			int y = i / 8 * 16;
			int id = tolerated.get(i);
			Biome biome = Biome.getBiome(id);
			Area area = new Area(x, y, 16, 16);
			CraftGUI.RENDER.colour(0xFFAAAAAA);
			CraftGUI.RENDER.texture(CraftGUITexture.BUTTON, area);
			if (biome != null) {
				int color = 0x99000000 | biome.color;
				CraftGUI.RENDER.colour(color);
				CraftGUI.RENDER.rectangle(area);
			}
			CraftGUI.RENDER.colour(0xFFFFFFFF);
		}
	}

	public void setSpecies(IAlleleBeeSpecies species) {
		tolerated.clear();
		IBeeGenome genome = BeeManager.beeRoot.templateAsGenome(BeeManager.beeRoot.getTemplate(species));
		IBee bee = BeeManager.beeRoot.getBee(genome);
		tolerated.addAll(bee.getToleratedBiomeIds());
	}
}
