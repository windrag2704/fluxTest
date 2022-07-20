package com.example.fluxtest.model;

public class FuncResult {
    private final Integer iterNum;
    private final String result;
    private final Long time;

    private Integer funcNum;

    public FuncResult(Integer iterNum, String result, Long time) {
        this.iterNum = iterNum;
        this.result = result;
        this.time = time;
    }

    public Integer getIterNum() {
        return iterNum;
    }

    public Integer getFuncNum() {
        return funcNum;
    }

    public String getResult() {
        return result;
    }

    public Long getTime() {
        return time;
    }

    public FuncResult setFuncNum(Integer funcNum) {
        this.funcNum = funcNum;
        return this;
    }
}
