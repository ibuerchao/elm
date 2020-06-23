package com.buerc.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeUtil {
    private static final String ROOT = "root";

    /**
     * 指定节点为根节点构造树
     *
     * @param data 平铺的数据
     * @param r    指定根节点
     * @return 节点树
     */
    public static List<TreeNode> tree(List<TreeNode> data, String r) {
        if (StringUtils.isBlank(r)) {
            r = ROOT;
        }
        TreeNode root = new TreeNode(r);
        buildTree(root, data);
        return root.getChildren();
    }

    /**
     * 使用迭代构造树
     */
    private static void buildTree(TreeNode root, List<TreeNode> data) {
        Map<Boolean, List<TreeNode>> map = data.stream()
                .collect(Collectors.groupingBy(node -> root.getId().equals(node.getParentId())));
        List<TreeNode> childNodes = map.get(Boolean.TRUE);
        List<TreeNode> notChildNodes = map.get(Boolean.FALSE);
        if (CollectionUtils.isEmpty(childNodes)) {
            return;
        }
        root.setChildren(childNodes);
        if (!CollectionUtils.isEmpty(notChildNodes)) {
            for (TreeNode node : childNodes) {
                buildTree(node, notChildNodes);
            }
        }
    }
}