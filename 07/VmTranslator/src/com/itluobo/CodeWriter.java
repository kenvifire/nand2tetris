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
    private int stack_start = 0;
    private int local_start = 1;
    private int arg_start = 2;
    private int this_start = 3;
    private int that_start = 4;
    private int temp = 5;



    public CodeWriter(File file) throws Exception {
        bw = new BufferedWriter(new FileWriter(file));

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

    private void writePushPop(Instruction instruction) throws Exception {
        String stackType = instruction.getArg1();
        String constant = instruction.getArg2();
        switch (instruction.getType()) {
            case PUSH:
                bw.write("@" + constant + "\n");
                bw.write("D=A\n");
                loadSp(stackType);
                writeAsm("A=M");
                bw.write("M=D\n");
                loadSp(stackType);
                increaseSp(stackType);
                break;
            case POP:
                loadSp(stackType);
                decreaseSp(stackType);
                writeAsm("D=M");
                loadSp(instruction.getArg1());
                writeAsm("M=D");
                break;
        }

    }

    private void loadSp(String stackType) throws Exception{
        writeAsm("@SP");
        switch (stackType) {
            case "local":
                writeAsm("A=A+" + local_start);
                break;
            case "argument":
                writeAsm("A=A+" + arg_start);
                break;
            case "this":
                writeAsm("A=A+" + this_start);
                break;
            case "that":
                writeAsm("A=A+" + that_start);
                break;
            case "temp":
                writeAsm("A=A+" + temp);
                break;
            case "constant"  :
                break;
        }
    }

    private void increaseSp(String stackType) throws Exception {
        switch (stackType) {
            case "local":
            case "argument":
            case "this":
            case "that":
            case "constant":
                writeAsm("M=M+1");
                break;
            case "temp":
                temp++;
                break;
        }
    }

    private void decreaseSp(String stackType) throws Exception {
        switch (stackType) {
            case "local":
            case "argument":
            case "this":
            case "that":
            case "constant":
                writeAsm("M=M-1");
                break;
            case "temp":
                temp--;
                break;
        }
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
