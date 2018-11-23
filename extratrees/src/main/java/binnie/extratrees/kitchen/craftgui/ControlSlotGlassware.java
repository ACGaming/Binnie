package binnie.extratrees.kitchen.craftgui;

import net.minecraft.item.ItemStack;

import binnie.core.api.gui.IWidget;
import binnie.core.gui.controls.core.IControlValue;
import binnie.core.gui.minecraft.control.ControlSlotBase;
import binnie.extratrees.alcohol.GlasswareType;

public class ControlSlotGlassware extends ControlSlotBase implements IControlValue<GlasswareType> {
	private GlasswareType glasswareType;

	public ControlSlotGlassware(IWidget parent, int x, int y, GlasswareType glasswareType) {
		super(parent, x, y);
		this.glasswareType = glasswareType;
	}

	@Override
	public GlasswareType getValue() {
		return this.glasswareType;
	}

	@Override
	public void setValue(GlasswareType value) {
		this.glasswareType = value;
	}

	@Override
	public ItemStack getItemStack() {
		return this.glasswareType.get(1);
	}
}
