package binnie.extrabees.gui;

import binnie.core.api.gui.IWidget;
import binnie.core.gui.Attribute;
import binnie.core.gui.CraftGUI;
import binnie.core.gui.ITooltip;
import binnie.core.gui.Tooltip;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.geometry.Area;
import binnie.core.gui.resource.textures.CraftGUITexture;
import binnie.core.gui.renderer.RenderUtil;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ControlBiomes extends Control implements ITooltip {
	private final List<ResourceLocation> tolerated;
	private static final BlockPos POSITION = new BlockPos(0, 64, 0);

	public ControlBiomes(IWidget parent, int x, int y, int width, int height) {
		super(parent, x, y, width * 16, height * 16);
		tolerated = new ArrayList<>();
		addAttribute(Attribute.MOUSE_OVER);
	}

	private Biome getBiome(ResourceLocation Id) {
        return Biome.REGISTRY.getObject(Id);
    }

	private Biome getToleratedBiome(int i) {
		if (i >= tolerated.size()) {
			return null;
		}
		ResourceLocation resourceLocation = tolerated.get(i);
		return getBiome(resourceLocation);
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

		Biome biome = getToleratedBiome(i);
		if (biome != null) {
			tooltip.add(biome.getBiomeName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderForeground(int guiWidth, int guiHeight) {
		for (int i = 0; i < tolerated.size(); ++i) {
			int x = i % 8 * 16;
			int y = i / 8 * 16;
			Area area = new Area(x, y, 16, 16);

			Biome biome = getToleratedBiome(i);
			int color;
			if (biome != null) {
				color = biome.getGrassColorAtPos(POSITION);
			} else {
				color = 0x555555;
			}
			RenderUtil.drawSolidRect(area, color);
			CraftGUI.RENDER.texture(CraftGUITexture.BUTTON, area);
		}
	}

	public void setSpecies(IAlleleBeeSpecies species) {
		tolerated.clear();
		IBeeGenome genome = BeeManager.beeRoot.templateAsGenome(BeeManager.beeRoot.getTemplate(species));
		IBee bee = BeeManager.beeRoot.getBee(genome);

		for (Biome biome : bee.getSuitableBiomes()) {
			ResourceLocation register = biome.getRegistryName();
			if (register != null) {
				tolerated.add(register);
			}
		}
	}
}
