package com.egorshenova.rss.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.MenuClickCallback;
import com.egorshenova.rss.models.RSSMenuItem;
import com.egorshenova.rss.mvp.menu.MenuContract;
import com.egorshenova.rss.mvp.menu.MenuPresenter;
import com.egorshenova.rss.ui.base.BaseFragment;

import java.util.List;

public class MenuDrawerFragment extends BaseFragment implements MenuContract.View {

    private RecyclerView drawerList;
    private DrawerLayout drawerLayout;
    private View containerView;
    private MenuPresenter presenter;
    private NavigationDrawerAdapter drawerAdapter;
    private MenuClickCallback menuCallback;
    private int fragmentId;

    public static MenuDrawerFragment get(){
        MenuDrawerFragment frag =  new MenuDrawerFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        drawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        drawerAdapter = new NavigationDrawerAdapter(getContext());
        drawerList.setAdapter(drawerAdapter);
        drawerList.setLayoutManager(new LinearLayoutManager(getContext()));

        containerView = getActivity().findViewById(fragmentId);

        presenter = new MenuPresenter();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.prepareMenuItems();
        drawerAdapter.setCallback(this.menuCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, MenuClickCallback menuCallback) {
        this.drawerLayout = drawerLayout;
        this.menuCallback = menuCallback;
        this.fragmentId = fragmentId;

        drawerLayout.bringToFront();
    }

    public void toggleMenu() {
        drawerLayout.closeDrawer(containerView);
    }

    @Override
    public void showMenuItems(List<RSSMenuItem> menuItems) {
        drawerAdapter.setMenuItems(menuItems);
        drawerAdapter.notifyDataSetChanged();
    }

    public void updateMenu(){
        presenter.prepareMenuItems();
    }

    public void setMenuCallback(MenuClickCallback menuCallback) {
        this.menuCallback = menuCallback;
        drawerAdapter.setCallback(this.menuCallback);
    }
}
