package lzw;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {
    public static void lzw_code(String path) throws IOException {
        String text = RW.read(path);

        int dictSize = 2048;
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put(String.valueOf((char) i), i);
        }

        String foundChars = "";
        List<Integer> result = new ArrayList<>();
        File encodeFile = new File("src/log");
        for (char character : text.toCharArray()) {
            String charsToAdd = foundChars + character;
            if (dictionary.containsKey(charsToAdd)) {
                foundChars = charsToAdd;
            } else {
                result.add(dictionary.get(foundChars));
                dictionary.put(charsToAdd, dictSize++);
                foundChars = String.valueOf(character);
            }
        }
        if (!foundChars.isEmpty()) {
            result.add(dictionary.get(foundChars));
        }

        StringBuilder res = new StringBuilder();
        for (Integer integer : result) {
            res.append(integer).append(" ");
        }


        RW.write(res.toString(),"src/encodeLZW");
    }

    public static void lzw_decode(String path) throws IOException {
        String text = RW.read(path);
        int dictSize = 2048;
        String[] nums = text.split(" ");
        List<Integer> encodedText = new ArrayList<>();
        for (String num : nums) {
            encodedText.add(Integer.valueOf(num));
        }

        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put(i, String.valueOf((char) i));
        }

        String characters = String.valueOf((char) encodedText.remove(0).intValue());
        StringBuilder result = new StringBuilder(characters);

        for (int code : encodedText) {
            String entry = dictionary.containsKey(code)
                    ? dictionary.get(code)
                    : characters + characters.charAt(0);
            dictionary.put(dictSize++, characters + entry.charAt(0));
            result.append(entry);
            characters = entry;
        }

        RW.write(result.toString(),"src/decodeLZW");
    }
}
