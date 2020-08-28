package com.buerc;

public class Rule {
    //映射规则
    private int[] rule;
    //实际有效数的个数
    private int count;
    //特征值个数
    private int num;
    //最大序列值
    private long max;

    public Rule(int[] rule, int count) {
        this.rule = rule;
        this.count = count;
    }

    public Rule(int[] rule, int count, int num) {
        this.rule = rule;
        this.count = count;
        this.num = num;
        this.max = (long)Math.pow(10,count) - 1;
    }

    public int[] getRule() {
        return rule;
    }

    public void setRule(int[] rule) {
        this.rule = rule;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}
