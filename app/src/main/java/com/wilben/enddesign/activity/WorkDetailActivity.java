package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wilben.enddesign.NoScrollGridView;
import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.NoScrollGridAdapter;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.operation.SearchService;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by wilben on 2016/4/2.
 */
public class WorkDetailActivity extends Activity {

    private NoScrollGridView gridView;
    private String workId;
    private String state;
    private Project project;
    private ArrayList<String> imageList;
    private NoScrollGridAdapter adapter;
    private ProgressDialog p;
    private TextView tv_title, tv_username, tv_time, tv_description, tv_state;
    private ImageButton f_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workdetail_entity);
        init();
        imageList = new ArrayList<String>();
        Bundle bundle = getIntent().getExtras();
        workId = bundle.getString("workId");
        state = bundle.getString("state");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkDetailActivity.this, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageList);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }
        });
        p.show();
        new WorkDetailAsyncTask().execute("WorkDetail", workId, state);
    }

    private void init() {
        gridView = (NoScrollGridView) findViewById(R.id.gridview);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_state = (TextView) findViewById(R.id.tv_state);
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
    }


    class WorkDetailAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                project = new Project();
                project = new SearchService().getWorkDetail(params[0], params[1], params[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            tv_title.setText(project.getTitle());
            tv_username.setText(project.getUsername());
            tv_time.setText(project.getTime());
            tv_description.setText(project.getDescription());
            switch (project.getState()) {
                case 0:
                    tv_state.setText("待设计");
                    break;
                case 1:
                    tv_state.setText("设计中");
                    break;
                case 2:
                    tv_state.setText("已完成");
                    break;
            }
            imageList = project.getImageUrls();
            adapter = new NoScrollGridAdapter(WorkDetailActivity.this, imageList);
            gridView.setAdapter(adapter);
        }
    }
}
