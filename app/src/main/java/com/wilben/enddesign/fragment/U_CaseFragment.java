package com.wilben.enddesign.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.activity.LoginActivity;
import com.wilben.enddesign.operation.SearchService;
import com.wilben.enddesign.adapter.ListItemAdapter;
import com.wilben.enddesign.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class U_CaseFragment extends Fragment {

    /**
     * Item数据实体集合
     */
    private List<ItemEntity> itemEntities;
    /**
     * ListView对象
     */
    private ListView listview;
    private ListItemAdapter adapter;
    private ProgressDialog p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.u_casefragment, container, false);
        listview = (ListView) view.findViewById(R.id.listview);
        itemEntities = new ArrayList<ItemEntity>();
        adapter = new ListItemAdapter(getActivity(), itemEntities);
        listview.setAdapter(adapter);
        p = new ProgressDialog(getActivity());
        p.setMessage("加载中...");
        p.show();
        new JsonAsyncTask().execute(getActivity());
        return view;
    }


    class JsonAsyncTask extends AsyncTask<Context, Void, String> {


        @Override
        protected String doInBackground(Context... params) {
            try {
                itemEntities = new SearchService().getCase(params[0], itemEntities);
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