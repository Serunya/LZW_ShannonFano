import lzw.LZW;
import shenonfano.ShenonFano;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        encodeFile("src/originalText");
        //decodeFile("src/encodeSF");
    }


    public static void encodeFile(String path) throws IOException {
        LZW.lzw_code(path);
        ShenonFano.encodeFile(Path.of("src/encodeLZW"));
    }

    public static void decodeFile(String path) throws IOException {
        ShenonFano.decodeFile(Path.of(path));
        LZW.lzw_decode("src/decodeSF");
    }



}
