package com.egorshenova.rss.models;

/**
 * Created by eyablonskaya on 14-Feb-17.
 */

public class BaseModel {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseModel)) return false;

        BaseModel baseModel = (BaseModel) o;

        return getId() == baseModel.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
