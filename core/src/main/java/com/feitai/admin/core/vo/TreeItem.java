package com.feitai.admin.core.vo;

import lombok.Data;

import java.util.List;

/**
 * 树结构
 */
@Data
public class TreeItem {

    private Long id;

    private String text;

    private List<TreeItem> children;

    private Long parentId;

    private Integer level;

    private String cls;//图标样式

    private boolean expanded = true;

    private String value;//用于存放其他值

}
