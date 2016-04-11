package com.wilben.enddesign.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.ListCaseAdapter;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.operation.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * 经典案例界面
 */
public class U_CaseFragment extends Fragment {

    /**
     * Item数据实体集合
     */
    private List<Project> itemEntities;
    /**
     * ListView对象
     */
    private ListView listview;
    private ListCaseAdapter adapter;
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
        itemEntities = new ArrayList<Project>();
        adapter = new ListCaseAdapter(getActivity(), itemEntities);
        listview.setAdapter(adapter);
        p = new ProgressDialog(getActivity());
        p.setMessage("加载中...");
        p.show();
        new JsonAsyncTask().execute("AllProject");
        return view;
    }


    class JsonAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
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