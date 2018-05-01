package com.edgar.lilyhouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.lilyhouse.Items.CommentItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.ImageUtil;
import com.edgar.lilyhouse.Utils.MyHtmlImageGetter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends UltimateViewAdapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private ArrayList<CommentItem> commentItems;
    private ImageUtil imageUtil;

    public CommentAdapter(Context context, ArrayList<CommentItem> commentItems) {
        this.context = context;
        this.commentItems = commentItems;
        this.imageUtil = new ImageUtil(context);
    }

    @Override
    public CommentViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public CommentViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_outside_comment, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public int getAdapterItemCount() {
        return commentItems.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        CommentItem commentItem = commentItems.get(position);

        if (commentItem.getMasterCommentNum() != 0) {
            holder.lvInnerContainer.setVisibility(View.VISIBLE);
            loadInnerComments(holder.lvInnerContainer, commentItem.getMasterComment());
        } else {
            holder.lvInnerContainer.setVisibility(View.GONE);
        }

        String avatarUrl = commentItem.getAvatar_url();
        imageUtil.setCircularImage(holder.ivAvatar, avatarUrl);
//        ImageUtil.setCircularImage(context, holder.ivAvatar, avatarUrl);
        int genderResId = (commentItem.getSex() == 1) ? R.drawable.ic_male : R.drawable.ic_female;
        holder.ivGender.setImageResource(genderResId);
        holder.tvNickname.setText(commentItem.getNickname());

        holder.tvTime.setText(getDateString(commentItem.getCreate_time()));
        holder.tvLikeCount.setText(String.valueOf(commentItem.getLike_amount()));
        holder.tvCommentCount.setText(String.valueOf(commentItem.getReply_amount()));

        String htmlContentString = "<span>" + commentItem.getContent() + "</span>";
        String imageUrl = commentItem.getUpload_images();
        if (imageUrl.length() != 0) {
            if (!imageUrl.startsWith("http")) {
                imageUrl = "https://images.dmzj.com/commentImg/" + commentItem.getObj_id() % 500
                        + "/" + imageUrl;
            }
            htmlContentString = htmlContentString + "<br /><img src=\"" + imageUrl + "\">";
        }
        holder.htvContent.setHtml(htmlContentString, new MyHtmlImageGetter(holder.htvContent));

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    protected class CommentViewHolder extends UltimateRecyclerviewViewHolder {



        protected ImageView ivAvatar, ivGender;
        protected TextView tvNickname, tvTime, tvLikeCount, tvCommentCount;
        protected HtmlTextView htvContent;
        protected LinearLayout lvInnerContainer;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView)itemView.findViewById(R.id.iv_user_avatar);
            ivGender = (ImageView)itemView.findViewById(R.id.iv_user_gender);
            tvNickname = (TextView)itemView.findViewById(R.id.tv_user_nickname);
            tvTime = (TextView)itemView.findViewById(R.id.tv_comment_time);
            tvLikeCount = (TextView)itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = (TextView)itemView.findViewById(R.id.tv_comment_count);
            htvContent = (HtmlTextView)itemView.findViewById(R.id.tv_outside_comment_content);
            lvInnerContainer = (LinearLayout)itemView.findViewById(R.id.lv_inner_comment_container);

        }
    }














    private String getDateString(long lastUpdateTime) {
        long times = lastUpdateTime * 1000;
        Date date = new Date(times);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    private void loadInnerComments(LinearLayout lvInnerContainer, ArrayList<CommentItem.MasterCommentItem> masterCommentItems) {

        lvInnerContainer.removeAllViews();

        for (int position = 0; position < masterCommentItems.size(); position++) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_inner_comment, null);

            TextView tvFloor = (TextView)view.findViewById(R.id.tv_comment_floor_number);
            HtmlTextView htvInnerContent = (HtmlTextView)view.findViewById(R.id.tv_inner_comment_content);

            tvFloor.setText("#" + String.valueOf(position + 1));
            String nameHtml = "<b>" + masterCommentItems.get(position).getNickname() + "</b>";
            String textContent = nameHtml + ": " + masterCommentItems.get(position).getContent();
            String htmlContent = "<span>" + textContent + "</span>";

            String imageUrl = masterCommentItems.get(position).getUpload_images();
            if (imageUrl.length() != 0) {
                if (!imageUrl.startsWith("http")) {
                    imageUrl = "https://images.dmzj.com/commentImg/" +
                            masterCommentItems.get(position).getObj_id() % 500 + "/" + imageUrl;
                }
                htmlContent = htmlContent + "<br /><img src=\"" + imageUrl + "\">";
            }
            htvInnerContent.setHtml(htmlContent, new MyHtmlImageGetter(htvInnerContent));
            lvInnerContainer.addView(view);


        }

    }

}
