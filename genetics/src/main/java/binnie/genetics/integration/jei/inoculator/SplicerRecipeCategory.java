package binnie.genetics.integration.jei.inoculator;

import binnie.genetics.integration.jei.RecipeUids;
import binnie.core.util.I18N;

public class SplicerRecipeCategory extends InoculatorRecipeCategory {
	public SplicerRecipeCategory() {
		super(true);
	}

	@Override
	public String getUid() {
		return RecipeUids.SPLICER;
	}

	@Override
	public String getTitle() {
		return I18N.localise("genetics.machine.inoculator.splicer.jeiTitle");
	}
}
