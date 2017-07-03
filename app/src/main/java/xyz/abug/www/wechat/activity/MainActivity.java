package xyz.abug.www.wechat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import xyz.abug.www.wechat.R;
import xyz.abug.www.wechat.fragment.MainChatFragment;
import xyz.abug.www.wechat.fragment.MainFriendFragment;
import xyz.abug.www.wechat.fragment.MainLookFragment;
import xyz.abug.www.wechat.fragment.MainUserFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Fragment mChatFragment, mFriendFragment, mLookFragment, mUserFragment;
    private FragmentManager mManager;
    private LinearLayout mLinearChat, mLinearFriend, mLinearLook, mLinearUser;
    private Fragment mFragmentContent;
    private TextView mTextChat, mTextUser, mTextFriend, mTextLook;
    private ImageView mImgChat, mImgUser, mImgFriend, mImgLook;
    private TextView mTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        firstShow();

    }

    /**
     * 初始化
     */
    private void initView() {
        mTextTitle = (TextView) findViewById(R.id.ac_main_txt_title);
        mLinearChat = (LinearLayout) findViewById(R.id.ac_main_linear_chat);
        mLinearFriend = (LinearLayout) findViewById(R.id.ac_main_linear_friend);
        mLinearLook = (LinearLayout) findViewById(R.id.ac_main_linear_look);
        mLinearUser = (LinearLayout) findViewById(R.id.ac_main_linear_user);
        mTextChat = (TextView) findViewById(R.id.ac_main_txt_chat);
        mTextFriend = (TextView) findViewById(R.id.ac_main_txt_friend);
        mTextLook = (TextView) findViewById(R.id.ac_main_txt_look);
        mTextUser = (TextView) findViewById(R.id.ac_main_txt_user);
        mImgChat = (ImageView) findViewById(R.id.ac_main_img_chat);
        mImgFriend = (ImageView) findViewById(R.id.ac_main_img_friend);
        mImgLook = (ImageView) findViewById(R.id.ac_main_img_look);
        mImgUser = (ImageView) findViewById(R.id.ac_main_img_user);
        mLinearChat.setOnClickListener(this);
        mLinearFriend.setOnClickListener(this);
        mLinearLook.setOnClickListener(this);
        mLinearUser.setOnClickListener(this);
    }

    /**
     * 第一次运行加载聊天页面
     */
    private void firstShow() {
        if (mChatFragment == null) {
            mChatFragment = new MainChatFragment();
            mFragmentContent = new Fragment();
        }
        mManager = getSupportFragmentManager();
        switchContent(mFragmentContent, mChatFragment);
    }

    @Override
    public void onClick(View v) {
        //恢复ui
        resetUI();
        switch (v.getId()) {
            case R.id.ac_main_linear_chat:
                //微信
                //标题设置
                mTextTitle.setText(R.string.main_chat);
                //选中状态
                mImgChat.setImageResource(R.drawable.bottom_chat_check);
                mTextChat.setTextColor(getResources().getColor(R.color.fontcolor_content_check));
                if (mChatFragment == null) {
                    mChatFragment = new MainChatFragment();
                }
                switchContent(mFragmentContent, mChatFragment);
                break;
            case R.id.ac_main_linear_friend:
                //好友
                //标题设置
                mTextTitle.setText(R.string.main_friend);
                //选中状态
                mImgFriend.setImageResource(R.drawable.bottom_friend_check);
                mTextFriend.setTextColor(getResources().getColor(R.color.fontcolor_content_check));
                if (mFriendFragment == null) {
                    mFriendFragment = new MainFriendFragment();
                }
                switchContent(mFragmentContent, mFriendFragment);
                break;
            case R.id.ac_main_linear_look:
                //发现
                //标题设置
                mTextTitle.setText(R.string.main_look);
                //选中状态
                mImgLook.setImageResource(R.drawable.bottom_look_check);
                mTextLook.setTextColor(getResources().getColor(R.color.fontcolor_content_check));
                if (mLookFragment == null) {
                    mLookFragment = new MainLookFragment();
                }
                switchContent(mFragmentContent, mLookFragment);
                break;
            case R.id.ac_main_linear_user:
                //我
                //标题设置
                mTextTitle.setText(R.string.main_user);
                //选中状态
                mImgUser.setImageResource(R.drawable.bottom_user_check);
                mTextUser.setTextColor(getResources().getColor(R.color.fontcolor_content_check));
                if (mUserFragment == null) {
                    mUserFragment = new MainUserFragment();
                }
                switchContent(mFragmentContent, mUserFragment);
                break;
        }
    }


    /**
     * fragment add and show
     */
    private void switchContent(Fragment from, Fragment to) {
        if (mFragmentContent != to) {
            mFragmentContent = to;
            FragmentTransaction transaction = mManager.beginTransaction();
            Toast.makeText(MainActivity.this, to.isAdded() + "", Toast.LENGTH_SHORT).show();
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.ac_main_frame_show, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    /**
     * 恢复所有有图标
     */
    private void resetUI() {
        mTextUser.setTextColor(getResources().getColor(R.color.fontcolor_content));
        mTextLook.setTextColor(getResources().getColor(R.color.fontcolor_content));
        mTextFriend.setTextColor(getResources().getColor(R.color.fontcolor_content));
        mTextChat.setTextColor(getResources().getColor(R.color.fontcolor_content));
        mImgUser.setImageResource(R.drawable.bottom_user);
        mImgChat.setImageResource(R.drawable.bottom_chat);
        mImgLook.setImageResource(R.drawable.bottom_look);
        mImgFriend.setImageResource(R.drawable.bottom_friend);
    }
}
