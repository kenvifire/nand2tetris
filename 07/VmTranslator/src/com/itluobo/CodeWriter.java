package com.itluobo;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by kenvi on 17/2/18.
 */
public class CodeWriter {
    private BufferedWriter bw;
    private int label = 0;
    private int temp = 5;
    private int static_seg = 24;
    private int SP = 0;
    private int LOCAL = 1;
    private int ARG = 2;
    private int THIS = 3;
    private int THAT = 4;

    private int constant_stack = 256;
    private int local_stack = 300;
    private int arg_stack = 400;
    private int this_stack = 3000;
    private int that_stack = 3010;




    public CodeWriter(File file) throws Exception {
        bw = new BufferedWriter(new FileWriter(file));
    }
    private void loadConstant(String constant) throws Exception {
        writeAsm("@" + constant);
    }
    private void loadSP() throws Exception {
        writeAsm("@SP");
    }


    public void writeInstruction(Instruction instruction) throws Exception {
        //write comment
        bw.write("//" + instruction.toString() + "\n");

        switch (instruction.getType()) {
            case PUSH:
            case POP:
                writePushPop(instruction);
                break;
            default:
                writeArithmetic(instruction);
                break;
        }
    }

    private void loadSP(String segType) throws Exception {
        switch (segType) {
            case "this":
                writeAsm("@" + THIS);
                break;
            case "that":
                writeAsm("@" + THAT);
                break;
            case "temp":
                writeAsm("@" + temp);
                break;
            case "argument":
                writeAsm("@" + ARG);
                break;
            case "static":
                writeAsm("@" + static_seg);
                break;
        }
    }


    private void writePushPop(Instruction instruction) throws Exception {
        String stackType = instruction.getArg1();
        String constant = instruction.getArg2();
        switch (instruction.getType()) {
            case PUSH:
                if(stackType.equals("constant")){
                    writePushContant(constant);
                }else if(stackType.equals("pointer")) {
                    writePushPointer(constant);
                }
                else {
//                    loadSpOffset(stackType, Integer.valueOf(instruction.getArg2()));
//                    bw.write("D=M\n");
//                    loadSP();
//                    writeAsm("A=M");
//                    writeAsm("M=D");
//                    increaseSp();

                    //D = segPointer
                    loadSP(instruction.getArg1());
                    writeAsm("D=M");

                    //add param 2
                    writeAsm("@" + instruction.getArg2());
                    writeAsm("D=D+A");

                    //load value
                    writeAsm("A=D");
                    writeAsm("D=M");

                    //write value
                    writeAsm("@SP");
                    writeAsm("A=M");
                    writeAsm("M=D");
                    writeAsm("@SP");
                    writeAsm("M=M+1");


                }
                break;
            case POP:
                if(stackType.equals("constant")) {
                    writePopConstant(constant);
                }else if(stackType.equals("pointer")){
                    writePopPointer(constant);
                }else {
                    //D = segPointer
                    loadSP(instruction.getArg1());
                    writeAsm("D=M");

                    //add param 2
                    writeAsm("@" + instruction.getArg2());
                    writeAsm("D=D+A");

                    //load sp
                    writeAsm("@SP");
                    writeAsm("A=M");
                    writeAsm("M=D");

                    //load value
                    writeAsm("@SP");
                    writeAsm("M=M-1");
                    writeAsm("@SP");
                    writeAsm("A=M");
                    writeAsm("D=M");

                    //load addr
                    writeAsm("@SP");
                    writeAsm("M=M+1");
                    writeAsm("@SP");
                    writeAsm("A=M");
                    writeAsm("A=M");
                    writeAsm("M=D");
                    writeAsm("@SP");
                    writeAsm("M=M-1");

                }
                break;
        }

    }

    private void writePopPointer(String pointer) throws Exception {
        decreaseSp();
        writeAsm("A=M");
        writeAsm("D=M");
        if(pointer.equals("0")) {
            writeAsm("@" + THIS);
        }else {
            writeAsm("@" + THAT);
        }
        writeAsm("M=D");
    }

    private void writePushPointer(String pointer) throws Exception {

        if(pointer.equals("0")) {
           writeAsm("@" + THIS);
        }else {
           writeAsm("@" + THAT);
        }
        writeAsm("D=M");
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }
    private void writePushContant(String constant) throws Exception {
        loadConstant(constant);
        writeAsm("D=A");
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }

    private void writePopConstant(String constant) throws Exception {
//        decreaseSp();
    }

    private void loadSpOffset(String stackType, int offset) throws Exception {
        int baseAddr = 0;
        switch (stackType) {
            case "local":
                baseAddr = local_stack;
                break;
            case "argument":
                baseAddr = arg_stack;
                break;
            case "this":
                baseAddr = this_stack;
                break;
            case "that":
                baseAddr = that_stack;
                break;
            case "temp":
                baseAddr = temp;
                break;
            case "constant"  :
                baseAddr = constant_stack;
                offset=0;
                break;
        }
        writeAsm("@" + (baseAddr + offset));
    }


    private void increaseSp() throws Exception {
        writeAsm("@SP");
        writeAsm("M=M+1");
    }
    private void decreaseSp() throws Exception {
        writeAsm("@SP");
        writeAsm("M=M-1");
    }

    private void writeAsm(String asm) throws Exception {
        bw.write(asm + "\n");

    }
    private void writeArithmetic(Instruction instruction) throws Exception {
        bw.write("@SP\n");
        bw.write("M=M-1\n");
        bw.write("A=M\n");

        if(instruction.getType() == InstructionType.NOT){
            bw.write("D=!M\n");
            bw.write("@START_" + label + "\n");
            bw.write("0;JMP\n");
        }else if(instruction.getType() == InstructionType.NEG) {
            bw.write("D=-M\n");
            bw.write("@START_" + label + "\n");
            bw.write("0;JMP\n");
        }else {
            bw.write("D=M\n");
            bw.write("@SP\n");
            bw.write("M=M-1\n");
            bw.write("A=M\n");

            switch (instruction.getType()) {
                case ADD:
                    bw.write("D=D+M\n");
                    bw.write("@START_" + label + "\n");
                    bw.write("0;JMP\n");
                    break;
                case SUB:
                    bw.write("D=M-D\n");
                    bw.write("@START_" + label + "\n");
                    bw.write("0;JMP\n");
                    break;
                case EQ:
                    bw.write("D=D-M\n");
                    bw.write("@BOOL_TRUE_" + label +"\n");
                    bw.write("D;JEQ\n");
                    break;
                case GT:
                    bw.write("D=D-M\n");
                    bw.write("@BOOL_TRUE_" + label +"\n");
                    bw.write("D;JLT\n");
                    break;
                case LT:
                    bw.write("D=D-M\n");
                    bw.write("@BOOL_TRUE_" + label +"\n");
                    bw.write("D;JGT\n");
                    break;
                case AND:
                    bw.write("D=D&M\n");
                    bw.write("@START_" + label + "\n");
                    bw.write("0;JMP\n");
                    break;
                case OR:
                    bw.write("D=D|M\n");
                    bw.write("@START_" + label + "\n");
                    bw.write("0;JMP\n");
                    break;
            }
        }
        //false
        bw.write("(BOOL_FALSE_" + label + ")\n");
        bw.write("D=0\n");
        bw.write("@START_" + label + "\n");
        bw.write("0;JMP\n");
        //true
        bw.write("(BOOL_TRUE_" + label + ")\n");
        bw.write("D=-1\n");
        bw.write("@START_" + label + "\n");
        bw.write("0;JMP\n");


        bw.write("(START_" + label + ")\n");
        bw.write("@SP\n");
        bw.write("A=M\n");
        bw.write("M=D\n");
        bw.write("@SP\n");
        bw.write("M=M+1\n");
        label++;

    }

    public void close() {
        try {
            if(bw != null) {
                bw.flush();
                bw.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
