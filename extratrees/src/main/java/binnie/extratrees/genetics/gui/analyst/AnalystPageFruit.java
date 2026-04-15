package binnie.extratrees.genetics.gui.analyst;

import binnie.core.Binnie;
import binnie.core.api.genetics.IBreedingSystem;
import binnie.core.api.gui.IArea;
import binnie.core.api.gui.ITitledWidget;
import binnie.core.api.gui.IWidget;
import binnie.core.gui.controls.ControlTextCentered;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.geometry.Point;
import binnie.core.gui.minecraft.control.ControlItemDisplay;
import binnie.core.util.I18N;
import binnie.core.util.Log;
import binnie.core.util.UniqueItemStackSet;
import binnie.genetics.api.analyst.AnalystConstants;
import binnie.genetics.api.analyst.IAnalystManager;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.IAlleleFruit;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IFruitFamily;
import forestry.arboriculture.FruitProviderPod;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class AnalystPageFruit extends Control implements ITitledWidget {
	private static final Unsafe UNSAFE;
	private static final long DROPS_OFFSET;

	// TODO: Mixin accessor?
	static {
		try {
			Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			UNSAFE = (Unsafe) unsafeField.get(null);
			DROPS_OFFSET = UNSAFE.objectFieldOffset(FruitProviderPod.class.getDeclaredField("drops"));
			Log.info("Got drops map from Forestry's FruitProviderPod");
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static Map<ItemStack, Float> getDrops(FruitProviderPod pod) {
		return (Map<ItemStack, Float>) UNSAFE.getObject(pod, DROPS_OFFSET);
	}

	public AnalystPageFruit(IWidget parent, IArea area, ITree ind, IAnalystManager analystManager) {
		super(parent, area);
		setColor(13382400);
		ITreeGenome genome = ind.getGenome();
		int y = 4;
		new ControlTextCentered(this, y, TextFormatting.UNDERLINE + getTitle()).setColor(getColor());
		y += 12;
		IBreedingSystem treeSystem = Binnie.GENETICS.getSystem(TreeManager.treeRoot);
		String yieldAlleleName = treeSystem.getAlleleName(EnumTreeChromosome.YIELD, ind.getGenome().getActiveAllele(EnumTreeChromosome.YIELD));
		new ControlTextCentered(this, y, TextFormatting.ITALIC + I18N.localise(AnalystConstants.FRUIT_KEY + ".yield") + ": " + yieldAlleleName).setColor(getColor());
		y += 20;
		Collection<ItemStack> products = new UniqueItemStackSet();
		Collection<ItemStack> specialties = new UniqueItemStackSet();
		Collection<ItemStack> wiid = new UniqueItemStackSet();
		products.addAll(ind.getProducts().keySet());
		specialties.addAll(ind.getSpecialties().keySet());
		if (ind.getGenome().getFruitProvider() instanceof FruitProviderPod) {
			FruitProviderPod pod = (FruitProviderPod) ind.getGenome().getFruitProvider();
			Collections.addAll(products, getDrops(pod).keySet().toArray(new ItemStack[0]));
		}
		if (products.size() > 0) {
			new ControlTextCentered(this, y, I18N.localise(AnalystConstants.FRUIT_KEY + ".natural")).setColor(getColor());
			y += 10;
			int w = products.size() * 18 - 2;
			int i = 0;
			for (ItemStack stack : products) {
				ControlItemDisplay d = new ControlItemDisplay(this, (getWidth() - w) / 2 + 18 * i, y);
				d.setTooltip();
				d.setItemStack(stack);
			}
			y += 26;
		}
		if (specialties.size() > 0) {
			new ControlTextCentered(this, y, I18N.localise(AnalystConstants.FRUIT_KEY + ".specialty")).setColor(getColor());
			y += 10;
			int w = products.size() * 18 - 2;
			int i = 0;
			for (ItemStack stack : specialties) {
				ControlItemDisplay d = new ControlItemDisplay(this, (getWidth() - w) / 2 + 18 * i, y);
				d.setTooltip();
				d.setItemStack(stack);
			}
			y += 26;
		}
		Collection<ItemStack> allProducts = new UniqueItemStackSet();
		allProducts.addAll(products);
		allProducts.addAll(specialties);
		Collection<ItemStack> refinedProducts = new UniqueItemStackSet();
		refinedProducts.addAll(analystManager.getAllProductsAndFluids(allProducts));
		if (refinedProducts.size() > 0) {
			y = analystManager.drawRefined(this, I18N.localise(AnalystConstants.FRUIT_KEY + ".refined"), y, refinedProducts);
			y += 8;
		}
		if (products.size() == 0 && specialties.size() == 0) {
			new ControlTextCentered(this, y, I18N.localise(AnalystConstants.FRUIT_KEY + ".noFruits")).setColor(getColor());
			y += 28;
		}
		new ControlTextCentered(this, y, I18N.localise(AnalystConstants.FRUIT_KEY + ".possible")).setColor(getColor());
		y += 12;
		Collection<IAllele> fruitAlleles = Binnie.GENETICS.getChromosomeMap(TreeManager.treeRoot).get(EnumTreeChromosome.FRUITS);
		for (IFruitFamily fam : ind.getGenome().getPrimary().getSuitableFruit()) {
			Collection<ItemStack> stacks = new UniqueItemStackSet();
			for (IAllele a : fruitAlleles) {
				if (((IAlleleFruit) a).getProvider().getFamily() == fam) {
					stacks.addAll(((IAlleleFruit) a).getProvider().getProducts().keySet());
					stacks.addAll(((IAlleleFruit) a).getProvider().getSpecialty().keySet());
					if (a.getUID().contains("fruitCocoa")) {
						stacks.add(new ItemStack(Items.DYE, 1, 3));
					} else if (((IAlleleFruit) a).getProvider() instanceof FruitProviderPod) {
						FruitProviderPod pod2 = (FruitProviderPod) ((IAlleleFruit) a).getProvider();
						Collections.addAll(stacks, getDrops(pod2).keySet().toArray(new ItemStack[0]));
					}
				}
			}
			y = analystManager.drawRefined(this, TextFormatting.ITALIC + fam.getName(), y, stacks);
			y += 2;
		}
		setSize(new Point(getWidth(), y + 8));
	}

	@Override
	public String getTitle() {
		return I18N.localise(AnalystConstants.FRUIT_KEY + ".title");
	}
}
