package com.itluobo;

import java.io.File;

/**
 * Created by kenvi on 17/2/18.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String dirPrefix = "/Users/hannahzhang/code/";
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/StackTest/StackTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/StackTest/StackTest.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/BasicTest/BasicTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/BasicTest/BasicTest.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/PointerTest/PointerTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/PointerTest/PointerTest.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/StaticTest/StaticTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/StaticTest/StaticTest.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/08/ProgramFlow/BasicLoop/BasicLoop.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/08/ProgramFlow/BasicLoop/BasicLoop.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/08/ProgramFlow/FibonacciSeries/FibonacciSeries.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/08/ProgramFlow/FibonacciSeries/FibonacciSeries.asm");
//        File vmFile = new File(dirPrefix + "nand2tetris/projects/08/FunctionCalls/SimpleFunction/SimpleFunction.vm");
//        File asmFile = new File(dirPrefix + "nand2tetris/projects/08/FunctionCalls/SimpleFunction/SimpleFunction.asm");
        File vmFile = new File(dirPrefix + "nand2tetris/projects/08/FunctionCalls/NestedCall/NestedCall.vm");
        File asmFile = new File(dirPrefix + "nand2tetris/projects/08/FunctionCalls/NestedCall/NestedCall.asm");

        Parser parser = new Parser(vmFile);
        CodeWriter codeWriter = new CodeWriter(asmFile);

        while (parser.hasMore()) {
            codeWriter.writeInstruction(parser.nexInstruction());
        }

        parser.close();
        codeWriter.close();
    }
}
