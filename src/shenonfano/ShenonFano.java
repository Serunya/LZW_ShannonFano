package shenonfano;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

public class ShenonFano {
    //*** общее для кода и декода//
    private static ArrayList<FSymbol> pro;

    private static float getDeffirence(ArrayList<FSymbol> list, int middleInt) {
        float sumLeftPossibility = 0;
        for (int i = 0; i < middleInt; i++) {
            sumLeftPossibility += list.get(i).possibility;
        }
        float sumRightPossibility = 0;
        for (int i = middleInt; i < list.size(); i++) {
            sumRightPossibility += list.get(i).possibility;
        }
        return Math.abs(sumLeftPossibility - sumRightPossibility);
    }


    private static void generateCodes(ArrayList<FSymbol> list) {
        if (list.size() == 1) {
            list.get(0).code += "1";
            return;
        }
        if (list.size() == 2) {
            list.get(0).code += "1";
            list.get(1).code += "0";
            return;
        }
        for (int i = 1; i < list.size() - 1; i++) {
            if (getDeffirence(list, i) <= getDeffirence(list, i + 1)) {
                ArrayList<FSymbol> left = new ArrayList<>();
                ArrayList<FSymbol> right = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    left.add(list.get(j));
                    list.get(j).code += "1";
                }

                for (int j = i; j < list.size(); j++) {
                    right.add(list.get(j));
                    list.get(j).code += "0";
                }
                generateCodes(left);
                generateCodes(right);
                break;
            }
        }
    }

    /*encode file Кодирование файла*/
    public static void encodeFile(Path path) {
        File file = path.toAbsolutePath().toFile();
        pro = new ArrayList<>();
        TupleSet abc = new TupleSet(file);
        pro = abc.valueToProbability();
        generateCodes(pro);
        StringBuilder binaryString = new StringBuilder();
        try {
            FileReader reader = new FileReader(file);
            for (int f = reader.read(); f != -1; f = reader.read()) {
                binaryString.append(getCode(f));
            }
            binaryString.append(getCode('\u001a'));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File encodeFile = new File("src/encodeSF");
            OutputStreamWriter fileWriter = new OutputStreamWriter(
                    new FileOutputStream(encodeFile),
                    StandardCharsets.UTF_8.newEncoder()
            );
            for (FSymbol f : pro) {
                fileWriter.write(Integer.toBinaryString(f.symb) + "&" + f.possibility + "\n");
            }
            fileWriter.flush();
            binaryWrite(fileWriter, binaryString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCode(int f) {
        for (FSymbol fSymbol : pro) {
            if (f == fSymbol.symb)
                return fSymbol.code;
        }
        return "";
    }

    private static void binaryWrite(OutputStreamWriter fileWriter, String binaryString) {
        int nullCount = 7 - (binaryString.length() % 7);
        while (nullCount != 0) {
            binaryString += '0';
            nullCount--;
        }
        int countSymb = binaryString.length() / 7;
        StringBuilder charString = new StringBuilder();
        for (int i = 0; i < countSymb; i++) {
            charString.append((char) Integer.parseInt(binaryString.substring(i * 7, ((i * 7) + 7)), 2));
        }
        try {
            fileWriter.write("|\n");
            fileWriter.write(charString.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //*Декодирование -                   файла*//
    public static void decodeFile(Path path) throws IOException {
        File file = path.toAbsolutePath().toFile();
        pro = new ArrayList<>();
        String line = "";
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        while (!(line = fileReader.readLine()).equals("|")) {
            String[] f = line.split("&");
            pro.add(new FSymbol((char) Integer.parseInt(f[0], 2), Float.parseFloat(f[1])));
        }
        generateCodes(pro);
        String decodeLine = fileToBinaryString(fileReader);
        int startIndex = 0;
        int endIndex = 1;
        FileWriter writer = new FileWriter(new File("src/decodeSF"));
        char sad;
        while ((sad = isSymbol(decodeLine.substring(startIndex, endIndex))) != '\u001a') {
            if (sad == (char) -1) {
                endIndex++;
            } else {
                writer.write(sad);
                startIndex = endIndex;
                endIndex = startIndex + 1;
            }
        }
        writer.flush();
        writer.close();
    }


    private static char isSymbol(String line) {
        for (FSymbol f : pro) {
            if (f.code.equals(line)) {
                return f.symb;
            }
        }
        return (char) -1;
    }


    private static String fileToBinaryString(BufferedReader fileReader) throws IOException {
        StringBuilder decodeLine = new StringBuilder();
        int c;
        while ((c = fileReader.read()) != -1) {
            StringBuilder bitCode = new StringBuilder(Integer.toBinaryString(c));
            StringBuilder nullCode = new StringBuilder();
            while (nullCode.length() < 7 - bitCode.length()) {
                nullCode.append("0");
            }
            nullCode.append(bitCode);
            decodeLine.append(nullCode);
        }
        return decodeLine.toString();
    }

}