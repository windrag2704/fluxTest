package com.example.fluxtest.model;

public class FuncResult {
    private Integer iterNum;
    private Long result;
    private Long time;

    private Integer funcNum;

    public FuncResult(Integer iterNum, Long result, Long time) {
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

    public Long getResult() {
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
