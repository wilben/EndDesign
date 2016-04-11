package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.ProjectAdapter;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.operation.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目列表界面
 * Created by wilben on 2016/4/3.
 */
public class MyProjectActivity extends Activity {

    private String username;
    private String position;
    private String role;
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
    private int State[];
    private ImageButton f_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        position = bundle.getString("position");
        role = bundle.getString("role");
        listview = (ListView) findViewById(R.id.listview);
        projectList = new ArrayList<Project>();
        adapter = new ProjectAdapter(this, projectList, role);
        listview.setAdapter(adapter);
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
        p.show();
        new ProjectAsyncTask().execute("Project", username, position, role);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pt, long id) {
                Intent intent = new Intent(MyProjectActivity.this, WorkDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("workId", String.valueOf(ID[pt]));
                bundle.putString("state", String.valueOf(State[pt]));
                bundle.putString("role", role);
                bundle.putString("position", position);
                bundle.putString("flag","t");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    class ProjectAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                projectList = new SearchService().getProject(params[0], params[1], params[2], params[3], projectList);
                ID = new int[projectList.size()];
                State = new int[projectList.size()];
                for (int i = 0; i < projectList.size(); i++) {
                    ID[i] = projectList.get(i).getWorkId();
                    State[i] = projectList.get(i).getState();
                }
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
