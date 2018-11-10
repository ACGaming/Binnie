package binnie.extratrees.integration.jei.distillery;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import binnie.extratrees.api.recipes.ExtraTreesRecipeManager;
import binnie.extratrees.api.recipes.IDistilleryManager;
import binnie.extratrees.api.recipes.IDistilleryRecipe;

public class DistilleryRecipeMaker {
	public static List<DistilleryRecipeWrapper> create() {
		List<DistilleryRecipeWrapper> recipes = new ArrayList<>();

		IDistilleryManager distilleryManager = ExtraTreesRecipeManager.distilleryManager;
		if (distilleryManager == null) {
			return recipes;
		}

		for (int level = 0; level < 3; level++) {
			for (IDistilleryRecipe recipe : distilleryManager.recipes(level)) {
				FluidStack input = recipe.getInput();
				FluidStack output = recipe.getOutput();
				recipes.add(new DistilleryRecipeWrapper(level, input, output));
			}
		}

		return recipes;
	}
}
