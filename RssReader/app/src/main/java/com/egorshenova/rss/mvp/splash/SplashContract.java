package com.egorshenova.rss.mvp.splash;

import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

/**
 * Created by eyablonskaya on 22-Feb-17.
 */

public interface SplashContract {

    interface View extends IBaseView{
        void openHomeScreen();
    }

    interface Presenter extends IBasePresenter<View>{
        void loadSavedFeeds();
    }
}
