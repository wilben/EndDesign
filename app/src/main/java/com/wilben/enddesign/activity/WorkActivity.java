package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.WorkAdapter;
import com.wilben.enddesign.entity.Work;
import com.wilben.enddesign.operation.SearchService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class WorkActivity extends Activity {

    private String username;
    private List<Work> listWork;
    private GridView gridView;
    private WorkAdapter adapter;
    private ProgressDialog p;
    private int[] ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_grid);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        gridView = (GridView) findViewById(R.id.gv_work);
        listWork = new ArrayList<Work>();
        adapter = new WorkAdapter(this, listWork);
        gridView.setAdapter(adapter);
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        p.show();
        new WorksAsyncTask().execute("Works", username);
    }

    class WorksAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            listWork.clear();
            try {
                listWork = new SearchService().getWorks(params[0], params[1], listWork);
                ID = new int[listWork.size()];
                for (int i = 0; i < listWork.size(); i++)
                    ID[i] = listWork.get(i).getWorkId();
            } catch (JSONException e) {
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