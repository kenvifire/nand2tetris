package com.itluobo;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenvi on 17/2/18.
 */
public class CodeWriter {
    private BufferedWriter bw;
    private int label = 0;
    private int temp = 5;
    private int static_seg = 24;

    private int constant_stack = 256;
    private int local_stack = 300;
    private int arg_stack = 400;
    private int this_stack = 3000;
    private int that_stack = 3010;

    private static final String THIS = "this";
    private static final String THAT = "that";
    private static final String TEMP = "temp";
    private static final String ARG = "argument";
    private static final String STATIC = "static";
    private static final String LOCAL = "local";


    private static final String RET_PREFIX = "ret_";
    private static final String FUN_PREFIX = "function_";
    private Map<String,Integer> functionReturnMap = new HashMap<>();




    public CodeWriter(File file) throws Exception {
        bw = new BufferedWriter(new FileWriter(file));
    }
    private void loadConstant(String constant) {
        writeAsm("@" + constant);
    }
    private void loadSP() {
        writeAsm("@SP");
    }


    public void writeInstruction(Instruction instruction) {
        //write comment
        writeAsm("//" + instruction.toString()) ;

        switch (instruction.getType()) {
            case PUSH:
            case POP:
                writePushPop(instruction);
                break;
            case LABEL:
                writeLabel(instruction.getArg1());
                break;
            case IF_GOTO:
                writeIfGoto(instruction.getArg1());
                break;
            case GOTO:
                writeGogo(instruction.getArg1());
                break;
            case CALL:
                writeCall(instruction.getArg1(), instruction.getArg2());
                break;
            case FUNCTION:
                writeFunction(instruction.getArg1(), instruction.getArg2());
                break;
            case RETURN:
                writeReturn();
                break;
            default:
                writeArithmetic(instruction);
                break;
        }
    }

    private void writeReturn() {
        // temp[0] = local
        loadSP(LOCAL);
        pushA();
        writeInstruction(new Instruction(InstructionType.POP, TEMP, "0"));

        //*ARG = pop()
        loadSP();
        writeAsm("A=M");
        writeAsm("D=M");
        decreaseSp();
        loadSP(ARG);
        writeAsm("M=A");
        writeAsm("M=D");


        //sp = arg + 1
        loadSP(ARG);
        writeAsm("D=A");
        loadSP();
        writeAsm("M=D+1");

        //THAT = *(endFrame - 1)
        writeInstruction(new Instruction(InstructionType.PUSH, TEMP, "0"));
        writePushConstant("1");
        writeInstruction(new Instruction(InstructionType.SUB, null, null));
        loadSP();
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("D=M");
        pushD();
        popSegPointer(THAT);

        writeInstruction(new Instruction(InstructionType.PUSH, TEMP, "0"));
        writePushConstant("1");
        writeInstruction(new Instruction(InstructionType.SUB, null, null));
        loadSP();
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("D=M");
        pushD();
        popSegPointer(THAT);

        writeInstruction(new Instruction(InstructionType.PUSH, TEMP, "0"));
        writePushConstant("2");
        writeInstruction(new Instruction(InstructionType.SUB, null, null));
        loadSP();
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("D=M");
        pushD();
        popSegPointer(THIS);

        writeInstruction(new Instruction(InstructionType.PUSH, TEMP, "0"));
        writePushConstant("3");
        writeInstruction(new Instruction(InstructionType.SUB, null, null));
        loadSP();
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("D=M");
        pushD();
        popSegPointer(ARG);


        writeInstruction(new Instruction(InstructionType.PUSH, TEMP, "0"));
        writePushConstant("4");
        writeInstruction(new Instruction(InstructionType.SUB, null, null));
        loadSP();
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("D=M");
        pushD();
        popSegPointer(LOCAL);


        writeInstruction(new Instruction(InstructionType.PUSH, TEMP, "0"));
        writePushConstant("3");
        writeInstruction(new Instruction(InstructionType.SUB, null, null));
        loadSP();
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("A=M");
        writeAsm("0;JMP");
    }


    private void pushA() {
        writeAsm("D=A");
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }

    private void pushD() {
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }
    private void writeFunction(String function, String nArgs) {
        writeLabel(getFunctionLabel(function));
        for (int i = 0; i < Integer.valueOf(nArgs); i++) {
            writePushConstant("0");
        }
    }

    private void writeCall(String function, String nArgs) {

        writeLabel(newReturnAddr(function));
        String returnLabel = RET_PREFIX + function;
        //push returnAddress
        writeAsm("@" + getCurrentReturnAddr(function) );
        writeAsm("D=A");
        loadSP();
        writeAsm("M=A");
        writeAsm("M=D");
        increaseSp();


        //push segments
        pushSegPointer(LOCAL);
        pushSegPointer(ARG);
        pushSegPointer(THIS);
        pushSegPointer(THAT);

        // arg = arg -5 - nArgs
        pushSegPointer(ARG);
        writePushConstant("5");
        writeArithmetic(new Instruction(InstructionType.SUB, null, null));
        writePushConstant(nArgs);
        writeArithmetic(new Instruction(InstructionType.SUB, null, null));
        popSegPointer(ARG);

        //LCL = SP
        loadSP();
        popSegPointer(LOCAL);


        // goto functionName
        writeGogo(FUN_PREFIX + function);

        //(return address)
        writeAsm("(" + returnLabel + ")");


    }

    private void pushSegPointer(String segType) {

        loadSP(segType);
        writeAsm("D=M");
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }

    private void popSegPointer(String segType) {
        decreaseSp();
        writeAsm("A=M");
        writeAsm("D=M");
        loadSP(segType);
        writeAsm("M=D");
    }

    private void writeIfGoto(String label) {
        //pop stack
        writeAsm("@SP");
        writeAsm("M=M-1");
        writeAsm("@SP");
        writeAsm("A=M");
        writeAsm("D=M");

        //load label
        writeAsm("@" + label);
        //jmp
        writeAsm("D;JGT");
    }

    private void writeGogo(String label) {

        //load label
        writeAsm("@" + label);
        //jmp
        writeAsm("0;JMP");
    }

    private void writeLabel(String label) {
        writeAsm("(" + label +")");
    }

    private void loadSP(String segType) {
        switch (segType) {
            case "this":
                writeAsm("@" + 3);
                break;
            case "that":
                writeAsm("@" + 4);
                break;
            case "temp":
                writeAsm("@" + temp);
                break;
            case "argument":
                writeAsm("@" + 2);
                break;
            case "static":
                writeAsm("@" + static_seg);
                break;
            case "local":
                writeAsm("@" + 1);
                break;
            default:
                throw new RuntimeException("invalid seg type:" + segType);
        }
    }


    private void writePushPop(Instruction instruction) {
        String stackType = instruction.getArg1();
        String constant = instruction.getArg2();
        switch (instruction.getType()) {
            case PUSH:
                if(stackType.equals("constant")){
                    writePushConstant(constant);
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

    private void writePopPointer(String pointer) {
        decreaseSp();
        writeAsm("A=M");
        writeAsm("D=M");
        if(pointer.equals("0")) {
            loadSP(THIS);
        }else {
            loadSP(THAT);
        }
        writeAsm("M=D");
    }

    private void writePushPointer(String pointer) {

        if(pointer.equals("0")) {
            loadSP(THIS);
        }else {
            loadSP(THAT);
        }
        writeAsm("D=M");
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }
    private void writePushConstant(String constant) {
        loadConstant(constant);
        writeAsm("D=A");
        loadSP();
        writeAsm("A=M");
        writeAsm("M=D");
        increaseSp();
    }

    private void writePopConstant(String constant) {
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


    private void increaseSp() {
        writeAsm("@SP");
        writeAsm("M=M+1");
    }
    private void decreaseSp() {
        writeAsm("@SP");
        writeAsm("M=M-1");
    }

    private void writeAsm(String asm) {
        try {
            bw.write(asm + "\n");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void writeArithmetic(Instruction instruction) {
        try {
            bw.write("@SP\n");
            bw.write("M=M-1\n");
            bw.write("A=M\n");

            if (instruction.getType() == InstructionType.NOT) {
                bw.write("D=!M\n");
                bw.write("@START_" + label + "\n");
                bw.write("0;JMP\n");
            } else if (instruction.getType() == InstructionType.NEG) {
                bw.write("D=-M\n");
                bw.write("@START_" + label + "\n");
                bw.write("0;JMP\n");
            } else {
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
                        bw.write("@BOOL_TRUE_" + label + "\n");
                        bw.write("D;JEQ\n");
                        break;
                    case GT:
                        bw.write("D=D-M\n");
                        bw.write("@BOOL_TRUE_" + label + "\n");
                        bw.write("D;JLT\n");
                        break;
                    case LT:
                        bw.write("D=D-M\n");
                        bw.write("@BOOL_TRUE_" + label + "\n");
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
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    public String newReturnAddr(String functionName) {
        Integer value = functionReturnMap.get(functionName);
        if(value == null) {
            functionReturnMap.put(functionName, 1);
        }else {
            value = value + 1;
            functionReturnMap.put(functionName, value);
        }

        return FUN_PREFIX + functionName +"." + value;

    }

    public String getCurrentReturnAddr(String functionName) {
        Integer value = functionReturnMap.get(functionName);
        if(value == null) {
            throw new RuntimeException("cannot get value for " + functionName);
        }

        return FUN_PREFIX + functionName + "." + value;
    }

    public String getFunctionLabel(String functionName) {
        return FUN_PREFIX + functionName;
    }


}
