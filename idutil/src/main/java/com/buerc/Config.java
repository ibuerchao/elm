package com.buerc;

public class Config {
    //key
    private String key;
    //映射规则
    private String pattern;
    //每次缓存数量
    private int cacheSize;
    //剩余阈值：低于此值时异步获取下一组值防止发生抖动
    private int leftSize;
    //当前剩余数量
    private long left;
    //当前值
    private long currentValue;
    //下一组起始值(当低于阈值时才更新此值)
    private long nextValue = -1;
    //到达最大序列号之后是否循环取值
    private boolean cycle;

    public Config(String key, String pattern, int cacheSize, int leftSize, boolean cycle) {
        this.key = key;
        this.pattern = pattern;
        this.cacheSize = cacheSize;
        this.leftSize = leftSize;
        this.cycle = cycle;
        this.left = cacheSize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getLeftSize() {
        return leftSize;
    }

    public void setLeftSize(int leftSize) {
        this.leftSize = leftSize;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }

    public long getNextValue() {
        return nextValue;
    }

    public void setNextValue(long nextValue) {
        this.nextValue = nextValue;
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }
}
