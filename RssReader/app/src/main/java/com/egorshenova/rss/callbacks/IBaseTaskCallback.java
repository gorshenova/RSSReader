package com.egorshenova.rss.callbacks;

public interface IBaseTaskCallback<T> {

    void onError(String message);

    void onSuccess(T t);

}
