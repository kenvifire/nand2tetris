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

    public Parser (InputStream in) {
       br = new BufferedReader(new InputStreamReader(in));

    }

    public boolean hasMoreCommands() throws Exception{
        String line = br.readLine();
        while (line != null && line.trim().length() == 0) {
            line=br.readLine();
        }
        if(line == null) {
            return false;
        }
        currentCommand = line.trim().replaceAll("[ \t]+","");
        return true;

    }

    private void parseInst(String ins) {
        symbol = null;
        label = null;
        dest = null;
        jump = null;
        comp = null;
        //A @cons/label
        if(ins.startsWith("@")) {
            symbol = ins.substring(1);
        }else {
            int destEnd = ins.indexOf('=');
            int compEnd = ins.indexOf(';');
            dest = ins.substring(0, destEnd);
            if (compEnd > 0) {
                comp = ins.substring(destEnd, compEnd);
                jump = ins.substring(compEnd);
            }

        }

    }




    public CommandType commandType() {
        return null;
    }

    public String symbol() {
        return "";
    }

    public String dest() {

    }
    public String comp() {

    }

    public String jump() {

    }

}
