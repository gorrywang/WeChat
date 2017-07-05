package xyz.abug.www.wechat.fragment;

import android.content.Context;
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
import java.util.Random;

import xyz.abug.www.wechat.R;
import xyz.abug.www.wechat.bean.CityBean;
import xyz.abug.www.wechat.decoration.DividerItemDecoration;
import xyz.abug.www.wechat.decoration.TitleItemDecoration;
import xyz.abug.www.wechat.view.IndexBar;

/**
 * Created by iswgr on 2017/7/3.
 */

public class MainFriendFragment extends Fragment {

    private View mView;

    private static final String TAG = "zxt";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mManager;
    private List<CityBean> mDatas;

    private TitleItemDecoration mDecoration;
    private Random mRandom;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    //随机头像
    private int[] mImgList = new int[]{R.drawable.user1,R.drawable.user2,R.drawable.user3,R.drawable.user4,R.drawable.user5,R.drawable.user6,R.drawable.user7,R.drawable.user8,R.drawable.user9};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main_friend, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mRandom = new Random();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(mManager = new LinearLayoutManager(getContext()));
        //initDatas();
        initDatas(getResources().getStringArray(R.array.provinces));

        //mDatas = new ArrayList<>();//测试为空或者null的情况 已经通过 2016 09 08

        mRecyclerView.setAdapter(mAdapter = new myAdapter(getContext(), mDatas));
        mRecyclerView.addItemDecoration(mDecoration = new TitleItemDecoration(getContext(), mDatas));
        mDecoration.setmTitleHeight(60);
        mDecoration.setTitleFontSize(45);
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRecyclerView.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


        //使用indexBar
        mTvSideBarHint = (TextView) mView.findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) mView.findViewById(R.id.indexBar);//IndexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas);//设置数据源
    }


    /**
     * 组织数据源
     */
    private void initDatas(String[] data) {
        mDatas = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            CityBean cityBean = new CityBean();
            cityBean.setCity(data[i]);//设置城市名称
            mDatas.add(cityBean);
        }
    }


    /**
     * 适配器
     */
    private class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
        private Context mContext;
        private List<CityBean> mDatas;
        private LayoutInflater mInflater;

        public myAdapter(Context mContext, List<CityBean> mDatas) {
            this.mContext = mContext;
            this.mDatas = mDatas;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public myAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(final myAdapter.ViewHolder holder, final int position) {
            final CityBean cityBean = mDatas.get(position);
            holder.name.setText(cityBean.getCity());
            holder.img.setImageResource(mImgList[mRandom.nextInt(9)]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas != null ? mDatas.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView img;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_friend_name);
                img = (ImageView) itemView.findViewById(R.id.item_friend_img);
            }
        }
    }

}
