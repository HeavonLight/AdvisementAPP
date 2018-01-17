package com.example.heavon.vo;

/**
 * Created by heavon on 2017/3/9.
 */

public class Type {
    private Long id;
    private String name;
    private String icon;
    private int orderid;
    private String title;
    private String keywords;

    public Type() {

    }

    public Type(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public Type(Long id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public Type(Long id, String name, String icon, int orderid, String title, String keywords) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.orderid = orderid;
        this.title = title;
        this.keywords = keywords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", orderid=" + orderid +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
