package binnie.core.block;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class TileEntityMetadata extends TileEntity {
	private int meta;
	private boolean droppedBlock;

	public TileEntityMetadata() {
		this.droppedBlock = false;
	}

	@Nullable
	public static TileEntityMetadata getTile(final IBlockAccess world, final BlockPos pos) {
		final IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock().hasTileEntity(blockState)) {
			final TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileEntityMetadata) {
				return (TileEntityMetadata) tile;
			}
		}
		return null;
	}

	public static ItemStack getItemStack(final Block block, final int damage) {
		final Item item = Item.getItemFromBlock(block);
		Preconditions.checkNotNull(item, "Could not get item for block %s", block);
		final ItemStack itemStack = new ItemStack(item, 1, 0);
		setItemDamage(itemStack, damage);
		return itemStack;
	}

	public static void setItemDamage(final ItemStack item, final int i) {
		item.setItemDamage(Math.min(i, 16387));
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("meta", i);
		item.setTagCompound(tag);
	}

	public static int getItemDamage(final ItemStack item) {
		if (item.hasTagCompound() && item.getTagCompound().hasKey("meta")) {
			return item.getTagCompound().getInteger("meta");
		}
		return item.getItemDamage();
	}

	public static int getTileMetadata(final IBlockAccess world, final BlockPos pos) {
		if (world == null || pos == null) return 0;
		final TileEntityMetadata tile = getTile(world, pos);
		return (tile == null) ? 0 : tile.getTileMetadata();
	}

	@Override
	public boolean receiveClientEvent(final int id, final int type) {
		if (id == 42) {
			this.meta = type;
			markDirty();
		}
		return true;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (world != null) {
			setTileMetadata(nbt.getInteger("meta"), world.isRemote);
		} else {
			this.meta = nbt.getInteger("meta");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("meta", this.meta);
		return nbt;
	}

	public int getTileMetadata() {
		return this.meta;
	}

	public void setTileMetadata(final int meta, final boolean notify) {
		if (this.meta != meta) {
			this.meta = meta;
			if (notify) {
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 3);
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	public boolean hasDroppedBlock() {
		return this.droppedBlock;
	}

	public void dropBlock() {
		this.droppedBlock = true;
	}
}
