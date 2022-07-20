package com.example.fluxtest.payload;

public class OrderedResult implements CalculationResult{
    private final Integer iterNum;
    private final String resultFirst;
    private final Integer timeFirst;
    private final Integer doneFirst;
    private final String resultSecond;
    private final Integer timeSecond;
    private final Integer doneSecond;

    public OrderedResult(Integer iterNum,
                         String resultFirst, Integer timeFirst, Integer doneFirst,
                         String resultSecond, Integer timeSecond, Integer doneSecond) {
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

    public String getResultFirst() {
        return resultFirst;
    }

    public Integer getTimeFirst() {
        return timeFirst;
    }

    public Integer getDoneFirst() {
        return doneFirst;
    }

    public String getResultSecond() {
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
