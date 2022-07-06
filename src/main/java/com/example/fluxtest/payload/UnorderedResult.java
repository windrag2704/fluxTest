package com.example.fluxtest.payload;

public class UnorderedResult implements CalculationResult {
    private Integer iterNum;
    private Integer funcNum;
    private Integer funcResult;
    private Integer funcTime;

    public UnorderedResult(Integer iterNum, Integer funcNum, Integer funcResult, Integer funcTime) {
        this.iterNum = iterNum;
        this.funcNum = funcNum;
        this.funcResult = funcResult;
        this.funcTime = funcTime;
    }

    public Integer getIterNum() {
        return iterNum;
    }

    public Integer getFuncNum() {
        return funcNum;
    }

    public Integer getFuncResult() {
        return funcResult;
    }

    public Integer getFuncTime() {
        return funcTime;
    }

    @Override
    public String toCSV() {
        return iterNum + "," + funcNum + "," + funcResult + "," + funcTime;
    }
}
