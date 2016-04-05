package com.kenvifire.hack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hannahzhang on 16/4/4.
 */
public class Code {
    private static final Map<String,String> compCodeMap = new HashMap<String, String>();
    private static final Map<String,String> destCodeMap = new HashMap<String, String>();
    private static final Map<String,String> jumpCodeMap = new HashMap<String, String>();


    static {
        compCodeMap.put("0",  "0101010");
        compCodeMap.put("1",  "0111111");
        compCodeMap.put("-1", "0111010");
        compCodeMap.put("D",  "0001100");
        compCodeMap.put("A",  "0110000");
        compCodeMap.put("!D", "0001101");
        compCodeMap.put("!A", "0110001");
        compCodeMap.put("-D", "0001111");
        compCodeMap.put("-A", "0110011");
        compCodeMap.put("D+1","0011111");
        compCodeMap.put("A+1","0110111");
        compCodeMap.put("D-1","0001110");
        compCodeMap.put("A-1","0110010");
        compCodeMap.put("D+A","0000010");
        compCodeMap.put("D-A","0010011");
        compCodeMap.put("A-D","0000111");
        compCodeMap.put("D&A","0000000");
        compCodeMap.put("D|A","0010101");
        compCodeMap.put("M",  "1110000");
        compCodeMap.put("!M", "1110001");
        compCodeMap.put("-M", "1110011");
        compCodeMap.put("M+1","1110111");
        compCodeMap.put("M-1","1110010");
        compCodeMap.put("D+M","1000010");
        compCodeMap.put("D-M","1010011");
        compCodeMap.put("M-D","1000111");
        compCodeMap.put("D&M","1000000");
        compCodeMap.put("D|M","1010101");
    }

    static {
        destCodeMap.put(null,  "000");
        destCodeMap.put("M",   "001");
        destCodeMap.put("D",   "010");
        destCodeMap.put("MD",  "011");
        destCodeMap.put("A",   "100");
        destCodeMap.put("AM",  "101");
        destCodeMap.put("AD",  "110");
        destCodeMap.put("AMD", "111");
    }

    static {
        jumpCodeMap.put(null,  "000");
        jumpCodeMap.put("JGT", "001");
        jumpCodeMap.put("JEQ", "010");
        jumpCodeMap.put("JGE", "011");
        jumpCodeMap.put("JLT", "100");
        jumpCodeMap.put("JNE", "101");
        jumpCodeMap.put("JLE", "110");
        jumpCodeMap.put("JMP", "111");
    }

    public static String getCompCode(String comp) {
       return compCodeMap.get(comp);
    }

    public static String getDestCode(String dest) {
        return destCodeMap.get(dest);
    }

    public static String getJumpCode(String jump) {
        return jumpCodeMap.get(jump);
    }

}
