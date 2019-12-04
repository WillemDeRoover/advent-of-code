package day4;

public class Puzzle4 {

	private static int start = 168630;
	private static int stop = 718098;

	public static void main(String[] args) {
		int passwordsWithinRange = 0;
		for (int i = start; i <= stop; i++) {
			int[] passwordArray = splitPassword(i);
			if (containsAdjacantDigits(passwordArray) && neverDecreases(passwordArray)) {
				passwordsWithinRange++;
			}
		}
		System.out.println("Passwords within range:" + passwordsWithinRange);

		int passwordsWithinRangeWithOnly2AdjacantsDigits = 0;
		for (int i = start; i <= stop; i++) {
			int[] passwordArray = splitPassword(i);
			if (containsOnlyTwoAdjacantDigits(passwordArray) && neverDecreases(passwordArray)) {
				passwordsWithinRangeWithOnly2AdjacantsDigits++;
			}
		}
		System.out.println("Passwords within range with only two adjacant digits:" + passwordsWithinRangeWithOnly2AdjacantsDigits);
	}

	private static int[] splitPassword(int password) {
		String passwordString = String.valueOf(password);
		int[] passwordArray = new int[passwordString.length()];
		for (int i = 0; i < passwordString.length(); i++) {
			passwordArray[i] = Integer.parseInt(passwordString.substring(i, i + 1));
		}
		return passwordArray;
	}

	private static boolean containsAdjacantDigits(int[] passwordArray) {
		for (int i = 0; i < passwordArray.length - 1; i++) {
			if (passwordArray[i] == passwordArray[i + 1]) {
				return true;
			}
		}
		return false;
	}

	private static boolean containsOnlyTwoAdjacantDigits(int[] passwordArray) {
		for (int i = 0; i < passwordArray.length - 1; i++) {
			if (passwordArray[i] == passwordArray[i + 1]) {
				boolean matchesBefore = false;
				boolean matchesAfter = false;
				if (i - 1 >= 0 && passwordArray[i] == passwordArray[i - 1]) {
					matchesBefore = true;
				}

				if (i + 2 <= passwordArray.length - 1 && passwordArray[i] == passwordArray[i + 2]) {
					matchesAfter = true;
				}

				if (!matchesBefore && !matchesAfter) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean neverDecreases(int[] passwordArray) {
		for (int i = 0; i < passwordArray.length - 1; i++) {
			if (passwordArray[i] > passwordArray[i + 1]) {
				return false;
			}
		}
		return true;
	}
}
