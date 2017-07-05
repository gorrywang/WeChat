package xyz.abug.www.wechat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;
import xyz.abug.www.wechat.R;
import xyz.abug.www.wechat.bean.ChatBean;
import xyz.abug.www.wechat.bean.MessageBean;
import xyz.abug.www.wechat.utils.TulingUtils;

import static xyz.abug.www.wechat.bean.ChatBean.CHAT_TYPE_FRIEND;
import static xyz.abug.www.wechat.bean.ChatBean.CHAT_TYPE_GROUP;
import static xyz.abug.www.wechat.bean.ChatBean.CHAT_TYPE_TAKE;
import static xyz.abug.www.wechat.utils.TulingUtils.mTuringKey;
import static xyz.abug.www.wechat.utils.TulingUtils.mUniqueId;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    //好友实体对象
    private static ChatBean mBean;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    //消息
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private MyChatAdapter mAdapter;
    private RecyclerView mRecyclerView;
    //声音
    private TextView mTextSy;
    //输入框
    private EditText mEditInput;
    //输入信息的三种状态
    private static final int INPUT_STATUS_SHENGYIN = 0;
    private static final int INPUT_STATUS_BIAOQING = 1;
    private static final int INPUT_STATUS_JIAHAO = 2;
    //当前输入的状态
    private int mInputStatus = INPUT_STATUS_SHENGYIN;
    //三种状态切换按钮
    private ImageView mImgSy, mImgJh, mImgBq;
    //每种状体的bool
    private boolean mOneBool = false;
    private boolean mTwoBool = true;
    private boolean mThreeBool = true;
    //表情框和添加框
    private LinearLayout mLinearBiaoqing, mLinearJiahao;
    //好友的控制面板
    private LinearLayout mLinearFriend;
    //键盘管理器
    private InputMethodManager mInputManager;
    //图灵管理
    private TuringApiManager mManager;
    //表情和加号viewpager
    private ViewPager mPagerBiaoqing, mPagerJiahao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //初始化控件
        initView();
        //初始化键盘管理器
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //设置toolbar
        setToolbar();
        //设置共有属性
        setting();
        //分开管理
        switchType();

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter = new MyChatAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * 加载聊天数据
     */
    private void initData() {
        MessageBean bean = null;
        bean = new MessageBean(0, 0, "你在干什么", "2017.1.1", mBean.getmName(), R.drawable.icon);
        mMessageBeanList.add(bean);
        bean = null;
        bean = new MessageBean(1, 0, "没干什么", "2017.1.1", mBean.getmName(), R.drawable.icon);
        mMessageBeanList.add(bean);
        bean = null;
        bean = new MessageBean(0, 0, mBean.getmLastChat(), "2017.1.1", mBean.getmName(), R.drawable.icon);
        mMessageBeanList.add(bean);
        bean = null;
    }

    /**
     * 公有设置
     */
    private void setting() {
        //名字
        mActionBar.setTitle(mBean.getmName());
    }

    /**
     * 设置Toolbar
     */
    private void setToolbar() {
        //设置toolbar
        setSupportActionBar(mToolbar);
        //开启toolbar返回
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 监听软键盘发送按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            //查看数据
            String data = mEditInput.getText().toString().trim();
            if (TextUtils.isEmpty(data)) {
                Toast.makeText(ChatActivity.this, "请输入发送内容", Toast.LENGTH_SHORT).show();
                return false;
            }
            MessageBean bean = new MessageBean(1, 1, data, "2017.1.1", "王高瑞", R.drawable.icon);
            mMessageBeanList.add(bean);
            mAdapter.notifyDataSetChanged();
            mEditInput.setText("");
            mRecyclerView.smoothScrollToPosition(mMessageBeanList.size() - 1);
//            //关闭软键盘
//            if(mInputManager.isActive()){
//                mInputManager.hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(), 0);
//            }
            //像服务器发送消息
            try {
                mManager.requestTuringAPI(data);
            } catch (Exception e) {
                Toast.makeText(ChatActivity.this, "错误", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPagerBiaoqing = (ViewPager) findViewById(R.id.ac_chat_pager_biaoqing);
        mPagerJiahao = (ViewPager) findViewById(R.id.ac_chat_pager_jiahao);
        mLinearFriend = (LinearLayout) findViewById(R.id.ac_chat_linear_friend_con);
        mToolbar = (Toolbar) findViewById(R.id.ac_chat_toolbar_title);
        mLinearBiaoqing = (LinearLayout) findViewById(R.id.ac_chat_linear_biaoqing);
        mLinearJiahao = (LinearLayout) findViewById(R.id.ac_chat_linear_jiahao);
        mRecyclerView = (RecyclerView) findViewById(R.id.ac_chat_recycler_show);
        mTextSy = (TextView) findViewById(R.id.ac_chat_txt_sy);
        mEditInput = (EditText) findViewById(R.id.ac_chat_edit_input);
        mImgSy = (ImageView) findViewById(R.id.ac_chat_img_shengyin);
        mImgBq = (ImageView) findViewById(R.id.ac_chat_img_biaoqing);
        mImgJh = (ImageView) findViewById(R.id.ac_chat_img_jihao);
        mImgSy.setOnClickListener(this);
        mImgBq.setOnClickListener(this);
        mImgJh.setOnClickListener(this);
        mEditInput.setOnClickListener(this);
        mEditInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //取得焦点
                    if (mInputStatus == INPUT_STATUS_JIAHAO || mInputStatus == INPUT_STATUS_BIAOQING) {
                        //关闭两个面板
                        mLinearJiahao.setVisibility(View.GONE);
                        mLinearBiaoqing.setVisibility(View.GONE);
                        mInputStatus = INPUT_STATUS_SHENGYIN;
                        //将两个数值变为true
                        mTwoBool = true;
                        mThreeBool = true;
                        //第二个变图标
                        mImgBq.setImageResource(R.drawable.biaoqing);
                    }
                } else {
                    //失去焦点
                }
            }
        });
    }

    /**
     * 判断页面类型
     * public static final int CHAT_TYPE_FRIEND = 0;
     * public static final int CHAT_TYPE_GROUP = 1;
     * public static final int CHAT_TYPE_TAKE = 2;
     * 0：好友消息
     * 1:群消息
     * 2:公众号消息
     */
    private void switchType() {
        switch (mBean.getmChatType()) {
            case CHAT_TYPE_FRIEND:
                //好友消息
                mLinearFriend.setVisibility(View.VISIBLE);
                //开启图灵
                startTuling();
                setFriendChat();
                break;
            case CHAT_TYPE_GROUP:
                //群组消息
                mLinearFriend.setVisibility(View.GONE);
                break;
            case CHAT_TYPE_TAKE:
                //公众号消息
                mLinearFriend.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 开启图灵
     */
    private void startTuling() {
        SDKInitBuilder b = new SDKInitBuilder(ChatActivity.this).setSecret(TulingUtils.mSecret).setTuringKey(mTuringKey)
                .setUniqueId(mUniqueId);
        // 初始化
        SDKInit.init(b, new InitListener() {

            // 回调失败
            @Override
            public void onFail(String arg0) {
                Log.e("TAG", arg0);
            }

            // 回调成功
            @Override
            public void onComplete() {
                // 实例化TuringApiManager，一定在sdkinit完成后
                mManager = new TuringApiManager(ChatActivity.this);
                //请求服务器
                mManager.setHttpListener(myHttpConnectionListener);
            }
        });
    }

    /**
     * 好友消息
     */
    private void setFriendChat() {
        //初始化数据
        initData();
        //初始化适配器
        initAdapter();
        //初始化pager
        initPager();
        //隐藏声音显示输入框
        mTextSy.setVisibility(View.GONE);
        mEditInput.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化表情和分类的adapter
     */
    private void initPager() {
        List<View> mList1 = new ArrayList<>();
        List<View> mList2 = new ArrayList<>();
//        MyPagerAdapter adapter2 = new MyPagerAdapter(mList2);
        View view1 = LayoutInflater.from(this).inflate(R.layout.item_jiahao_1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.item_jiahao_2, null);
        mList1.add(view1);
        mList1.add(view2);
        MyPagerAdapter adapter1 = new MyPagerAdapter(mList1);
        mPagerJiahao.setAdapter(adapter1);

    }

    /**
     * 从消息页面跳转到聊天页面
     */
    public static void jumpActivity(Context context, ChatBean bean) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
        mBean = bean;
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_chat_edit_input:
                //关闭面板
                if (mInputStatus == INPUT_STATUS_JIAHAO || mInputStatus == INPUT_STATUS_BIAOQING) {
                    //关闭两个面板
                    mLinearJiahao.setVisibility(View.GONE);
                    mLinearBiaoqing.setVisibility(View.GONE);
                    mInputStatus = INPUT_STATUS_SHENGYIN;
                    //将两个数值变为true
                    mTwoBool = true;
                    mThreeBool = true;
                    //第二个变图标
                    mImgBq.setImageResource(R.drawable.biaoqing);
                }
                break;
            case R.id.ac_chat_img_shengyin:
                //打开声音
                if (mOneBool) {
                    //打开键盘
                    mEditInput.setVisibility(View.VISIBLE);
                    //关闭 语音 表情 加号
                    mTextSy.setVisibility(View.GONE);
                    mLinearJiahao.setVisibility(View.GONE);
                    mLinearBiaoqing.setVisibility(View.GONE);
                    //打开输入法
                    mInputManager.showSoftInput(mEditInput, InputMethodManager.RESULT_SHOWN);
                    mInputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    //edit获取焦点
                    mEditInput.requestFocus();
                    //换声音标志
                    mImgSy.setImageResource(R.drawable.yuyin);
                } else {
                    //打开语音
                    mTextSy.setVisibility(View.VISIBLE);
                    mLinearJiahao.setVisibility(View.GONE);
                    mLinearBiaoqing.setVisibility(View.GONE);
                    mEditInput.setVisibility(View.GONE);
                    //关闭输入法
                    mInputManager.hideSoftInputFromWindow(mEditInput.getWindowToken(), 0);
                    //换键盘标志
                    mImgSy.setImageResource(R.drawable.jianpan);
                }

                if (mInputStatus != INPUT_STATUS_SHENGYIN) {
                    mTwoBool = true;
                    mThreeBool = true;
                    //第二个按钮换成表情
                    mImgBq.setImageResource(R.drawable.biaoqing);
                }
                mOneBool = !mOneBool;
                //设置为第一
                mInputStatus = INPUT_STATUS_SHENGYIN;
                break;
            case R.id.ac_chat_img_biaoqing:
                //打开表情
                if (mTwoBool) {
                    //打开表情面板
                    mLinearBiaoqing.setVisibility(View.VISIBLE);
                    //打开键盘
                    mEditInput.setVisibility(View.VISIBLE);
                    //关闭加号 语音
                    mTextSy.setVisibility(View.GONE);
                    mLinearJiahao.setVisibility(View.GONE);
                    //关闭输入法
                    mInputManager.hideSoftInputFromWindow(mEditInput.getWindowToken(), 0);
                    //换键盘标志
                    mImgBq.setImageResource(R.drawable.jianpan);
                } else {
                    //打开键盘
                    mEditInput.setVisibility(View.VISIBLE);
                    //关闭 语音 表情 加号
                    mTextSy.setVisibility(View.GONE);
                    mLinearJiahao.setVisibility(View.GONE);
                    mLinearBiaoqing.setVisibility(View.GONE);
                    //打开输入法
                    mInputManager.showSoftInput(mEditInput, InputMethodManager.RESULT_SHOWN);
                    mInputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    //edit获取焦点
                    mEditInput.requestFocus();
                    //换表情
                    mImgBq.setImageResource(R.drawable.biaoqing);
                }
                if (mInputStatus != INPUT_STATUS_BIAOQING) {
                    mOneBool = false;
                    mThreeBool = true;
                    //第一个按钮换成生硬
                    mImgSy.setImageResource(R.drawable.yuyin);
                }
                mTwoBool = !mTwoBool;
                //设置为第二
                mInputStatus = INPUT_STATUS_BIAOQING;
                break;
            case R.id.ac_chat_img_jihao:
                //打开加号
                if (mThreeBool) {
                    //打开加号面板
                    mLinearJiahao.setVisibility(View.VISIBLE);
                    //打开键盘
                    mEditInput.setVisibility(View.VISIBLE);
                    //关闭表情 语音
                    mTextSy.setVisibility(View.GONE);
                    mLinearBiaoqing.setVisibility(View.GONE);
                    //关闭输入法
                    mInputManager.hideSoftInputFromWindow(mEditInput.getWindowToken(), 0);
                } else {
                    //关闭加号面板
                    //打开键盘
                    mEditInput.setVisibility(View.VISIBLE);
                    //关闭 语音 表情 加号
                    mTextSy.setVisibility(View.GONE);
                    mLinearJiahao.setVisibility(View.GONE);
                    mLinearBiaoqing.setVisibility(View.GONE);
                    //打开输入法
                    mInputManager.showSoftInput(mEditInput, InputMethodManager.RESULT_SHOWN);
                    mInputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    //edit获取焦点
                    mEditInput.requestFocus();
                }
                if (mInputStatus != INPUT_STATUS_JIAHAO) {
                    mOneBool = false;
                    mTwoBool = true;
                    //第一个按钮换成生硬
                    mImgSy.setImageResource(R.drawable.yuyin);
                    //第二个换成表情
                    mImgBq.setImageResource(R.drawable.biaoqing);
                }
                mThreeBool = !mThreeBool;
                //设置为第三
                mInputStatus = INPUT_STATUS_JIAHAO;
                break;
        }
    }

    /**
     * 标题菜单点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回
                finish();
                break;
        }
        return true;
    }

    /**
     * pagerAdapter
     */
    private class MyPagerAdapter extends PagerAdapter {
        private List<View> mList;

        public MyPagerAdapter(List<View> mList) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }
    }

    /**
     * 消息适配器
     */
    private class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(ChatActivity.this).inflate(R.layout.item_message_chat, parent, false);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MessageBean bean = mMessageBeanList.get(position);
            int who = bean.getmWho();
            if (who == 0) {
                //好友
                holder.relativeLeft.setVisibility(View.VISIBLE);
                holder.relativeRight.setVisibility(View.GONE);
                holder.leftText.setText(bean.getmMessage());
                holder.leftImg.setImageResource(bean.getmImg());
            } else {
                //自己
                holder.relativeLeft.setVisibility(View.GONE);
                holder.relativeRight.setVisibility(View.VISIBLE);
                holder.rightText.setText(bean.getmMessage());
                holder.rightImg.setImageResource(bean.getmImg());
            }
        }

        @Override
        public int getItemCount() {
            return mMessageBeanList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout relativeLeft, relativeRight;
            TextView leftText, rightText;
            ImageView leftImg, rightImg;

            public ViewHolder(View itemView) {
                super(itemView);
                leftImg = (ImageView) itemView.findViewById(R.id.item_message_img_left);
                rightImg = (ImageView) itemView.findViewById(R.id.item_message_img_right);
                leftText = (TextView) itemView.findViewById(R.id.item_message_txt_left_show);
                rightText = (TextView) itemView.findViewById(R.id.item_message_txt_right_show);
                relativeLeft = (RelativeLayout) itemView.findViewById(R.id.item_message_relative_left);
                relativeRight = (RelativeLayout) itemView.findViewById(R.id.item_message_relative_right);
            }
        }
    }


    /**
     * 网络请求回调
     */
    HttpConnectionListener myHttpConnectionListener = new HttpConnectionListener() {

        // 成功后
        @Override
        public void onSuccess(RequestResult result) {
            //有数据
            if (result != null) {
                try {
                    Log.d("TAG", result.getContent().toString());
                    // 获取json对象,参数结果转换成string
                    JSONObject result_obj = new JSONObject(result.getContent().toString());
                    //判断json对象中有没有text
                    if (result_obj.has("text")) {
                        final String ss = (String) result_obj.get("text");
                        //更新数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MessageBean bean = new MessageBean(0, 1, ss, "2017.1.1", mBean.getmName(), R.drawable.icon);
                                mMessageBeanList.add(bean);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.smoothScrollToPosition(mMessageBeanList.size() - 1);
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.d("TAG", "JSONException:" + e.getMessage());
                }
            }
        }

        // 失败后
        @Override
        public void onError(ErrorMessage errorMessage) {
            Log.d("TAG", errorMessage.getMessage());
        }
    };

}
