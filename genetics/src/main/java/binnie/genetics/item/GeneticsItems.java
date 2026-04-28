package binnie.genetics.item;

import binnie.core.item.IItemMiscProvider;
import binnie.core.util.I18N;
import binnie.genetics.modules.ModuleCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public enum GeneticsItems implements IItemMiscProvider {
	LaboratoryCasing("genetics.item.misc.casing_iron", "casing_iron"),
	DNADye("genetics.item.misc.dna_dye", "dna_dye"),
	FluorescentDye("genetics.item.misc.dye_fluor", "dye_fluor"),
	Enzyme("genetics.item.misc.enzyme", "enzyme"),
	GrowthMedium("genetics.item.misc.growth_medium", "growth_medium"),
	EmptySequencer("genetics.item.misc.sequencer_empty", "sequencer_empty"),
	EMPTY_SERUM("genetics.item.misc.serum_empty", "serum_empty"),
	EMPTY_GENOME("genetics.item.misc.genome_empty", "genome_empty"),
	IntegratedCircuit("genetics.item.misc.integrated_circuit", "integrated_circuit"),
	IntegratedCPU("genetics.item.misc.integrated_cpu", "integrated_cpu"),
	IntegratedCasing("genetics.item.misc.casing_circuit", "casing_circuit");

	private final String name;
	private final String modelPath;

	GeneticsItems(final String name, final String modelPath) {
		this.name = name;
		this.modelPath = modelPath;
	}

	@Override
	public String getModelPath() {
		return modelPath;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final List<String> tooltip) {
	}

	@Override
	public String getDisplayName(final ItemStack stack) {
		return I18N.localise(name);
	}

	@Override
	public ItemStack get(final int size) {
		return new ItemStack(ModuleCore.itemGenetics, size, this.ordinal());
	}

	public ItemStack get(Item itemGenetics, final int size) {
		return new ItemStack(itemGenetics, size, this.ordinal());
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
