package com.example.fluxtest.payload;

public class UnorderedResult implements CalculationResult {
    private final Integer iterNum;
    private final Integer funcNum;
    private final String funcResult;
    private final Integer funcTime;

    public UnorderedResult(Integer iterNum, Integer funcNum, String funcResult, Integer funcTime) {
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

    public String getFuncResult() {
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
