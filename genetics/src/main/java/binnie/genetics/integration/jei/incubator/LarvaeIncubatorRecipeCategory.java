package binnie.genetics.integration.jei.incubator;

import binnie.genetics.integration.jei.RecipeUids;
import binnie.core.util.I18N;

public class LarvaeIncubatorRecipeCategory extends IncubatorRecipeCategory {
	@Override
	public String getUid() {
		return RecipeUids.INCUBATOR_LARVAE;
	}

	@Override
	public String getTitle() {
		return I18N.localise("genetics.machine.lab_machine.incubator.larvae.jeiTitle");
	}
}
