package com.edgar.lilyhouse.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.edgar.lilyhouse.Activities.ReaderActivity;
import com.edgar.lilyhouse.Items.ChapterItem;
import com.edgar.lilyhouse.R;

public class GridExAdapter extends BaseAdapter {

    private static final String TAG = "===================" + GridExAdapter.class.getSimpleName();

    private Context context;
    private ChapterItem chapterItem;
    private SharedPreferences sharedPreferences;
    private String titleString;
    private String[] authorsStrings;

    public GridExAdapter(Context context, ChapterItem chapterItem, String titleString, String[] authorsStrings) {
        this.context = context;
        this.chapterItem = chapterItem;
        this.titleString = titleString;
        this.authorsStrings = authorsStrings;
        this.sharedPreferences = context.getSharedPreferences("WATCH_STATE", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return chapterItem.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return chapterItem.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return chapterItem.getData().get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView;
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_grid_chapter, null);
        } else {
            itemView = convertView;
        }
        Button button = (Button) itemView.findViewById(R.id.btn_chapter_grid_item); {
        final TextView tvNew = (TextView)itemView.findViewById(R.id.tv_chapter_new);
        tvNew.setVisibility(View.GONE);
        button.setText(chapterItem.getData().get(position).getChapter_name());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018\5\1 chapter item click event
                storeWatchState(String.valueOf(chapterItem.getData().get(position).getId()), true);
                tvNew.setVisibility(View.GONE);

                String urlString = "https://m.dmzj.com/view/" + chapterItem.getData().get(position).getComic_id() +
                        "/" + chapterItem.getData().get(position).getId() + ".html";

                Intent readerIntent = new Intent(context, ReaderActivity.class);
                readerIntent.putExtra(context.getString(R.string.info_title_string_extra),
                        chapterItem.getData().get(position).getChapter_name());

                String fullTitleString = titleString + "/" + chapterItem.getData().get(position).getTitle() +
                        "/" + chapterItem.getData().get(position).getChapter_name();
                readerIntent.putExtra(context.getString(R.string.info_url_string_extra), urlString);
                readerIntent.putExtra(context.getString(R.string.full_title_string_extra), fullTitleString);
                readerIntent.putExtra(context.getString(R.string.authors_string_extra), authorsStrings);
                context.startActivity(readerIntent);


            }
        });
        if ( sharedPreferences.getBoolean(String.valueOf(chapterItem.getData().get(position).getId()), false ) )
            tvNew.setVisibility(View.GONE);
        }
        return itemView;
    }

    private void storeWatchState(String key, boolean hasWatched) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, hasWatched);
        editor.apply();

    }

}
