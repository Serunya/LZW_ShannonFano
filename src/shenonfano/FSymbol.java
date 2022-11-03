package shenonfano;

import java.util.BitSet;

public class FSymbol {
    char symb;
    float possibility;
    String code = "";

    public FSymbol(char symb, float possibility) {
        this.symb = symb;
        this.possibility = possibility;
    }

    public FSymbol(char symb,String code){
        this.symb = symb;
        this.code = code;
    }

}
