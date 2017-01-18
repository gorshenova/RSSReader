package com.egorshenova.rss.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.egorshenova.rss.Constants;
import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.AddFeedCallback;
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
import com.egorshenova.rss.utils.link.URLClickListener;

public class HomeActivity extends BaseActivity implements HomeContract.View{

    private static final Logger logger = Logger.getLogger(HomeActivity.class);
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private MenuDrawerFragment drawerFragment;
    private AddFeedFragment addFeedFragment;
    private FeedContentFragment feedContentFragment;
    private boolean isRefreshMenuAvailable;
    private HomePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerFragment = (MenuDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, menuClickCallback);

        setSupportActionBar(toolbar);

        presenter =  new HomePresenter();
        presenter.attachView(this);
        presenter.initializeContent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.saveFeeds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addFeedFragment != null) {
            addFeedFragment.setCallback(null);
        }

        if (feedContentFragment != null) {
            feedContentFragment.setUrlClickCallback(null);
        }
        drawerFragment.setMenuCallback(null);
        drawerFragment.setMenuCallback(null);
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return R.id.content_frame;
    }

    private void openFeedContentFragment(RSSFeed feed) {
        drawerFragment.toggleMenu();
        isRefreshMenuAvailable = true;
        Bundle args = new Bundle();
        args.putSerializable(Constants.BUNDLE_KEY_FEED, feed);
        feedContentFragment = FeedContentFragment.getInstance(args);
        feedContentFragment.setUrlClickCallback(urlClickCallback);
        openFragment(feedContentFragment, Constants.FRAGMENT_TAG_FEED_CONTENT);
    }

    private void openAddFeedFragment() {
        isRefreshMenuAvailable = false;
        drawerFragment.toggleMenu();
        addFeedFragment = AddFeedFragment.getInstance(getIntent().getExtras());
        addFeedFragment.setCallback(addFeedCallback);
        openFragment(addFeedFragment, Constants.FRAGMENT_TAG_ADD_FEED);
    }

    private AddFeedCallback addFeedCallback = new AddFeedCallback() {
        @Override
        public void openFeed(RSSFeed feed) {
            // show feed
            openFeedContentFragment(feed);

            //update menu
            drawerFragment.updateMenu();
        }
    };

    private MenuClickCallback menuClickCallback = new MenuClickCallback() {
        @Override
        public void onMenuItemClick(RSSMenuItem menuItem) {
            logger.debug("onMenuItemClick");
            openFeedContentFragment(menuItem.getFeed());
        }

        @Override
        public void onAddFeedClick() {
            logger.debug("onMenuItemClick");
            openAddFeedFragment();
        }
    };

    private URLClickListener urlClickCallback = new URLClickListener() {
        @Override
        public void onClick(String url) {
            openWebViewFragment(url);
        }
    };

    private void openWebViewFragment(String url) {
        drawerFragment.toggleMenu();

        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_SHOW_URL, url);
        openFragment(WebViewFragment.getInstance(args), Constants.FRAGMENT_TAG_WEB_VIEW);
    }

    @Override
    public void openFeedContentView(RSSFeed feed) {
        openFeedContentFragment(feed);
    }

    @Override
    public void openAddFeedView() {
        openAddFeedFragment();
    }
}
