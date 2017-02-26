package com.itluobo;

import java.io.File;

/**
 * Created by kenvi on 17/2/18.
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/StackTest/StackTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/StackArithmetic/StackTest/StackTest.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/BasicTest/BasicTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/BasicTest/BasicTest.asm");
//        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/PointerTest/PointerTest.vm");
//        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/PointerTest/PointerTest.asm");
        File vmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/StaticTest/StaticTest.vm");
        File asmFile = new File("/Users/kenvi/code/study/nand2tetris/projects/07/MemoryAccess/StaticTest/StaticTest.asm");

        Parser parser = new Parser(vmFile);
        CodeWriter codeWriter = new CodeWriter(asmFile);

        while (parser.hasMore()) {
            codeWriter.writeInstruction(parser.nexInstruction());
        }

        parser.close();
        codeWriter.close();
    }
}
