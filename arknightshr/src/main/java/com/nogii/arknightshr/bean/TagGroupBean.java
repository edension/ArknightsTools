package com.nogii.arknightshr.bean;

import java.util.List;

public class TagGroupBean {


    /**
     * id : 1
     * name : 资质
     * children : [{"id":101,"name":"新手","rare":1},{"id":102,"name":"资深干员","rare":3},{"id":103,"name":"高级资深干员","rare":4}]
     */

    private long id;
    private String name;
    private List<TagBean> children;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TagBean> getChildren() {
        return children;
    }

    public void setChildren(List<TagBean> children) {
        this.children = children;
    }
}
