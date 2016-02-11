package gui;

public class MassData {
	private double mass;
	private double inv_mass;

	MassData(double mass) {
		this.mass = mass;

		if (mass == 0) {
			this.inv_mass = 0;
		} else {
			this.inv_mass = 1 / mass;
		}
	}

	public double getMass() {
		return mass;
	}

	public double getInv_mass() {
		return inv_mass;
	}
}
