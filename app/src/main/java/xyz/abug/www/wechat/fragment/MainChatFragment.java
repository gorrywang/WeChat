package xyz.abug.www.wechat.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.abug.www.wechat.R;
import xyz.abug.www.wechat.activity.ChatActivity;
import xyz.abug.www.wechat.bean.ChatBean;
import xyz.abug.www.wechat.utils.DensityUtils;
import xyz.abug.www.wechat.decoration.MyDecoration;

import static xyz.abug.www.wechat.bean.ChatBean.CHAT_TYPE_FRIEND;
import static xyz.abug.www.wechat.bean.ChatBean.CHAT_TYPE_GROUP;

/**
 * Created by iswgr on 2017/7/3.
 * 消息页面
 */

public class MainChatFragment extends Fragment {

    private View mView;
    private SwipeMenuRecyclerView mRecycler;
    private MyChatAdapter mMyChatAdapter;
    //数据数组
    private List<ChatBean> mChatBeans = new ArrayList<>();
    private float mWidth, mHeight, mDelWidth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main_chat, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mWidth = DensityUtils.dp2px(getContext(), 100);
        mDelWidth = DensityUtils.dp2px(getContext(), 70);
        mHeight = DensityUtils.dp2px(getContext(), 65);
        initChatData();
        initAdapter();
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mMyChatAdapter = new MyChatAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mRecycler.setAdapter(mMyChatAdapter);
        mRecycler.addItemDecoration(new MyDecoration(getContext(), MyDecoration.VERTICAL_LIST));
        //设置监听器,创建菜单
        mRecycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                //标记为未读
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getResources().getColor(R.color.a))
                        .setText("标为未读")
                        .setTextColor(Color.WHITE)
                        .setTextSize(16)
                        .setWidth((int) mWidth)
                        .setHeight((int) mHeight);
                swipeRightMenu.addMenuItem(deleteItem);
                //删除
                SwipeMenuItem deleteItem2 = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getResources().getColor(R.color.b))
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(16)
                        .setWidth((int) mDelWidth)
                        .setHeight((int) mHeight);
                swipeRightMenu.addMenuItem(deleteItem2);

            }
        });
        mRecycler.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                if (menuPosition == 0) {
                    //标记为未读
                    Toast.makeText(getContext(), "标记为未读", Toast.LENGTH_SHORT).show();
                    mRecycler.smoothCloseMenu();
                } else if (menuPosition == 1) {
                    //删除
                    mChatBeans.remove(adapterPosition);
                    mMyChatAdapter.notifyDataSetChanged();
                    mRecycler.smoothCloseMenu();
                }
            }
        });
    }

    /**
     * 初始化聊天数据
     */
    private void initChatData() {
        ChatBean bean;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "12:12", "亮亮", "我叫桂亮，我是最帅的~哈哈哈", R.drawable.user1);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "11:12", "东辉", "我是葬爱家族的族长祁东辉", R.drawable.user2);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "10:09", "林依", "林雨~依直走~", R.drawable.user3);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "09:12", "老边", "嘿~嘿~嘿~", R.drawable.user4);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "09:05", "生鑫", "wo ji ni niang a", R.drawable.user5);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "09:00", "刘兴洲", "[图片]", R.drawable.user6);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "08:31", "老司机", "卧槽！有人把我头按在键盘上了！唔唔唔RRDTYFGHUIHYGYTFRYGHUIYGUTYFYTYG", R.drawable.user7);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "08:25", "赵诺诺", "哈哈哈，撒比", R.drawable.user8);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "08:12", "汉志", "哥哥你起床了吗？我到了", R.drawable.user9);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "07:12", "硕硕", "放假咱一块聚聚", R.drawable.user1);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_GROUP, "07:10", "顺丰速运", "[我要出出出出出出出名]", R.drawable.sfsy);
        mChatBeans.add(bean);
        bean = null;

    }

    /**
     * 初始化数据
     */
    private void initView() {
        mRecycler = (SwipeMenuRecyclerView) mView.findViewById(R.id.frag_chat_recycler_show);
    }

    /**
     * 消息页面适配器
     */
    private class MyChatAdapter extends SwipeMenuAdapter<MyChatAdapter.ViewHolder> {


        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_main_chat_chat, parent, false);
            return inflate;
        }

        @Override
        public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            final ViewHolder viewHolder = new ViewHolder(realContentView);
            //点击事件
            viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取指针
                    int adapterPosition = viewHolder.getAdapterPosition();
                    ChatBean bean = mChatBeans.get(adapterPosition);
                    //跳转
                    ChatActivity.jumpActivity(getContext(), bean);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ChatBean bean = mChatBeans.get(position);
            holder.textChat.setText(bean.getmLastChat());
            holder.textTime.setText(bean.getmTime());
            holder.textName.setText(bean.getmName());
            holder.img.setImageResource(bean.getmImg());
        }

        @Override
        public int getItemCount() {
            return mChatBeans.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textName, textTime, textChat;
            ImageView img;
            RelativeLayout relativeLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_main_chat_relative_item);
                textName = (TextView) itemView.findViewById(R.id.item_main_chat_txt_name);
                textTime = (TextView) itemView.findViewById(R.id.item_main_chat_txt_time);
                textChat = (TextView) itemView.findViewById(R.id.item_main_chat_txt_chat);
                img = (ImageView) itemView.findViewById(R.id.item_main_chat_img);
            }
        }
    }
}
