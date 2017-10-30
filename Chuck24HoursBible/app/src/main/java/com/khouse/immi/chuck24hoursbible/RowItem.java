package com.khouse.immi.chuck24hoursbible;

/**
 * Created by Immi on 4/27/2017.
 */
public class RowItem {

    private String topic_name;
    private int topic_pic_id;
    private String status;
    private String contactType;
    private String vidurlPath;
    private boolean viddownloaded;
    private String audurlPath;
    private boolean auddownloaded;

    public RowItem(String topic_name, int profile_pic_id, String status,
                   String contactType, String vidurlPath, String audurlPath, boolean viddownloaded, boolean auddownloaded) {

        this.topic_name = topic_name;
        this.topic_pic_id = profile_pic_id;
        this.status = status;
        this.contactType = contactType;
        this.vidurlPath = vidurlPath;
        this.viddownloaded = viddownloaded;
        this.audurlPath = audurlPath;
        this.auddownloaded = auddownloaded;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public int getTopic_pic_id() {
        return topic_pic_id;
    }

    public void setTopic_pic_id(int topic_pic_id) {
        this.topic_pic_id = topic_pic_id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getContactType() {
        return contactType;
    }
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    public void setVidUrlPath(String vidurlPath) {
        this.vidurlPath = vidurlPath;
    }
    public String getVidUrlPath() {
        return vidurlPath;
    }
    public void setVidDownloaded(boolean viddownloaded) {this.viddownloaded = viddownloaded; }
    public boolean getVidDownloaded() {
        return viddownloaded;
    }
    public void setAudUrlPath(String audurlPath) {
        this.audurlPath = audurlPath;
    }
    public String getAudUrlPath() {
        return audurlPath;
    }
    public void setAudDownloaded(boolean auddownloaded) {this.auddownloaded = auddownloaded; }
    public boolean getAudDownloaded() {
        return auddownloaded;
    }
}