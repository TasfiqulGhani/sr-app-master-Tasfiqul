package com.shubhasharon.ui.Comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubhasharon.R;
import com.shubhasharon.modals.ItemComment;

import org.jsoup.Jsoup;

import java.util.ArrayList;


/**
 * Created by mac on 2/15/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<ItemComment> datasource = new ArrayList<>();
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public CommentAdapter(Context context, ArrayList<ItemComment> datasource) {
        this.context = context;
        this.datasource = datasource;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            view = inflater.inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(view);// to change later

        }else if(viewType==VIEW_TYPE_LOADING){

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder mholder=(ViewHolder) holder;
            mholder.username.setText(datasource.get(position).getUser());
            String date = datasource.get(position).getDate();
            try {
                mholder.date.setText(date.substring(0, Math.min(date.length(), 10)));
            }catch (Exception e){}
            mholder.comment.setText(html2text(datasource.get(position).getComment()));
        }
    }
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (datasource.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment,date,username;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.comment);
            date=itemView.findViewById(R.id.date);
            username=itemView.findViewById(R.id.username);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        public ViewHolder2(View itemView) {
            super(itemView);
        }
    }
}
