package day19;

import com.sun.tools.javac.util.Pair;

public class Puzzle19 {

	private static String input = "109,424,203,1,21102,11,1,0,1105,1,282,21102,1,18,0,1105,1,259,1201,1,0,221,203,1,21101,0,31,0,1105,1,282,21102,38,1,0,1106,0,259,21001,23,0,2,22101,0,1,3,21101,0,1,1,21102,1,57,0,1105,1,303,2101,0,1,222,21001,221,0,3,20102,1,221,2,21102,259,1,1,21101,80,0,0,1106,0,225,21101,0,111,2,21102,1,91,0,1105,1,303,2102,1,1,223,20101,0,222,4,21102,1,259,3,21102,1,225,2,21102,1,225,1,21101,0,118,0,1105,1,225,20101,0,222,3,21102,148,1,2,21102,1,133,0,1106,0,303,21202,1,-1,1,22001,223,1,1,21102,148,1,0,1106,0,259,2101,0,1,223,20102,1,221,4,21001,222,0,3,21101,0,17,2,1001,132,-2,224,1002,224,2,224,1001,224,3,224,1002,132,-1,132,1,224,132,224,21001,224,1,1,21101,0,195,0,106,0,109,20207,1,223,2,20102,1,23,1,21102,-1,1,3,21101,0,214,0,1105,1,303,22101,1,1,1,204,1,99,0,0,0,0,109,5,2102,1,-4,249,22101,0,-3,1,21202,-2,1,2,21202,-1,1,3,21102,1,250,0,1105,1,225,22102,1,1,-4,109,-5,2106,0,0,109,3,22107,0,-2,-1,21202,-1,2,-1,21201,-1,-1,-1,22202,-1,-2,-2,109,-3,2105,1,0,109,3,21207,-2,0,-1,1206,-1,294,104,0,99,22102,1,-2,-2,109,-3,2106,0,0,109,5,22207,-3,-4,-1,1206,-1,346,22201,-4,-3,-4,21202,-3,-1,-1,22201,-4,-1,2,21202,2,-1,-1,22201,-4,-1,1,21202,-2,1,3,21101,0,343,0,1105,1,303,1105,1,415,22207,-2,-3,-1,1206,-1,387,22201,-3,-2,-3,21202,-2,-1,-1,22201,-3,-1,3,21202,3,-1,-1,22201,-3,-1,2,21201,-4,0,1,21102,384,1,0,1106,0,303,1105,1,415,21202,-4,-1,-4,22201,-4,-3,-4,22202,-3,-2,-2,22202,-2,-4,-4,22202,-3,-2,-3,21202,-4,-1,-2,22201,-3,-2,1,21202,1,1,-4,109,-5,2106,0,0";

	private static final int BEAM_AREA = 50;
	public static final int SHIP_SIZE = 99;
	public static void main(String[] args) {
		System.out.println("In the 50x50 space closest to the emitter " + calculateAffectedSpace() + " points are affected");
		Pair<Long, Long> coordinate = findBeamCoordinatesToCoverShip();
		System.out.println("The beam will cover the whole ship on coordinates: (" + coordinate.fst + ", " + coordinate.snd + ")");
	}

	private static long calculateAffectedSpace() {
		long affected = 0;
		for(long x = 0; x < BEAM_AREA; x ++) {
			for(long y = 0; y < BEAM_AREA; y++) {
				if(isAffected(x, y)) {
					affected++;
				}
			}
		}
		return affected;
	}

	private static Pair<Long, Long> findBeamCoordinatesToCoverShip() {
		long y = 0;
		long x = SHIP_SIZE;
		while(true) {
			if(isAffected(x, y)) {
				if(isAffected(x - SHIP_SIZE, y + SHIP_SIZE)) {
					return new Pair<>( x - SHIP_SIZE, y);
				}
				x++;
			} else {
				y++;
			}
		}
	}

	private static boolean isAffected(long x, long y) {
		IntComputer intComputer = new IntComputer(input);
		intComputer.processInput(x);
		intComputer.processInput(y);
		return intComputer.getOutput().remove() == 1L;
	}
}
