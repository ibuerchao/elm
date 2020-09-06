package com.buerc.sys.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TransferVo {
    private String id;
    //所有值
    private List<TransferData> data;
    //已经赋予的值
    private Set<String> value;

    @Data
    public static class TransferData{
        private String key;
        private String label;
        private boolean disabled;
    }
}
