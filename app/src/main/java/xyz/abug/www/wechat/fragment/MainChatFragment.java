package xyz.abug.www.wechat.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import xyz.abug.www.wechat.decoration.MyDecoration;
import xyz.abug.www.wechat.utils.DensityUtils;

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
        //申请权限
        sqqx();
        return mView;
    }

    /**
     * 申请权限
     */
    private void sqqx() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //无权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "权限申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "权限授权失败，6.0以上设备聊天页面可能会闪退", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
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
                    //设置
                    ChatBean bean = mChatBeans.get(adapterPosition);
                    bean.setmRead(1);
                    bean.setmLastChat("[未读消息]" + bean.getmLastChat());
                    mChatBeans.set(adapterPosition, bean);
                    mRecycler.smoothCloseMenu();
                    mMyChatAdapter.notifyDataSetChanged();
                } else if (menuPosition == 1) {
                    //删除
                    mChatBeans.remove(adapterPosition);
                    mRecycler.smoothCloseMenu();
                    mMyChatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 初始化聊天数据
     */
    private void initChatData() {
        ChatBean bean;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "12:17", "王五", "哦呵呵~", R.drawable.user9, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "12:12", "亮亮", "我叫桂亮，我是最帅的~哈哈哈", R.drawable.user1, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "11:12", "东辉", "我是葬爱家族的族长祁东辉", R.drawable.user2, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "10:09", "林依", "林雨~依直走~", R.drawable.user3, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "09:12", "老边", "嘿~嘿~嘿~", R.drawable.user4, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "09:05", "生鑫", "wo ji ni niang a", R.drawable.user5, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "09:00", "刘兴洲", "[图片]", R.drawable.user6, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "08:31", "老司机", "卧槽！有人把我头按在键盘上了！唔唔唔RRDTYFGHUIHYGYTFRYGHUIYGUTYFYTYG", R.drawable.user7, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "08:25", "赵诺诺", "哈哈哈，撒比", R.drawable.user8, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "08:12", "汉志", "哥哥你起床了吗？我到了", R.drawable.user9, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_FRIEND, "07:12", "硕硕", "放假咱一块聚聚", R.drawable.user1, 0);
        mChatBeans.add(bean);
        bean = null;
        bean = new ChatBean(CHAT_TYPE_GROUP, "07:10", "顺丰速运", "[我要出出出出出出出名]", R.drawable.sfsy, 0);
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
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ChatBean bean = mChatBeans.get(position);
            if (bean.getmRead() == 1) {
                holder.textChat.setTextColor(Color.RED);
            }
            holder.textChat.setText(bean.getmLastChat());
            holder.textTime.setText(bean.getmTime());
            holder.textName.setText(bean.getmName());
            holder.img.setImageResource(bean.getmImg());
            //点击事件
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取指针
                    int adapterPosition = position;
                    ChatBean bean = mChatBeans.get(adapterPosition);
                    //判断是不是一度消息
                    int i = bean.getmRead();
                    if (i == 1) {
                        holder.textChat.setTextColor(getResources().getColor(R.color.fontcolor_content));
                        bean.setmLastChat(bean.getmLastChat().split("]")[1]);
                        bean.setmRead(0);
                        mChatBeans.set(adapterPosition, bean);
                        bean = mChatBeans.get(adapterPosition);
                    }
                    //跳转
                    ChatActivity.jumpActivity(getContext(), bean);
                    mMyChatAdapter.notifyDataSetChanged();
                }
            });
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
