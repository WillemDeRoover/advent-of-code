package _2020.day15;

import java.util.HashMap;
import java.util.Map;

public class Puzzle {

	public static void main(String[] args) {
		Map<Integer, Integer> numberPositionMap = new HashMap<>(Map.of(8, 1, 13, 2, 1, 3, 0,4 , 18, 5 ));
		int currentNumber = 9;
		int currentPos = 6;
		while(currentPos < 30000000) {
			if(!numberPositionMap.containsKey(currentNumber)) {
				numberPositionMap.put(currentNumber, currentPos++);
				currentNumber = 0;
			} else {
				int newNumber = currentPos - numberPositionMap.get(currentNumber);
				numberPositionMap.put(currentNumber, currentPos++);
				currentNumber = newNumber;
			}
		}
		System.out.println(currentNumber);
	}
}
