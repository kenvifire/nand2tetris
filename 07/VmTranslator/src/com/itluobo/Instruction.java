package com.itluobo;

/**
 * Created by kenvi on 17/2/18.
 */
public class Instruction {
    private InstructionType type;
    private String arg1;
    private String arg2;

    public Instruction(InstructionType type, String arg1, String arg2) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public InstructionType getType() {
        return type;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", type, arg1, arg2);
    }
}
