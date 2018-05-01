package com.edgar.lilyhouse.Items;

public class ReaderImageItem {

//{
//"id": 71879,
//"comic_id": 9949,
//"chapter_name": "第131话",
//"chapter_order": 2730,
//"createtime": 1523864271,
//"folder": "y/一拳超人/第131话",
//"page_url": [
//"https://images.dmzj.com/y/一拳超人/第131话/001.jpg",
//],
//"chapter_type": 0,
//"chaptertype": 0,
//"chapter_true_type": 1,
//"chapter_num": 131,
//"updatetime": 1523864271,
//"sum_pages": 47,
//"sns_tag": 1,
//"uid": 0,
//"username": "",
//"translatorid": "5",
//"translator": "不良汉化组",
//"link": "",
//"message": "",
//"download": "",
//"hidden": 0,
//"direction": 0,
//"filesize": 5439246,
//"high_file_size": 0,
//"picnum": 47,
//"hit": 41021,
//"prev_chap_id": 71553,
//"comment_count": 1410
//}

    private int id, comic_id, chapter_order, chapter_type, chaptertype, chapter_true_type,
            sum_pages, sns_tag, uid, hidden, direction, filesize, high_file_size, picnum, hit, prev_chap_id, comment_count;
    long createtime, updatetime;
    private String chapter_name, folder, username, translatorid, translator, link, message, chapter_num, download;
    private String[] page_url;

    public ReaderImageItem(int id, int comic_id, int chapter_order, int chapter_type, int chaptertype, int chapter_true_type, String chapter_num, int sum_pages, int sns_tag, int uid, int hidden, int direction, int filesize, int high_file_size, int picnum, int hit, int prev_chap_id, int comment_count, long createtime, long updatetime, String chapter_name, String folder, String username, String translatorid, String translator, String link, String message, String download, String[] page_url) {
        this.id = id;
        this.comic_id = comic_id;
        this.chapter_order = chapter_order;
        this.chapter_type = chapter_type;
        this.chaptertype = chaptertype;
        this.chapter_true_type = chapter_true_type;
        this.chapter_num = chapter_num;
        this.sum_pages = sum_pages;
        this.sns_tag = sns_tag;
        this.uid = uid;
        this.hidden = hidden;
        this.direction = direction;
        this.filesize = filesize;
        this.high_file_size = high_file_size;
        this.picnum = picnum;
        this.hit = hit;
        this.prev_chap_id = prev_chap_id;
        this.comment_count = comment_count;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.chapter_name = chapter_name;
        this.folder = folder;
        this.username = username;
        this.translatorid = translatorid;
        this.translator = translator;
        this.link = link;
        this.message = message;
        this.download = download;
        this.page_url = page_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComic_id() {
        return comic_id;
    }

    public void setComic_id(int comic_id) {
        this.comic_id = comic_id;
    }

    public int getChapter_order() {
        return chapter_order;
    }

    public void setChapter_order(int chapter_order) {
        this.chapter_order = chapter_order;
    }

    public int getChapter_type() {
        return chapter_type;
    }

    public void setChapter_type(int chapter_type) {
        this.chapter_type = chapter_type;
    }

    public int getChaptertype() {
        return chaptertype;
    }

    public void setChaptertype(int chaptertype) {
        this.chaptertype = chaptertype;
    }

    public int getChapter_true_type() {
        return chapter_true_type;
    }

    public void setChapter_true_type(int chapter_true_type) {
        this.chapter_true_type = chapter_true_type;
    }

    public String getChapter_num() {
        return chapter_num;
    }

    public void setChapter_num(String chapter_num) {
        this.chapter_num = chapter_num;
    }

    public int getSum_pages() {
        return sum_pages;
    }

    public void setSum_pages(int sum_pages) {
        this.sum_pages = sum_pages;
    }

    public int getSns_tag() {
        return sns_tag;
    }

    public void setSns_tag(int sns_tag) {
        this.sns_tag = sns_tag;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public int getHigh_file_size() {
        return high_file_size;
    }

    public void setHigh_file_size(int high_file_size) {
        this.high_file_size = high_file_size;
    }

    public int getPicnum() {
        return picnum;
    }

    public void setPicnum(int picnum) {
        this.picnum = picnum;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getPrev_chap_id() {
        return prev_chap_id;
    }

    public void setPrev_chap_id(int prev_chap_id) {
        this.prev_chap_id = prev_chap_id;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTranslatorid() {
        return translatorid;
    }

    public void setTranslatorid(String translatorid) {
        this.translatorid = translatorid;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String[] getPage_url() {
        return page_url;
    }

    public void setPage_url(String[] page_url) {
        this.page_url = page_url;
    }
}
