package xyz.abug.www.wechat.bean;

/**
 * Created by iswgr on 2017/7/3.
 * 消息实体类
 */

public class ChatBean {
    public static final int CHAT_TYPE_FRIEND = 0;
    public static final int CHAT_TYPE_GROUP = 1;
    public static final int CHAT_TYPE_TAKE = 2;
    /**
     * 0：好友消息
     * 1:群消息
     * 2:公众号消息
     */
    private int mChatType;
    //消息时间
    private String mTime;
    //消息名字
    private String mName;
    //最后一条消息
    private String mLastChat;

    public ChatBean(int mChatType, String mTime, String mName, String mLastChat) {
        this.mChatType = mChatType;
        this.mTime = mTime;
        this.mName = mName;
        this.mLastChat = mLastChat;
    }

    public int getmChatType() {
        return mChatType;
    }

    public void setmChatType(int mChatType) {
        this.mChatType = mChatType;
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

    public String getmLastChat() {
        return mLastChat;
    }

    public void setmLastChat(String mLastChat) {
        this.mLastChat = mLastChat;
    }
}
