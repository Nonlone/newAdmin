package com.feitai.admin.system.vo;

import com.feitai.admin.system.model.Resource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Menu {

    /**
     * 一二三级菜单都要
     */
    private String id;

    /**
     * 一级菜单
     */
    private String icon;

    /**
     * 一级菜单
     */
    private String homePage;

    /**
     * 一级菜单
     */
    private List<Menu> menu;

    /**
     * 二级菜单
     */
    private List<Menu> items;

    private String text;

    /**
     * 二级菜单
     */
    private Boolean collapsed;

    /**
     * 三级菜单
     */
    private String href;

    private int order;

    private Long parentId;

    public static Menu build(int level, Resource resource, String ctx) {
        Menu menu = new Menu();
        menu.setId(resource.getCode());
        menu.setText(resource.getName());
        if (resource.getOrderId() != null) menu.setOrder(resource.getOrderId());

        Resource parent = resource.getParent();
        //menu.setHomePage(ctx+"/"+resource.getUrl());
        if (parent != null) {
            menu.setParentId(parent.getId());
        }
        if (level == 1) {
            menu.setIcon(resource.getIco());
            menu.setMenu(new ArrayList<Menu>());
            menu.setHomePage(resource.getUrl());
        } else if (level == 2) {
            menu.setCollapsed(false);
            menu.setHomePage(resource.getUrl());
            menu.setItems(new ArrayList<Menu>());
            menu.setIcon(resource.getIco());
        } else if (level == 3) {
            menu.setHref(ctx + resource.getUrl());
        }
        return menu;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((href == null) ? 0 : href.hashCode());
        result = prime * result + ((icon == null) ? 0 : icon.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((menu == null) ? 0 : menu.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Menu other = (Menu) obj;
        if (href == null) {
            if (other.href != null)
                return false;
        } else if (!href.equals(other.href))
            return false;
        if (icon == null) {
            if (other.icon != null)
                return false;
        } else if (!icon.equals(other.icon))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (menu == null) {
            if (other.menu != null)
                return false;
        } else if (!menu.equals(other.menu))
            return false;
        if (parentId == null) {
            if (other.parentId != null)
                return false;
        } else if (!parentId.equals(other.parentId))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

}
