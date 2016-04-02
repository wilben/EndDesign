package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.wilben.enddesign.NoScrollGridView;
import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.NoScrollGridAdapter;
import com.wilben.enddesign.operation.SearchService;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by wilben on 2016/4/2.
 */
public class WorkDetailActivity extends Activity {

    private NoScrollGridView gridView;
    private String workId;
    private ArrayList<String> imageList;
    private NoScrollGridAdapter adapter;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workdetail_entity);
        gridView = (NoScrollGridView) findViewById(R.id.gridview);
        imageList = new ArrayList<String>();
        adapter = new NoScrollGridAdapter(WorkDetailActivity.this, imageList);
        gridView.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
        workId = bundle.getString("workId");
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        p.show();
        new WorkDetailAsyncTask().execute("WorkDetail", workId);
    }

    class WorkDetailAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            imageList.clear();
            try {
                imageList = new SearchService().getWorkDetail(params[0], params[1], imageList);
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
