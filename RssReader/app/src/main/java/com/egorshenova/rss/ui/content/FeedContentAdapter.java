package com.egorshenova.rss.ui.content;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.models.RSSItem;
import com.egorshenova.rss.utils.StringUtils;
import com.egorshenova.rss.utils.link.URLClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedContentAdapter extends RecyclerView.Adapter<FeedContentAdapter.FeedContentHolder> {

    private List<RSSItem> itemList = new ArrayList<RSSItem>();
    private URLClickListener urlClickListener;

    @Override
    public FeedContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed_item, parent, false);
        return new FeedContentHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedContentHolder holder, int position) {
        RSSItem item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<RSSItem> items) {
        itemList.clear();
        itemList.addAll(items);
    }

    public class FeedContentHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView, readMoreTextView;
        public ImageView imageView;

        public FeedContentHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.item_title);
            descriptionTextView = (TextView) view.findViewById(R.id.item_descrption);
            readMoreTextView = (TextView) view.findViewById(R.id.item_read_more);
            imageView = (ImageView) view.findViewById(R.id.item_image);
            readMoreTextView.setPaintFlags(readMoreTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        public void bind(final RSSItem item) {
            titleTextView.setText(Html.fromHtml(item.getTitle()));

            SpannableString description = StringUtils.linkifyText(Html.fromHtml(item.getDescription()).toString(), urlClickListener);
            descriptionTextView.setText(description);

            if (item.getImageUrl() != null) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(GlobalContainer.getInstance().getContext())
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.default_rss_icon)
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }

            readMoreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (urlClickListener != null) {
                        urlClickListener.onClick(item.getLink());
                    }
                }
            });
        }
    }

    public void setUrlClickListener(URLClickListener urlClickListener) {
        this.urlClickListener = urlClickListener;
    }
}
