package _2020.day25;

public class Puzzle {

	public static final long DOOR_PUBLIC_KEY = 8987316;
	public static final long CARD_PUBLIC_KEY = 14681524;

	public static void main(String[] args) {
		long publicKey = 1;
		int loopSize = 0;
		while(publicKey != DOOR_PUBLIC_KEY) {
			publicKey *= 7;
			publicKey %= 20201227;
			loopSize++;
		}

		long encryptionKey = 1;
		for (int i = 0; i < loopSize; i++) {
			encryptionKey *= CARD_PUBLIC_KEY;
			encryptionKey %= 20201227;
		}

		System.out.println(encryptionKey);
	}

}
