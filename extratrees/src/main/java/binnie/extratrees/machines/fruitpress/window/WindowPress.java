package binnie.extratrees.machines.fruitpress.window;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import binnie.core.api.gui.Alignment;
import binnie.core.gui.minecraft.InventoryType;
import binnie.core.gui.minecraft.Window;
import binnie.core.gui.minecraft.control.ControlEnergyBar;
import binnie.core.gui.minecraft.control.ControlErrorState;
import binnie.core.gui.minecraft.control.ControlLiquidTank;
import binnie.core.gui.minecraft.control.ControlPlayerInventory;
import binnie.core.gui.minecraft.control.ControlSlot;
import binnie.core.machines.Machine;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.machines.fruitpress.FruitPressLogic;
import binnie.extratrees.machines.fruitpress.FruitPressMachine;

public class WindowPress extends Window {
	public WindowPress(EntityPlayer player, IInventory inventory, Side side) {
		super(194, 192, player, inventory, side);
	}

	@Nullable
	public static Window create(EntityPlayer player, @Nullable IInventory inventory, Side side) {
		if (inventory == null) {
			return null;
		}
		return new WindowPress(player, inventory, side);
	}

	@Override
	protected String getModId() {
		return ExtraTrees.instance.getModId();
	}

	@Override
	protected String getBackgroundTextureName() {
		return "Press";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initialiseClient() {
		this.setTitle(Machine.getMachine(this.getInventory()).getPackage().getDisplayName());
		new ControlSlot.Builder(this, 24, 52).assign(FruitPressMachine.SLOT_FRUIT);
		new ControlLiquidTank(this, 99, 32, FruitPressMachine.TANK_OUTPUT);
		new ControlEnergyBar(this, 154, 32, 16, 60, Alignment.BOTTOM);
		new ControlPlayerInventory(this);
		new ControlErrorState(this, 128, 54);
		new ControlFruitPressProgress(this, 62, 24);
		((Window) this.getTopParent()).getContainer().createClientSlot(InventoryType.MACHINE, FruitPressMachine.SLOT_CURRENT);
	}

	@Override
	public void receiveGuiNBTOnServer(EntityPlayer player, String name, NBTTagCompound nbt) {
		super.receiveGuiNBTOnServer(player, name, nbt);
		FruitPressLogic logic = Machine.getInterface(FruitPressLogic.class, this.getInventory());
		if (logic != null) {
			if (name.equals("fruitpress-click") && logic.canWork() == null && (logic.canProgress() == null || logic.canProgress().isPowerError())) {
				logic.alterProgress(2.0f);
			} else if (name.equals("clear-fruit")) {
				logic.setProgress(0.0f);
				this.getInventory().setInventorySlotContents(FruitPressMachine.SLOT_CURRENT, ItemStack.EMPTY);
			}
		}
	}
}
