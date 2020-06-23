package com.buerc.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeNode {
    private String id;
    private String parentId;
    private String label;
    private List<TreeNode> children;

    public TreeNode(String id) {
        this.id = id;
    }

    public TreeNode(String id, String name, String parentId) {
        this.id = id;
        this.parentId = parentId;
        this.label = name;
    }

    public TreeNode(String id, String name, TreeNode parent) {
        this.id = id;
        this.parentId = parent.getId();
        this.label = name;
    }
}