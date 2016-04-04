package com.kenvifire.hack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by hannahzhang on 16/4/4.
 */
public class Parser {

    private String currentCommand;

    private BufferedReader br;

    private String symbol;

    private String label;

    private String dest;

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
        currentCommand = line.trim();
        return true;

    }

    private void parseInst(String ins) {
        //A
        if(ins.startsWith("@")) {
            
        }else {

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
