package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.ListDesignerAdapter;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.operation.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilben on 2016/4/3.
 */
public class SearchActivity extends Activity implements View.OnClickListener {

    /**
     * Item数据实体集合
     */
    private List<Designer> listDesigner;
    /**
     * ListView对象
     */
    private ListView listview;
    private ListDesignerAdapter adapter;
    private ProgressDialog p;
    private EditText et_content;
    private ImageButton f_back;
    private ImageView iv_search;
    private String content;
    private String[] names;
    private Bundle bundle;
    private Intent intent;
    private String username;
    private String user;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        bundle = getIntent().getExtras();
        user = bundle.getString("user");
        role = bundle.getString("role");
        init();
    }

    private void init() {
        listview = (ListView) findViewById(R.id.listview);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        f_back.setOnClickListener(this);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        listDesigner = new ArrayList<Designer>();
        adapter = new ListDesignerAdapter(this, listDesigner);
        listview.setAdapter(adapter);
        p = new ProgressDialog(this);
        p.setMessage("查询中...");
        iv_search.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                username = names[position];
                bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("user", user);
                bundle.putString("role", role);
                intent = new Intent();
                intent.setClass(SearchActivity.this, DesignerDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.iv_search:
                content = et_content.getText().toString().trim();
                if (content.length() <= 0) {
                    return;
                } else {
                    new SearchDesignerAsyncTask().execute("SearchDesigner", content);
                }
                break;

        }
    }

    class SearchDesignerAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                listDesigner.clear();
                listDesigner = new SearchService().searchDesigner(params[0], params[1], listDesigner);
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
