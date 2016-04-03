package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.ProjectAdapter;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.operation.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilben on 2016/4/3.
 */
public class MyProjectActivity extends Activity {

    private String username;
    private String position;
    /**
     * Item数据实体集合
     */
    private List<Project> projectList;
    /**
     * ListView对象
     */
    private ListView listview;
    private ProjectAdapter adapter;
    private ProgressDialog p;
    private int ID[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_casefragment);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        position = bundle.getString("position");
        listview = (ListView) findViewById(R.id.listview);
        projectList = new ArrayList<Project>();
        adapter = new ProjectAdapter(this, projectList);
        listview.setAdapter(adapter);
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        p.show();
        new ProjectAsyncTask().execute("Project", username, position);
    }

    class ProjectAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                projectList = new SearchService().getProject(params[0], params[1], params[2], projectList);
                ID = new int[projectList.size()];
                for (int i = 0; i < projectList.size(); i++)
                    ID[i] = projectList.get(i).getWorkId();
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
