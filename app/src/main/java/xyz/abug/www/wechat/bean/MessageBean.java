package xyz.abug.www.wechat.bean;

/**
 * Created by iswgr on 2017/7/4.
 * 消息内容
 */

public class MessageBean {
    //who
    private int mWho;
    //消息类型
    private int mMessageType;
    //消息内容
    private String mMessage;
    //消息时间
    private String mTime;
    //发送人
    private String mName;
    //头像
    private int mImg;

    public MessageBean(int mWho, int mMessageType, String mMessage, String mTime, String mName, int mImg) {
        this.mWho = mWho;
        this.mMessageType = mMessageType;
        this.mMessage = mMessage;
        this.mTime = mTime;
        this.mName = mName;
        this.mImg = mImg;
    }

    public int getmImg() {
        return mImg;
    }

    public void setmImg(int mImg) {
        this.mImg = mImg;
    }

    public int getmWho() {
        return mWho;
    }

    public void setmWho(int mWho) {
        this.mWho = mWho;
    }

    public int getmMessageType() {
        return mMessageType;
    }

    public void setmMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
