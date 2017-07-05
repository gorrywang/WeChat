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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView mRv;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mManager;
    private List<CityBean> mDatas;

    private TitleItemDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;

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
    }

    /**
     * 初始化数据
     */
    private void initView() {
        mRv = (RecyclerView) mView.findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(getContext()));
        //initDatas();
        initDatas(getResources().getStringArray(R.array.provinces));
        //mDatas = new ArrayList<>();//测试为空或者null的情况 已经通过 2016 09 08

        mRv.setAdapter(mAdapter = new CityAdapter(getContext(), mDatas));
        mRv.addItemDecoration(mDecoration = new TitleItemDecoration(getContext(), mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRv.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


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
     *
     * @param data
     * @return
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
     * 更新数据源
     *
     * @param view
     */
    public void updateDatas(View view) {
        for (int i = 0; i < 99; i++) {
            mDatas.add(new CityBean("东京"));
            mDatas.add(new CityBean("大阪"));
        }
        mAdapter.notifyDataSetChanged();
        mIndexBar.setmSourceDatas(mDatas);
    }


    //适配器

    public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
        private Context mContext;
        private List<CityBean> mDatas;
        private LayoutInflater mInflater;

        public CityAdapter(Context mContext, List<CityBean> mDatas) {
            this.mContext = mContext;
            this.mDatas = mDatas;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_city, parent, false));
        }

        @Override
        public void onBindViewHolder(final CityAdapter.ViewHolder holder, final int position) {
            final CityBean cityBean = mDatas.get(position);
            holder.tvCity.setText(cityBean.getCity());
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
            TextView tvCity;

            public ViewHolder(View itemView) {
                super(itemView);
                tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            }
        }
    }

}
