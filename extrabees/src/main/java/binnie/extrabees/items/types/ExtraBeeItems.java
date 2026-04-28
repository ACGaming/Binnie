package binnie.extrabees.items.types;

import binnie.core.util.I18N;
import binnie.extrabees.modules.ModuleCore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public enum ExtraBeeItems implements IEBItemMiscProvider {

	SCENTED_GEAR("extrabees.item.misc.scented_gear", "scented_gear"),
	DIAMOND_SHARD("extrabees.item.misc.diamond_shard", "diamond_shard") {
		@Override
		protected void init() {
			setGem("Diamond");
		}
	},
	EMERALD_SHARD("extrabees.item.misc.emerald_shard", "emerald_shard") {
		@Override
		protected void init() {
			setGem("Emerald");
		}
	},
	RUBY_SHARD("extrabees.item.misc.ruby_shard", "ruby_shard") {
		@Override
		protected void init() {
			setGem("Ruby");
		}
	},
	SAPPHIRE_SHARD("extrabees.item.misc.sapphire_shard", "sapphire_shard") {
		@Override
		protected void init() {
			setGem("Sapphire");
		}
	},
	LAPIS_SHARD("extrabees.item.misc.lapis_shard", "lapis_shard"),
	IRON_DUST("extrabees.item.misc.iron_dust", "iron_dust") {
		@Override
		protected void init() {
			setMetal("Iron");
		}
	},
	GOLD_DUST("extrabees.item.misc.gold_dust", "gold_dust") {
		@Override
		protected void init() {
			setMetal("Gold");
		}
	},
	SILVER_DUST("extrabees.item.misc.silver_dust", "silver_dust") {
		@Override
		protected void init() {
			setMetal("Silver");
		}
	},
	PLATINUM_DUST("extrabees.item.misc.platinum_dust", "platinum_dust") {
		@Override
		protected void init() {
			setMetal("Platinum");
		}
	},
	COPPER_DUST("extrabees.item.misc.copper_dust", "copper_dust") {
		@Override
		protected void init() {
			setMetal("Copper");
		}
	},
	TIN_DUST("extrabees.item.misc.tin_dust", "tin_dust") {
		@Override
		protected void init() {
			setMetal("Tin");
		}
	},
	NICKEL_DUST("extrabees.item.misc.nickel_dust", "nickel_dust") {
		@Override
		protected void init() {
			setMetal("Nickel");
		}
	},
	LEAD_DUST("extrabees.item.misc.lead_dust", "lead_dust") {
		@Override
		protected void init() {
			setMetal("Lead");
		}
	},
	ZINC_DUST("extrabees.item.misc.zinc_dust", "zinc_dust") {
		@Override
		protected void init() {
			setMetal("Zinc");
		}
	},
	TITANIUM_DUST("extrabees.item.misc.titanium_dust", "titanium_dust") {
		@Override
		protected void init() {
			setMetal("Titanium");
		}
	},
	TUNGSTEN_DUST("extrabees.item.misc.tungsten_dust", "tungsten_dust") {
		@Override
		protected void init() {
			setMetal("Tungsten");
		}
	},
	URANIUM_DUST("extrabees.item.misc.radioactive_dust", "radioactive_dust"),
	COAL_DUST("extrabees.item.misc.coal_dust", "coal_dust") {
		@Override
		protected void init() {
			setMetal("Coal");
		}
	},
	RED_DYE("extrabees.item.misc.dye_red", "dye_red"),
	YELLOW_DYE("extrabees.item.misc.dye_yellow", "dye_yellow"),
	BLUE_DYE("extrabees.item.misc.dye_blue", "dye_blue"),
	GREEN_DYE("extrabees.item.misc.dye_green", "dye_green"),
	WHITE_DYE("extrabees.item.misc.dye_white", "dye_white"),
	BLACK_DYE("extrabees.item.misc.dye_black", "dye_black"),
	BROWN_DYE("extrabees.item.misc.dye_brown", "dye_brown"),
	CLAY_DUST("extrabees.item.misc.clay_dust", "clay_dust"),
	YELLORIUM_DUST("extrabees.item.misc.yellorium_dust", "yellorium_dust") {
		@Override
		protected void init() {
			setMetal("Yellorium");
		}
	},
	BLUTONIUM_DUST("extrabees.item.misc.blutonium_dust", "blutonium_dust") {
		@Override
		protected void init() {
			setMetal("Blutonium");
		}
	},
	CYANITE_DUST("extrabees.item.misc.cyanite_dust", "cyanite_dust") {
		@Override
		protected void init() {
			setMetal("Cyanite");
		}
	};

	public final String name;
	public final String modelPath;
	@Nullable
	public String metalString;
	@Nullable
	public String gemString;

	ExtraBeeItems(String name, String modelPath) {
		this.metalString = null;
		this.gemString = null;
		this.name = name;
		this.modelPath = modelPath;
		init();
	}

	protected void setGem(final String string) {
		this.gemString = string;
	}

	protected void setMetal(final String string) {
		this.metalString = string;
	}

	@Override
	public boolean isActive() {
		if (this.metalString != null) {
			NonNullList<ItemStack> ingots = OreDictionary.getOres("ingot" + this.metalString);
			NonNullList<ItemStack> dust = OreDictionary.getOres("dust" + this.metalString);
			return !ingots.isEmpty() || !dust.isEmpty() || this == ExtraBeeItems.COAL_DUST;
		}
		NonNullList<ItemStack> gems = OreDictionary.getOres("gem" + this.gemString);
		return this.gemString == null || !gems.isEmpty();
	}

	protected void init() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final List<String> tooltip) {
	}

	@Override
	public String getName(final ItemStack itemStack) {
		return I18N.localise(name);
	}

	@Override
	public ItemStack get(final int amount) {
		return new ItemStack(ModuleCore.itemMisc, amount, this.ordinal());
	}

	@Override
	public String getModelPath() {
		return modelPath;
	}

}
