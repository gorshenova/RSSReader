package com.egorshenova.rss.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.egorshenova.rss.Constants;
import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.MenuClickCallback;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.models.RSSMenuItem;
import com.egorshenova.rss.mvp.home.HomeContract;
import com.egorshenova.rss.mvp.home.HomePresenter;
import com.egorshenova.rss.ui.base.BaseActivity;
import com.egorshenova.rss.ui.content.FeedContentFragment;
import com.egorshenova.rss.ui.menu.MenuDrawerFragment;
import com.egorshenova.rss.utils.DialogHelper;
import com.egorshenova.rss.utils.Logger;

public class HomeActivity extends BaseActivity implements HomeContract.View {

    private static final Logger logger = Logger.getLogger(HomeActivity.class);
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private MenuDrawerFragment drawerFragment;
    private boolean isRefreshMenuAvailable;
    private HomePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerFragment = MenuDrawerFragment.get();
        add(drawerFragment, R.id.fragment_navigation_drawer, Constants.FRAGMENT_TAG_MENU_DRAWER);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, menuClickCallback);

        setSupportActionBar(toolbar);

        presenter = new HomePresenter();
        presenter.attachView(this);
        presenter.initializeContent();
    }

    @Override
    protected void onStart() {
        logger.debug("onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        logger.debug("onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        logger.debug("onDestroy");
        super.onDestroy();
        presenter.detachView();
        drawerFragment.setMenuCallback(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logger.debug("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logger.debug("onOptionsItemSelected");
        FeedContentFragment feedContentFragment = (FeedContentFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_TAG_FEED_CONTENT);
        if (isRefreshMenuAvailable && feedContentFragment != null) {
            int id = item.getItemId();
            if (id == R.id.menu_sort_by_newest) {
                feedContentFragment.sortByNewest();
                return true;
            } else if (id == R.id.menu_sort_by_oldest) {
                feedContentFragment.sortByOldest();
                return true;
            }
        } else {
            DialogHelper.showToast(this, R.string.error_sorting_not_available);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContentHolderId() {
        logger.debug("getContentHolderId");
        return R.id.content_frame;
    }

    @Override
    public void openAddFeedView() {
        logger.debug("openAddFeedView");
        openAddFeedFragment();
    }

    @Override
    public void openFeedContentView(RSSFeed feed) {
        logger.debug("openFeedContentView");
        openFeedContentFragment(feed);
    }

    @Override
    public void showProgress() {
        logger.debug("showProgress");
        createProgress(getString(R.string.progress_loading));
    }

    @Override
    public void hideProgress() {
        logger.debug("hideProgress");
        closeProgress();
    }

    @Override
    public void updateMenu() {
        logger.debug("updateMenu");
        drawerFragment.updateMenu();
    }

    private void openFeedContentFragment(RSSFeed feed) {
        logger.debug("openFeedContentFragment");

        drawerFragment.toggleMenu();
        isRefreshMenuAvailable = true;

        Bundle args = new Bundle();
        args.putSerializable(Constants.BUNDLE_KEY_FEED, feed);
        FeedContentFragment feedContentFragment = FeedContentFragment.getInstance(args);
        feedContentFragment.setArguments(args);
        openFragment(feedContentFragment, Constants.FRAGMENT_TAG_FEED_CONTENT);
    }

    private void openAddFeedFragment() {
        logger.debug("openAddFeedFragment");
        isRefreshMenuAvailable = false;
        drawerFragment.toggleMenu();
        AddFeedFragment addFeedFragment = AddFeedFragment.getInstance(getIntent().getExtras());
        openFragment(addFeedFragment, Constants.FRAGMENT_TAG_ADD_FEED);
    }

    private MenuClickCallback menuClickCallback = new MenuClickCallback() {
        @Override
        public void onMenuOpenFeedClick(RSSMenuItem menuItem) {
            logger.debug("onMenuOpenFeedClick");
            openFeedContentFragment(menuItem.getFeed());
        }

        @Override
        public void onMenuAddFeedClick() {
            logger.debug("onMenuOpenFeedClick");
            openAddFeedFragment();
        }
    };
}
