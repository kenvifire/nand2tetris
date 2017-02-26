package com.itluobo;

/**
 * Created by kenvi on 17/2/18.
 */
public enum InstructionType {
    PUSH("push", 2),
    POP("pop", 2),
    ADD("add", 0),
    SUB("sub", 0),
    NEG("neg", 0 ),
    EQ("eq", 0),
    GT("gt", 0),
    LT("lt", 0),
    AND("and", 0),
    OR("or", 0),
    NOT("not", 0);

    private String ins;
    private int argCount;

    private InstructionType(String ins, int argCount) {
        this.ins = ins;
        this.argCount = argCount;
    }

    public String getIns() {
        return ins;
    }

    public int getArgCount() {
        return argCount;
    }

    public static InstructionType getByIns(String ins) {
        for (InstructionType type : values()) {
            if(type.ins.equals(ins)) {
                return type;
            }
        }
        return null;
    }
}
