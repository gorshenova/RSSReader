package com.egorshenova.rss.tasks;

public class AsyncTaskResult<T> {
    private T result;
    private Exception error;

    public T getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

    public AsyncTaskResult(T result) {
        super();
        this.result = result;
    }

    public AsyncTaskResult(Exception error) {
        super();
        this.error = error;
    }

    @Override
    public String toString() {
        return "AsyncTaskResult{" +
                "result=" + result +
                ", error=" + error +
                '}';
    }
}