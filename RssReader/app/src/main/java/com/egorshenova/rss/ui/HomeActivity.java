package com.egorshenova.rss.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.egorshenova.rss.utils.link.URLClickListener;

public class HomeActivity extends BaseActivity implements HomeContract.View{

    private static final Logger logger = Logger.getLogger(HomeActivity.class);
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;

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
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        drawerFragment = (MenuDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, menuClickCallback);

        setSupportActionBar(toolbar);

        presenter = new HomePresenter();
        presenter.attachView(this);
        presenter.initializeContent();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                logger.debug("---> openFeedContentView");
                openFeedContentView((RSSFeed) tab.getTag());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        //select  tab for current feed
        TabLayout.Tab tab = tabLayout.getTabAt(feed.getId() - 1);
        tab.select();

        drawerFragment.toggleMenu();
        updateTabLayoutVisibility(View.VISIBLE);
        isRefreshMenuAvailable = true;

        Bundle args = new Bundle();
        args.putSerializable(Constants.BUNDLE_KEY_FEED, feed);
        feedContentFragment = FeedContentFragment.getInstance(args);
        feedContentFragment.setUrlClickCallback(urlClickCallback);
        openFragment(feedContentFragment, Constants.FRAGMENT_TAG_FEED_CONTENT);
    }

    private void openAddFeedFragment() {
        isRefreshMenuAvailable = false;
        updateTabLayoutVisibility(View.GONE);
        drawerFragment.toggleMenu();
        addFeedFragment = AddFeedFragment.getInstance(getIntent().getExtras());
        openFragment(addFeedFragment, Constants.FRAGMENT_TAG_ADD_FEED);
    }

    private void openWebViewFragment(String url) {
        drawerFragment.toggleMenu();
        updateTabLayoutVisibility(View.GONE);
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_SHOW_URL, url);
        openFragment(WebViewFragment.getInstance(args), Constants.FRAGMENT_TAG_WEB_VIEW);
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

    private URLClickListener urlClickCallback = new URLClickListener() {
        @Override
        public void onClick(String url) {
            openWebViewFragment(url);
        }
    };


    @Override
    public void openFeedContentView(RSSFeed feed) {
        openFeedContentFragment(feed);
    }

    @Override
    public void openAddFeedView() {
        openAddFeedFragment();
    }

    @Override
    public void addTab(RSSFeed feed) {
        logger.debug("Add new tab by feed: " + feed);
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setTag(feed);
        newTab.setText(feed.getTitle());

        //feedId starts from 1 but tab index starts from 0, so tabPosition is calculated as feedId - 1
        int tabPosition = feed.getId() -1;
        tabLayout.addTab(newTab, tabPosition, true);
    }

    @Override
    public void updateTab(RSSFeed feed) {
        logger.debug("Update tab by feed: "  + feed);
        int tabPosition = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(tabPosition).setText(feed.getTitle());
    }

    @Override
    public void updateTabLayoutVisibility(int visibility) {
        tabLayout.setVisibility(visibility);
    }

    @Override
    public void showProgress() {
        createProgress(getString(R.string.progress_loading));
    }

    @Override
    public void hideProgress() {
        closeProgress();
    }

    @Override
    public void updateMenu() {
        drawerFragment.updateMenu();
    }
}
