package com.kenvifire.hack;


import java.io.*;

/**
 * Created by hannahzhang on 16/4/4.
 */
public class Main {
    public static void main(String [] args) throws Exception{
        String file = "/Users/kenvi/code/study/nand2tetris/projects/06/pong/Pong";

        FileInputStream fin = new FileInputStream(file + ".asm");
        Parser parser = new Parser(fin);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file + ".hack")));
        SymbolTable symbolTable = new SymbolTable();



        int insLine = 0;
        //first time, parse symbols
        while (parser.hasMoreCommands()) {
            parser.advance();
           // System.out.println(String.format("[1]%s,%s,%s,%s,%s",parser.commandType(),parser.symbol(),parser.dest(),parser.comp(),parser.jump()));
            CommandType type = parser.commandType();
            if(type == CommandType.L_COMMAND ) {
                symbolTable.addEntry(parser.symbol(),insLine);
            }else {
                insLine++;
            }
        }

        //second time, emit instructions

        fin.close();
        fin = new FileInputStream(file + ".asm");
        parser = new Parser(fin);

        while (parser.hasMoreCommands()) {
            parser.advance();
           // System.out.println(String.format("[2]%s,%s,%s,%s,%s",parser.commandType(),parser.symbol(),parser.dest(),parser.comp(),parser.jump()));
            CommandType type = parser.commandType();
            String instruction ="";
            if (type == CommandType.A_COMMAND) {
                Integer address;
                String symbol = parser.symbol();
                if(!isNumeric(symbol)) {
                    address = symbolTable.getAddress(symbol);
                }else {
                    address = Integer.valueOf(symbol);
                }
                String addressStr = Integer.toBinaryString(address);
                int padCount = 16 - addressStr.length();
                StringBuilder padStr = new StringBuilder();
                while(padCount > 0) {
                    padStr.append("0");
                    padCount--;
                }
                instruction =  padStr.toString() + addressStr;

            }else if (type == CommandType.C_COMMAND) {
                String comp = parser.comp();
                String dest = parser.dest();
                String jump = parser.jump();
                instruction = "111" + Code.getCompCode(comp) + Code.getDestCode(dest) + Code.getJumpCode(jump);
            }else {
                continue;
            }
            if(instruction.length() != 16) {
                throw new RuntimeException("invalid instruction:" + instruction +":" + parser.getCurrentCommand());
            }
            System.out.println(instruction);
            bw.write(instruction);
            bw.newLine();
        }


        fin.close();
        bw.close();

    }

    private static boolean isNumeric(String str) {
        for(int i=0; i< str.length(); i++)  {
            char c = str.charAt(i);
            if(c<'0' || c>'9') {
                return false;
            }
        }

        return true;

    }
}
