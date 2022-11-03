package lzw;

import java.io.*;

public class RW {
    public static String read(String address) {
        StringBuilder text = new StringBuilder();
        try(FileReader reader = new FileReader(address))
        {
            int c;
            while((c=reader.read())!=-1){

                text.append((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        return text.toString();
    }

    public static void write(String text, String address) {
        try(FileWriter writer = new FileWriter(address))
        {
            writer.write(text);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public static String fileToBinaryString(String address) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(address));
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

    public static void binaryWrite(String address, String binaryString) throws FileNotFoundException {
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(address));
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
}
