package com.edgar.lilyhouse.Items;

public class CommentQueryArg {

    private String obj_id, authoruid, is_Original, comment_type, dt;

    public CommentQueryArg(String obj_id, String authoruid, String is_Original, String comment_type, String dt) {
        this.obj_id = obj_id;
        this.authoruid = authoruid;
        this.is_Original = is_Original;
        this.comment_type = comment_type;
        this.dt = dt;
    }

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getAuthoruid() {
        return authoruid;
    }

    public void setAuthoruid(String authoruid) {
        this.authoruid = authoruid;
    }

    public String getIs_Original() {
        return is_Original;
    }

    public void setIs_Original(String is_Original) {
        this.is_Original = is_Original;
    }

    public String getComment_type() {
        return comment_type;
    }

    public void setComment_type(String comment_type) {
        this.comment_type = comment_type;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
