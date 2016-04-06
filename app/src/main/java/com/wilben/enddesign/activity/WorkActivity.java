package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.WorkAdapter;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.operation.SearchService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class WorkActivity extends Activity {

    private String username;
    private List<Project> listWork;
    private GridView gridView;
    private WorkAdapter adapter;
    private ProgressDialog p;
    private int[] ID;
    private int[] State;
    private ImageButton f_back;
    private String role;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_grid);
        final Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        role = bundle.getString("role");
        gridView = (GridView) findViewById(R.id.gv_work);
        listWork = new ArrayList<Project>();
        adapter = new WorkAdapter(this, listWork);
        gridView.setAdapter(adapter);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putString("workId", String.valueOf(ID[position]));
                bundle1.putString("state", String.valueOf(State[position]));
                bundle1.putString("role", role);
                intent.putExtras(bundle1);
                intent.setClass(WorkActivity.this, WorkDetailActivity.class);
                startActivity(intent);
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
                State = new int[listWork.size()];
                for (int i = 0; i < listWork.size(); i++) {
                    ID[i] = listWork.get(i).getWorkId();
                    State[i] = listWork.get(i).getState();
                }
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