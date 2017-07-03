package xyz.abug.www.wechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.abug.www.wechat.R;
import xyz.abug.www.wechat.bean.ChatBean;
import xyz.abug.www.wechat.view.MyDecoration;

/**
 * Created by iswgr on 2017/7/3.
 * 消息页面
 */

public class MainChatFragment extends Fragment {

    private View mView;
    private RecyclerView mRecycler;
    private MyChatAdapter mMyChatAdapter;
    //数据数组
    private List<ChatBean> mChatBeans = new ArrayList<>();

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
    }

    /**
     * 初始化聊天数据
     */
    private void initChatData() {
        ChatBean bean;
        for (int i = 0; i < 10; i++) {
            bean = new ChatBean(1, "12:1" + i, "张三" + i, "你好，我是联想一班的张三" + i, R.drawable.icon);
            mChatBeans.add(bean);
            bean = null;
        }
    }

    /**
     * 初始化数据
     */
    private void initView() {
        mRecycler = (RecyclerView) mView.findViewById(R.id.frag_chat_recycler_show);
    }

    /**
     * 消息页面适配器
     */
    private class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_main_chat_chat, parent, false);
            ViewHolder viewHolder = new ViewHolder(inflate);
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

            public ViewHolder(View itemView) {
                super(itemView);
                textName = (TextView) itemView.findViewById(R.id.item_main_chat_txt_name);
                textTime = (TextView) itemView.findViewById(R.id.item_main_chat_txt_time);
                textChat = (TextView) itemView.findViewById(R.id.item_main_chat_txt_chat);
                img = (ImageView) itemView.findViewById(R.id.item_main_chat_img);
            }
        }
    }
}
