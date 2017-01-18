package com.egorshenova.rss.mvp.abs;

import android.content.Context;

import com.egorshenova.rss.GlobalContainer;

public class BasePresenter<T extends IBaseView> implements IBasePresenter<T> {

    private T view;
    private Context context;

    public BasePresenter() {
        this.context = GlobalContainer.getInstance().getContext();
    }

    @Override
    public void attachView(T view) {
        this.view =  view;
    }

    @Override
    public void detachView() {
        view =  null;
    }

        protected void checkViewAttached(){
        if(!isViewAttached()){
            throw new MvpViewNotAttachedException();
        }
    }

    private boolean isViewAttached(){
        return view != null;
    }

    protected T getView() {
        return view;
    }

    protected Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(IBaseView) before" + " requesting data to the Presenter");
        }
    }
}