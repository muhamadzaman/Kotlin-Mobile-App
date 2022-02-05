package com.artpluslogic.weird32bitformatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class WeirdFormatter {

    /**
     * Encodes the provided string into 32bit format and returns a list of decimals for the encoded
     * data
     *
     * @param input
     * @return
     */
    public static ArrayList<Integer> encode(String input) {

        ArrayList<Integer> encodedDecimals = new ArrayList<>();
        ArrayList<String> splittedStringArray = splitStringBySize(input, 4);

        for (String chunk : splittedStringArray) {

            HashMap<Integer, Character> raw32BitData = new HashMap<>();

            char[] charArr = chunk.toCharArray();
            for (int i = 0; i < charArr.length; i++) {
                String binaryString = String.format("%8s", Integer.toBinaryString(charArr[i])).replace(" ", "0");
                for (int j = 0; j < binaryString.length(); j++) {
                    raw32BitData.put(((3 - i) * 8) + j, binaryString.charAt(j));
                }
            }

            if (raw32BitData.size() < 32) {
                int difference = 32 - raw32BitData.size();
                for (int i = 0; i < difference; i++) {
                    raw32BitData.put(i, '0');
                }
            }

            HashMap<Integer, Character> encoded32BitData = scrambleData(raw32BitData);
            String encodedBinaryString = encoded32BitData.values()
                    .stream()
                    .map(Object::toString)
                    .reduce("", String::concat);

            encodedDecimals.add(Integer.parseInt(encodedBinaryString, 2));
        }

        return encodedDecimals;
    }

    /**
     * Decodes the given list of encoded decimals and returns the string in original form
     *
     * @param encodedIntegers
     * @return
     */
    public static String decode(ArrayList<Integer> encodedIntegers) {
        StringBuilder decodedStringBuilder = new StringBuilder();
        String decodedString = "";

        for (int encodedInteger : encodedIntegers) {

            // 1- Convert the integer into binary string.
            StringBuilder encodedBinaryString = new StringBuilder(Integer.toBinaryString(encodedInteger));
            while (encodedBinaryString.length() < 32) {
                encodedBinaryString.insert(0, "0");
            }

            // 2- Convert the binary string into the array/hashmap
            HashMap<Integer, Character> encoded32BitData = new HashMap<>();
            char[] charArr = encodedBinaryString.toString().toCharArray();
            for (int i = 0; i < charArr.length; i++) {
                encoded32BitData.put(i, charArr[i]);
            }

            // 3- decode the hashmap
            HashMap<Integer, Character> decoded32BitData = unscrambleData(encoded32BitData);

            // 4- convert it into decoded binary string
            String decodedBinaryString = decoded32BitData.values()
                    .stream()
                    .map(Object::toString)
                    .reduce("", String::concat);

            // 5- convert into original string
            decodedBinaryString = decodedBinaryString.replaceAll("(.{8})", "$1 ");

            String raw = Arrays.stream(decodedBinaryString.split(" "))
                    .map(binary -> ((char) Integer.parseInt(binary, 2)))
                    .map(String::valueOf)
                    .collect(Collectors.joining()); // cut the space

            StringBuffer buffer = new StringBuffer(raw);
            decodedString = String.valueOf(buffer.reverse());
            decodedStringBuilder.append(decodedString);
        }

        return decodedStringBuilder.toString();
    }


    /**
     * Scrambles the raw binary data stored in the hashmap and returns the encoded data
     *
     * @param raw32BitData
     * @return HashMap<Integer, Character>
     */
    private static HashMap<Integer, Character> scrambleData(HashMap<Integer, Character> raw32BitData) {
        HashMap<Integer, Character> encoded32BitData = new HashMap<>();

        for (int i = 0, a = 0, b = a + 8, c = b + 8, d = c + 8;
             i < 8;
             i++, a++, b++, c++, d++) {
            encoded32BitData.put(encoded32BitData.size(), raw32BitData.get(a));
            encoded32BitData.put(encoded32BitData.size(), raw32BitData.get(b));
            encoded32BitData.put(encoded32BitData.size(), raw32BitData.get(c));
            encoded32BitData.put(encoded32BitData.size(), raw32BitData.get(d));
        }
        return encoded32BitData;
    }

    /**
     * Unscrambles the encoded binary data stored in the hashmap and returns the decoded data
     *
     * @param encoded32BitData
     * @return HashMap<Integer, Character>
     */
    private static HashMap<Integer, Character> unscrambleData(HashMap<Integer, Character> encoded32BitData) {
        HashMap<Integer, Character> decoded32BitData = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 4));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 8));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 12));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 16));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 20));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 24));
            decoded32BitData.put(decoded32BitData.size(), encoded32BitData.get(i + 28));
        }
        return decoded32BitData;
    }

    /**
     * Splits the given string into chunks of provided size
     *
     * @param str
     * @param size
     * @return ArrayList<String>
     */
    private static ArrayList<String> splitStringBySize(String str, int size) {
        ArrayList<String> split = new ArrayList<>();
        if (str.length() <= size) {
            split.add(str);
        } else {
            for (int i = 0; i <= str.length() / size; i++) {
                split.add(str.substring(i * size, Math.min((i + 1) * size, str.length())));
            }
        }
        return split;
    }
}
