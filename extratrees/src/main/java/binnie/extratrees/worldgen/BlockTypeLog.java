package binnie.extratrees.worldgen;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.api.world.ITreeGenData;

public class BlockTypeLog implements WorldGenBlockType {
	private final ITreeGenData treeGenData;

	public BlockTypeLog(ITreeGenData treeGenData) {
		this.treeGenData = treeGenData;
	}

	@Override
	public void setBlock(World world, ITreeGenData tree, BlockPos pos) {
		treeGenData.setLogBlock(world, pos, EnumFacing.UP);
	}
}
