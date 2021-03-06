package com.kenvifire.hack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hannahzhang on 16/4/4.
 */
public class SymbolTable {

    private Map<String, Integer> symbolTable;

    private int currentAddress =0;

    public SymbolTable() {
        symbolTable = new HashMap<String, Integer>();
        init();
    }

    private void init() {
        symbolTable.put("SP",    0);
        symbolTable.put("LCL",   1);
        symbolTable.put("ARG",   2);
        symbolTable.put("THIS",  3);
        symbolTable.put("THAT",  4);
        symbolTable.put("R0",    0);
        symbolTable.put("R1",    1);
        symbolTable.put("R2",    2);
        symbolTable.put("R3",    3);
        symbolTable.put("R4",    4);
        symbolTable.put("R5",    5);
        symbolTable.put("R6",    6);
        symbolTable.put("R7",    7);
        symbolTable.put("R8",    8);
        symbolTable.put("R9",    9);
        symbolTable.put("R10",   10);
        symbolTable.put("R11",   11);
        symbolTable.put("R12",   12);
        symbolTable.put("R13",   13);
        symbolTable.put("R14",   14);
        symbolTable.put("R15",   16);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD",    24576);

        currentAddress = 17;
    }

    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        Integer address = symbolTable.get(symbol);
        if(address == null) {
            address = currentAddress++;
            symbolTable.put(symbol, address);
        }
        return address;
    }
}
