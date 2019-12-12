package day12;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle12 {

	public static void main(String[] args) {
		List<Pair> xAxis = Arrays.asList(new Pair(-7), new Pair(-12), new Pair(6), new Pair(4));
		List<Pair> yAxis = Arrays.asList(new Pair(-8), new Pair(-3), new Pair(-17), new Pair(-10));
		List<Pair> zAxis = Arrays.asList(new Pair(9), new Pair(-4), new Pair(-9), new Pair(-6));

		calculateEnergyOfSystem(xAxis, yAxis, zAxis);
		calculateStepsForFullCycle(xAxis, yAxis, zAxis);
	}

	private static void calculateEnergyOfSystem(List<Pair> xAxis, List<Pair> yAxis, List<Pair> zAxis) {
		for (int i = 1; i <= 1000; i++) {
			moveOneCycle(xAxis);
			moveOneCycle(yAxis);
			moveOneCycle(zAxis);
		}
		System.out.println("The total energy of the system after 1000 cycles: " + calculateEnergy(xAxis, yAxis, zAxis));
	}

	private static void moveOneCycle(List<Pair> axis) {
		axis.forEach(anAxis -> axis.stream().filter(otherAxis -> !otherAxis.equals(anAxis)).forEach(anAxis::recalculateVelocity));
		axis.forEach(Pair::applyVelocity);
	}

	private static int calculateEnergy(List<Pair> xAxis, List<Pair> yAxis, List<Pair> zAxis) {
		int energy = 0;
		for(int i = 0; i < xAxis.size(); i++) {
			energy += calculateEnergy(xAxis.get(i), yAxis.get(i), zAxis.get(i));
		}
		return energy;
	}

	private static int calculateEnergy(Pair x, Pair y, Pair z) {
		return (Math.abs(x.getPosition()) + Math.abs(y.getPosition()) + Math.abs(z.getPosition())) * (Math.abs(x.getVelocity()) + Math.abs(y.getVelocity()) + Math.abs(z.getVelocity()));
	}

	private static void calculateStepsForFullCycle(List<Pair> xAxis, List<Pair> yAxis, List<Pair> zAxis) {
		BigInteger stepsOfCompleteCycle = lcd(lcd(calculateStepsForFullCycle(xAxis), calculateStepsForFullCycle(yAxis)), calculateStepsForFullCycle(zAxis));
		System.out.println("it took " + stepsOfCompleteCycle + " to complete a full cycle");
	}

	private static BigInteger calculateStepsForFullCycle(List<Pair> axis) {
		List<Pair> origin = clone(axis);
		int cycleNumber = 0;
		while(true) {
			moveOneCycle(axis);
			cycleNumber++;
			if(isSame(axis, origin)) {
				return new BigInteger(String.valueOf(cycleNumber));
			}
		}
	}

	private static List<Pair> clone(List<Pair> axis) {
		return axis.stream().map(Pair::new).collect(Collectors.toList());
	}

	private static boolean isSame(List<Pair> axis, List<Pair> otherAxis) {
		for(int i = 0; i < axis.size(); i++) {
			if(!axis.get(i).isSame(otherAxis.get(i))) {
				return false;
			}
		}
		return true;
	}

	private static BigInteger lcd(BigInteger number1, BigInteger number2) {
		BigInteger gcd = number1.gcd(number2);
		BigInteger abs = number1.multiply(number2).abs();
		return abs.divide(gcd);
	}

}

class Pair {
	private int position;
	private int velocity = 0;

	Pair(int position) {
		this.position = position;
	}

	Pair(Pair pair) {
		this.position = pair.position;
		this.velocity = pair.velocity;
	}

	int getPosition() {
		return position;
	}

	int getVelocity() {
		return velocity;
	}

	void applyVelocity() {
		this.position += velocity;
	}

	void recalculateVelocity(Pair other) {
		if (other.position > this.getPosition()) {
			velocity++;
		} else if (other.position < this.getPosition()) {
			velocity--;
		}
	}

	boolean isSame(Pair pair) {
		return position == pair.position && velocity == pair.velocity;
	}

}
