package com.itluobo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by kenvi on 17/2/18.
 */
public class Parser {
    private BufferedReader br;
    private Instruction currentInstruction;

    public Parser(File file) throws Exception{
       br = new BufferedReader(new FileReader(file));
    }

    public boolean hasMore() throws  Exception {
        String line = null;
        while ((line = br.readLine()) != null) {
            currentInstruction = parser(line);
            if(currentInstruction == null) continue;
            else break;
        }
        return currentInstruction != null;
    }

    private Instruction parser(String line) {
        //comment
        line = line.trim();
        if(line.length() == 0) return null;
        else if(line.startsWith("//")) return null;
        String[] commandList = line.split(" ");

        return parserInstruction(commandList);
    }

    public Instruction nexInstruction(){
        Instruction result =  currentInstruction;
        currentInstruction = null;
        return result;
    }

    public void close() {
        try {
            if (br != null) {
                br.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Instruction parserInstruction(String[] commands) {
        InstructionType type = InstructionType.getByIns(commands[0]);
        if(type == null) throw new RuntimeException("invalid command:" + commands[0]);
        if(type.getArgCount() == 2) {
            return new Instruction(type, commands[1], commands[2] );
        }else {
            return new Instruction(type, null ,null);
        }
    }

}
