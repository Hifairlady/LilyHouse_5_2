package com.edgar.lilyhouse.Items;

import java.util.ArrayList;

public class CommentItem {

    private int id, is_passed, top_status, is_goods, obj_id, sender_uid, like_amount,
            to_uid, to_comment_id, origin_comment_id, reply_amount, hot_comment_amount, sex, masterCommentNum;
    private long create_time;
    private String upload_images, content, cover, nickname, avatar_url;
    private ArrayList<MasterCommentItem> masterComment;

    public CommentItem(int id, int is_passed, int top_status, int is_goods, int obj_id, int sender_uid,
                       int like_amount, long create_time, int to_uid, int to_comment_id,
                       int origin_comment_id, int reply_amount, int hot_comment_amount, int sex,
                       int masterCommentNum, String upload_images, String content, String cover,
                       String nickname, ArrayList<MasterCommentItem> masterComment, String avatar_url) {
        this.id = id;
        this.is_passed = is_passed;
        this.top_status = top_status;
        this.is_goods = is_goods;
        this.obj_id = obj_id;
        this.sender_uid = sender_uid;
        this.like_amount = like_amount;
        this.create_time = create_time;
        this.to_uid = to_uid;
        this.to_comment_id = to_comment_id;
        this.origin_comment_id = origin_comment_id;
        this.reply_amount = reply_amount;
        this.hot_comment_amount = hot_comment_amount;
        this.sex = sex;
        this.masterCommentNum = masterCommentNum;
        this.upload_images = upload_images;
        this.content = content;
        this.cover = cover;
        this.nickname = nickname;
        this.masterComment = masterComment;
        this.avatar_url = avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_passed() {
        return is_passed;
    }

    public void setIs_passed(int is_passed) {
        this.is_passed = is_passed;
    }

    public int getTop_status() {
        return top_status;
    }

    public void setTop_status(int top_status) {
        this.top_status = top_status;
    }

    public int getIs_goods() {
        return is_goods;
    }

    public void setIs_goods(int is_goods) {
        this.is_goods = is_goods;
    }

    public int getObj_id() {
        return obj_id;
    }

    public void setObj_id(int obj_id) {
        this.obj_id = obj_id;
    }

    public int getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(int sender_uid) {
        this.sender_uid = sender_uid;
    }

    public int getLike_amount() {
        return like_amount;
    }

    public void setLike_amount(int like_amount) {
        this.like_amount = like_amount;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(int to_uid) {
        this.to_uid = to_uid;
    }

    public int getTo_comment_id() {
        return to_comment_id;
    }

    public void setTo_comment_id(int to_comment_id) {
        this.to_comment_id = to_comment_id;
    }

    public int getOrigin_comment_id() {
        return origin_comment_id;
    }

    public void setOrigin_comment_id(int origin_comment_id) {
        this.origin_comment_id = origin_comment_id;
    }

    public int getReply_amount() {
        return reply_amount;
    }

    public void setReply_amount(int reply_amount) {
        this.reply_amount = reply_amount;
    }

    public int getHot_comment_amount() {
        return hot_comment_amount;
    }

    public void setHot_comment_amount(int hot_comment_amount) {
        this.hot_comment_amount = hot_comment_amount;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMasterCommentNum() {
        return masterCommentNum;
    }

    public void setMasterCommentNum(int masterCommentNum) {
        this.masterCommentNum = masterCommentNum;
    }

    public String getUpload_images() {
        return upload_images;
    }

    public void setUpload_images(String upload_images) {
        this.upload_images = upload_images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<MasterCommentItem> getMasterComment() {
        return masterComment;
    }

    public void setMasterComment(ArrayList<MasterCommentItem> masterComment) {
        this.masterComment = masterComment;
    }

    public class MasterCommentItem {
        private int id, is_passed, top_status, is_goods, obj_id, sender_uid, like_amount, to_uid, to_comment_id, origin_comment_id, reply_amount, hot_comment_amount, sex;
        private String upload_images, content, cover, nickname;
        private long create_time;

        public MasterCommentItem(int id, int is_passed, int top_status, int is_goods, int obj_id, int sender_uid, int like_amount, long create_time, int to_uid, int to_comment_id, int origin_comment_id, int reply_amount, int hot_comment_amount, int sex, String upload_images, String content, String cover, String nickname) {
            this.id = id;
            this.is_passed = is_passed;
            this.top_status = top_status;
            this.is_goods = is_goods;
            this.obj_id = obj_id;
            this.sender_uid = sender_uid;
            this.like_amount = like_amount;
            this.create_time = create_time;
            this.to_uid = to_uid;
            this.to_comment_id = to_comment_id;
            this.origin_comment_id = origin_comment_id;
            this.reply_amount = reply_amount;
            this.hot_comment_amount = hot_comment_amount;
            this.sex = sex;
            this.upload_images = upload_images;
            this.content = content;
            this.cover = cover;
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_passed() {
            return is_passed;
        }

        public void setIs_passed(int is_passed) {
            this.is_passed = is_passed;
        }

        public int getTop_status() {
            return top_status;
        }

        public void setTop_status(int top_status) {
            this.top_status = top_status;
        }

        public int getIs_goods() {
            return is_goods;
        }

        public void setIs_goods(int is_goods) {
            this.is_goods = is_goods;
        }

        public int getObj_id() {
            return obj_id;
        }

        public void setObj_id(int obj_id) {
            this.obj_id = obj_id;
        }

        public int getSender_uid() {
            return sender_uid;
        }

        public void setSender_uid(int sender_uid) {
            this.sender_uid = sender_uid;
        }

        public int getLike_amount() {
            return like_amount;
        }

        public void setLike_amount(int like_amount) {
            this.like_amount = like_amount;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(int to_uid) {
            this.to_uid = to_uid;
        }

        public int getTo_comment_id() {
            return to_comment_id;
        }

        public void setTo_comment_id(int to_comment_id) {
            this.to_comment_id = to_comment_id;
        }

        public int getOrigin_comment_id() {
            return origin_comment_id;
        }

        public void setOrigin_comment_id(int origin_comment_id) {
            this.origin_comment_id = origin_comment_id;
        }

        public int getReply_amount() {
            return reply_amount;
        }

        public void setReply_amount(int reply_amount) {
            this.reply_amount = reply_amount;
        }

        public int getHot_comment_amount() {
            return hot_comment_amount;
        }

        public void setHot_comment_amount(int hot_comment_amount) {
            this.hot_comment_amount = hot_comment_amount;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUpload_images() {
            return upload_images;
        }

        public void setUpload_images(String upload_images) {
            this.upload_images = upload_images;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }


















}
