package binnie.extratrees.genetics.fruits;

import binnie.core.util.I18N;
import forestry.api.genetics.IFruitFamily;

public enum ETFruitFamily implements IFruitFamily {
	BERRY("extratrees.genetics.fruits.berry", "berry", "berri"),
	CITRUS("extratrees.genetics.fruits.citrus", "citrus", "citrus");

	private final String name;
	private final String uid;
	private final String scientific;

	ETFruitFamily(final String name, final String uid, final String scientific) {
		this.name = name;
		this.uid = uid;
		this.scientific = scientific;
	}

	@Override
	public String getUID() {
		return "binnie.family." + this.uid;
	}

	@Override
	public String getName() {
		return I18N.localise(this.name);
	}

	@Override
	public String getScientific() {
		return this.scientific;
	}

	@Override
	public String getDescription() {
		return I18N.localise(this.name);
	}
}
