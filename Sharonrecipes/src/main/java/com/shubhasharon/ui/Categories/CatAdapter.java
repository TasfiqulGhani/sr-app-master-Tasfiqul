package com.shubhasharon.ui.Categories;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shubhasharon.modals.Category;
import com.shubhasharon.ui.Home.OpenArticle;
import com.shubhasharon.R;
import com.shubhasharon.SQLITE.DBHelper;
import com.shubhasharon.utils.MyData;

import java.util.ArrayList;

/**
 * Created by wail babou on 2016-12-24.
 */

public class CatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Category> datasource = new ArrayList<>();
    Context context;
    ViewHolder viewHolder;
    private String[] bgColors;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public CatAdapter(Context context, ArrayList<Category> datasource) {
        this.context = context;
        this.datasource = datasource;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_cat, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = inflater.inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (datasource.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Typeface caviar = Typeface.createFromAsset(context.getAssets(),
                "CaviarDreams.ttf");
        Typeface stc = Typeface.createFromAsset(context.getAssets(),
                "stc.otf");

        if (holder instanceof ViewHolder) {
            ViewHolder mholder = (ViewHolder) holder;
            mholder.title.setText(Html.fromHtml(datasource.get(position).getCategory_title()).toString());
            mholder.title.setTypeface(stc);
            mholder.count.setText(datasource.get(position).getCategory_posts_count() +" POSTS");
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);

        }

    }


    @Override
    public int getItemCount() {
        if (datasource == null)
            return 0;
        else
            return datasource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, count;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    Intent ii = new Intent(context, OpenCategoryActivity.class);
                    ii.putExtra("id", datasource.get(position).getCategory_id());
                    ii.putExtra("title", datasource.get(position).getCategory_title());
                    context.startActivity(ii);
                }
            });
        }

    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_ATOP);

        }
    }
}
