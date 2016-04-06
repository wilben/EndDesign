package com.wilben.enddesign.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wilben.enddesign.R;
import com.wilben.enddesign.activity.DesignerDetailActivity;
import com.wilben.enddesign.adapter.ListDesignerAdapter;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.operation.SearchService;

import java.util.ArrayList;
import java.util.List;

public class U_DesignerFragment extends Fragment {

    /**
     * Item数据实体集合
     */
    private List<Designer> listDesigner;
    /**
     * ListView对象
     */
    private ListView listview;
    private ListDesignerAdapter adapter;
    private TextView local_city;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private ProgressDialog p;
    private String[] names;
    private Bundle bundle;
    private Intent intent;
    private String username;
    private String user;
    private String role;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.u_designerfragment, container, false);
        listview = (ListView) view.findViewById(R.id.listview);
        local_city = (TextView) view.findViewById(R.id.local_city);
        getlocation();
        bundle = getArguments();
        if (bundle != null) {
            user = bundle.getString("user");
            role = bundle.getString("role");
        }
        listDesigner = new ArrayList<Designer>();
        adapter = new ListDesignerAdapter(getActivity(), listDesigner);
        listview.setAdapter(adapter);
        p = new ProgressDialog(getActivity());
        p.setMessage("加载中...");
        p.show();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                username = names[position];
                bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("user", user);
                bundle.putString("role", role);
                intent = new Intent();
                intent.setClass(getActivity(), DesignerDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        new JsonAsyncTask().execute("AllDesigner");
        return view;
    }

    //获取城市
    private void getlocation() {
        mLocationClient = new LocationClient(getActivity().getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        initLocation();
        mLocationClient.start();// 开始定位
    }

    /**
     * 设置相关参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 100000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            String cityString = location.getCity();
            cityString = cityString.substring(0, cityString.indexOf("市"));
            local_city.setText(cityString);
        }
    }


    class JsonAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                listDesigner.clear();
                listDesigner = new SearchService().getAllDesigner(params[0], listDesigner);
                names = new String[listDesigner.size()];
                for (int i = 0; i < listDesigner.size(); i++)
                    names[i] = listDesigner.get(i).getUsername();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            adapter.notifyDataSetChanged();
        }
    }
}