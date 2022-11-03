package shenonfano;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TupleSet {

    TupleSet(File file) {
        try {
            FileReader reader = new FileReader(file);
            for (int f = reader.read(); f != -1; f = reader.read()) {
                addElement((char) f);
            }
            addElement('\u001a');
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TupleSet() {
    }


    ArrayList<Pair> array = new ArrayList<>();
    int countSymbol = 0;

    public void addElement(char f) {
        countSymbol++;
        for (Pair pair : array) {
            if (pair.key == f) {
                pair.value++;
                return;
            }
        }
        array.add(new Pair(f, 1));
    }

    public ArrayList<FSymbol> valueToProbability() {
        ArrayList<FSymbol> symbolProbability = new ArrayList<>();
        Pair temp = array.remove(0);
        symbolProbability.add(new FSymbol(temp.key, temp.value / countSymbol));
        for (Pair pair : array) {
            boolean elemAdd = false;
            float probability = pair.value / countSymbol;
            for (int i = 0; i < symbolProbability.size(); i++) {
                if (symbolProbability.get(i).possibility < probability) {
                    symbolProbability.add(i, new FSymbol(pair.key, probability));
                    elemAdd = true;
                    break;
                }
            }
            if(!elemAdd)
                symbolProbability.add(new FSymbol(pair.key, probability));
        }
        return symbolProbability;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        for (Pair pair : array) {
            returnString.append("[ ").append(pair.key).append(" : ").append(pair.value).append(" ]\n");
        }
        returnString.append("count Sybmol = ").append(countSymbol);
        return returnString.toString();
    }
}
