package binnie.extratrees.machines.lumbermill.window;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import binnie.core.api.gui.Alignment;
import binnie.core.gui.minecraft.Window;
import binnie.core.gui.minecraft.control.ControlEnergyBar;
import binnie.core.gui.minecraft.control.ControlErrorState;
import binnie.core.gui.minecraft.control.ControlLiquidTank;
import binnie.core.gui.minecraft.control.ControlPlayerInventory;
import binnie.core.gui.minecraft.control.ControlSlot;
import binnie.core.machines.Machine;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.lumbermill.LumbermillMachine;

public class WindowLumbermill extends Window {
	public WindowLumbermill(EntityPlayer player, IInventory inventory, Side side) {
		super(220, 192, player, inventory, side);
	}

	@Nullable
	public static Window create(EntityPlayer player, @Nullable IInventory inventory, Side side) {
		if (inventory == null) {
			return null;
		}
		return new WindowLumbermill(player, inventory, side);
	}

	@Override
	protected String getModId() {
		return ExtraTrees.instance.getModId();
	}

	@Override
	protected String getBackgroundTextureName() {
		return "Lumbermill";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initialiseClient() {
		this.setTitle(Machine.getMachine(this.getInventory()).getPackage().getDisplayName());
		new ControlSlot.Builder(this, 42, 43).assign(LumbermillMachine.SLOT_LOG);
		new ControlSlot.Builder(this, 148, 43).assign(LumbermillMachine.SLOT_PLANKS);
		new ControlSlot.Builder(this, 172, 28).assign(LumbermillMachine.SLOT_BARK);
		new ControlSlot.Builder(this, 172, 58).assign(LumbermillMachine.SLOT_SAWDUST);
		new ControlLumbermillProgress(this, 70, 43);
		new ControlLiquidTank(this, 16, 32, LumbermillMachine.TANK_WATER);
		new ControlEnergyBar(this, 8, 112, 16, 60, Alignment.BOTTOM);
		new ControlPlayerInventory(this);
		new ControlErrorState(this, 95, 73);
	}
}
