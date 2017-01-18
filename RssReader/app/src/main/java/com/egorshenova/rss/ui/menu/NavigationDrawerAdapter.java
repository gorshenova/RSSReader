package com.egorshenova.rss.ui.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.MenuClickCallback;
import com.egorshenova.rss.models.RSSMenuItem;
import com.egorshenova.rss.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.DrawerMenuHolder> {

    private Logger logger = Logger.getLogger(NavigationDrawerAdapter.class);
    private Context context;
    private List<RSSMenuItem> menuItems = new ArrayList<>();
    private MenuClickCallback callback;

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NavigationDrawerAdapter.DrawerMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_nav_drawer_menu, parent, false);
        return new DrawerMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.DrawerMenuHolder holder, int position) {
        final RSSMenuItem currentItemMenu = menuItems.get(position);
        holder.bind(currentItemMenu);
    }

    @Override
    public int getItemCount() {
        return menuItems == null ? 0 : menuItems.size();
    }

    public void setCallback(MenuClickCallback callback) {
        this.callback = callback;
    }

    public void setMenuItems(List<RSSMenuItem> menuData) {
        menuItems.clear();
        menuItems.addAll(menuData);
    }

    public class DrawerMenuHolder extends RecyclerView.ViewHolder {

        TextView menuTitle;

        public DrawerMenuHolder(View itemView) {
            super(itemView);
            menuTitle = (TextView) itemView.findViewById(R.id.tv_menu_title);
        }

        public void bind(final RSSMenuItem menuItem) {
            menuTitle.setText(menuItem.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback == null) return;

                    switch (menuItem.getMenuAction()) {
                        case ADD_FEED_ACTION:
                            callback.onAddFeedClick();
                            break;
                        case ACTION_OPEN_FEED:
                            callback.onMenuItemClick(menuItem);
                            break;
                        default:
                            logger.error("Menu menuAction isn't identified: " + menuItem.getMenuAction());
                            break;
                    }
                }
            });
        }
    }
}
