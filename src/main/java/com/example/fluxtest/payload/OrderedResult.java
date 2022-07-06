package com.example.fluxtest.payload;

public class OrderedResult implements CalculationResult{
    private Integer iterNum;
    private Integer resultFirst;
    private Integer timeFirst;
    private Integer doneFirst;
    private Integer resultSecond;
    private Integer timeSecond;
    private Integer doneSecond;

    public OrderedResult(Integer iterNum,
                         Integer resultFirst, Integer timeFirst, Integer doneFirst,
                         Integer resultSecond, Integer timeSecond, Integer doneSecond) {
        this.iterNum = iterNum;
        this.resultFirst = resultFirst;
        this.timeFirst = timeFirst;
        this.doneFirst = doneFirst;
        this.resultSecond = resultSecond;
        this.timeSecond = timeSecond;
        this.doneSecond = doneSecond;
    }

    public Integer getIterNum() {
        return iterNum;
    }

    public Integer getResultFirst() {
        return resultFirst;
    }

    public Integer getTimeFirst() {
        return timeFirst;
    }

    public Integer getDoneFirst() {
        return doneFirst;
    }

    public Integer getResultSecond() {
        return resultSecond;
    }

    public Integer getTimeSecond() {
        return timeSecond;
    }

    public Integer getDoneSecond() {
        return doneSecond;
    }

    @Override
    public String toCSV() {
        return iterNum + "," + resultFirst + "," + timeFirst + "," + doneFirst + "," +
                resultSecond + "," + timeSecond + "," + doneSecond;
    }
}
