package binnie.extrabees.gui;

import binnie.core.api.gui.IWidget;
import binnie.core.gui.Attribute;
import binnie.core.gui.CraftGUI;
import binnie.core.gui.ITooltip;
import binnie.core.gui.Tooltip;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.controls.scroll.ControlScrollableContent;
import binnie.core.gui.geometry.Area;
import binnie.core.gui.geometry.Point;
import binnie.core.gui.resource.textures.CraftGUITexture;
import binnie.core.gui.renderer.RenderUtil;
import binnie.core.util.I18N;
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

public class ControlBiomes extends ControlScrollableContent<Control> implements ITooltip {
	private final List<ResourceLocation> tolerated;
	private final Control content;
	private static final BlockPos POSITION = new BlockPos(0, 64, 0);

	public ControlBiomes(IWidget parent, int x, int y, int width, int height) {
		super(parent, x, y, width, height, 16);
		tolerated = new ArrayList<>();
		addAttribute(Attribute.MOUSE_OVER);
		content = new Control(this, 0, 0, width - 16, 0);
		setScrollableContent(content);
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

		float mouseX = getRelativeMousePosition().xPos();
		float mouseY = getRelativeMousePosition().yPos();
		if (mouseX >= getSize().xPos() - 16) {
			return;
		}
		
		int x = (int) (mouseX / 16.0f);
		int y = (int) (mouseY / 16.0f);
		int i = x + y * 8;
		if (i >= tolerated.size()) {
			return;
		}

		Biome biome = getToleratedBiome(i);
		if (biome != null) {
			String text = I18N.localise(biome.getRegistryName().toString());
			tooltip.add(text);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderForeground(int guiWidth, int guiHeight) {
		for (int i = 0; i < tolerated.size(); ++i) {
			int x = i % 8 * 16;
			int y = i / 8 * 16;
			Area area = new Area(x, y, 16, 16);

			CraftGUI.RENDER.texture(CraftGUITexture.BUTTON, area);
			Biome biome = getToleratedBiome(i);
			int color;
			if (biome != null) {
				color = biome.getGrassColorAtPos(POSITION);
			} else {
				color = 0x555555;
			}
			RenderUtil.drawSolidRect(area, (180 << 24) | (color & 0x00FFFFFF));
			
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
		int rows = (int) Math.ceil(tolerated.size() / 8.0f);
		content.setSize(new Point(content.getSize().xPos(), rows * 16));
		movePercentage(-100f);
	}
}
