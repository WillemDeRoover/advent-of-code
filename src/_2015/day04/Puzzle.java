package _2015.day04;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

public class Puzzle {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        IntStream.iterate(0, i -> i + 1)
                .filter(i -> isCorrectMD5Hash("iwrupvqb" + i))
                .findFirst().ifPresent(System.out::println);
    }

    private static boolean isCorrectMD5Hash(String input) {
        try {
            byte[] md5Hash = MessageDigest.getInstance("MD5").digest(input.getBytes());
            String hashtext = new BigInteger(1, md5Hash).toString(16);
            hashtext = String.format("%32s", hashtext).replace(' ', '0');
            return hashtext.startsWith("000000");
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

    }


}
