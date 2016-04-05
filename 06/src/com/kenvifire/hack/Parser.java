package com.kenvifire.hack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Created by hannahzhang on 16/4/4.
 */
public class Parser {


    private String currentCommand;

    private BufferedReader br;

    private String symbol;

    private String label;

    private String dest;

    private String comp;

    private String jump;

    private CommandType commandType;

    public Parser (InputStream in) {
       br = new BufferedReader(new InputStreamReader(in));

    }

    public boolean hasMoreCommands() throws Exception{
        String line = br.readLine();
        while (line != null && (line.trim().length() == 0 || line.trim().startsWith("//"))) {
            line=br.readLine();
        }
        if(line == null) {
            return false;
        }
        currentCommand = line.trim().replaceAll("[ \t]+","");

        return true;

    }

    public void advance() {
        parseInst(currentCommand);
    }
    private void parseInst(String ins) {
       // System.out.println("parse instruction:[" + ins + "]");
        symbol = null;
        label = null;
        dest = null;
        jump = null;
        comp = null;
        commandType = null;
        //remove comment
        if(ins.contains("//")) {
            ins = ins.substring(0,ins.indexOf("//"));
        }
        //A @cons/label
        if(ins.startsWith("@")) {
            symbol = ins.substring(1);
            commandType = CommandType.A_COMMAND;
        }else if (ins.startsWith("(")) {
            commandType = CommandType.L_COMMAND;
            symbol = ins.substring(ins.indexOf("(") + 1,ins.indexOf(")"));
        }
        else {
            //System.out.println("C-->" + ins);
            commandType = CommandType.C_COMMAND;
            int destEnd = ins.indexOf('=');
            int compEnd = ins.indexOf(';');
            if(destEnd > 0) {
                dest = ins.substring(0, destEnd);
            }
            if (compEnd > 0) {
                if(destEnd > 0) {
                    comp = ins.substring(destEnd + 1, compEnd);
                }else {
                    comp = ins.substring(0, compEnd);
                }
                jump = ins.substring(compEnd+1);
            }else {
                comp = ins.substring(destEnd+1);
            }

        }

    }




    public CommandType commandType() {
        return commandType;
    }

    public String symbol() {
        return symbol;
    }

    public String dest() {
        return dest;
    }
    public String comp() {
        return comp;
    }

    public String jump() {
        return jump;
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

}
