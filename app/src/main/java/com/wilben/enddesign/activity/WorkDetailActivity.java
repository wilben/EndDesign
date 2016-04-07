package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.NoScrollGridView;
import com.wilben.enddesign.R;
import com.wilben.enddesign.adapter.NoScrollGridAdapter;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.operation.SearchService;
import com.wilben.enddesign.util.HttpUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by wilben on 2016/4/2.
 */
public class WorkDetailActivity extends Activity implements View.OnClickListener {

    private NoScrollGridView gridView;
    private String workId;
    private String state;
    private String role;
    private Project project;
    private ArrayList<String> imageList;
    private NoScrollGridAdapter adapter;
    private ProgressDialog p;
    private TextView tv_title, tv_username, tv_time, tv_description, tv_state, tv_name, tv_update;
    private ImageButton f_back;
    private Button btn_accept, btn_cancel, btn_confirm;
    private String result = "";
    private String position;
    private Bundle bundle;
    private Intent intent;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workdetail);
        init();
        imageList = new ArrayList<String>();
        bundle = getIntent().getExtras();
        workId = bundle.getString("workId");
        state = bundle.getString("state");
        role = bundle.getString("role");
        position = bundle.getString("position");
        flag = bundle.getString("flag");
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
        tv_name = (TextView) findViewById(R.id.tv_name);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        tv_update = (TextView) findViewById(R.id.tv_update);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        f_back.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                if (flag.equals("t")) {
                    intent = new Intent();
                    bundle = new Bundle();
                    bundle.putString("position", position);
                    bundle.putString("role", role);
                    if (role.equals("1")) {
                        bundle.putString("username", project.getDesignername());
                    } else {
                        bundle.putString("username", project.getUsername());
                    }
                    intent.putExtras(bundle);
                    intent.setClass(WorkDetailActivity.this, MyProjectActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.tv_update:
                intent = new Intent();
                bundle = new Bundle();
                bundle.putString("workId", workId);
                bundle.putString("title", project.getTitle());
                bundle.putString("description", project.getDescription());
                bundle.putString("position", position);
                bundle.putString("username", project.getDesignername());
                intent.putExtras(bundle);
                intent.setClass(WorkDetailActivity.this, UpdateProjectActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_confirm:
                new AlertDialog.Builder(this)
                        .setTitle("确认完成项目吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“确认”后的操作
                                        new ChangeStateAsyncTask().execute("ChangeState", workId, "2");
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“返回”后的操作,这里不设置没有任何操作
                                    }
                                }).show();
                break;
            case R.id.btn_accept:
                new AlertDialog.Builder(this)
                        .setTitle("确认接收项目吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“确认”后的操作
                                        new ChangeStateAsyncTask().execute("ChangeState", workId, "1");
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“返回”后的操作,这里不设置没有任何操作
                                    }
                                }).show();
                break;
            case R.id.btn_cancel:
                new AlertDialog.Builder(this)
                        .setTitle("确认拒绝项目吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“确认”后的操作
                                        new ChangeStateAsyncTask().execute("ChangeState", workId, "-1");
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“返回”后的操作,这里不设置没有任何操作
                                    }
                                }).show();
                break;
            default:
                break;
        }
    }

    class ChangeStateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            result = new HttpUtils().changeState(params[0], params[1], params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if (result.equals("t")) {
                Toast.makeText(WorkDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WorkDetailActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
            intent = new Intent();
            bundle = new Bundle();
            bundle.putString("position", position);
            bundle.putString("role", role);
            if (role.equals("1")) {
                bundle.putString("username", project.getDesignername());
            } else {
                bundle.putString("username", project.getUsername());
            }
            intent.putExtras(bundle);
            intent.setClass(WorkDetailActivity.this, MyProjectActivity.class);
            startActivity(intent);
            finish();
        }
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
            if (role.equals("0")) {
                tv_username.setText(project.getDesignername());
            } else {
                tv_name.setText("客户");
                tv_username.setText(project.getUsername());
            }
            tv_title.setText(project.getTitle());
            tv_time.setText(project.getTime());
            tv_description.setText(project.getDescription());
            switch (project.getState()) {
                case 0:
                    tv_state.setText("待设计");
                    if (role.equals("1")) {
                        btn_accept.setVisibility(View.VISIBLE);
                    }
                    btn_cancel.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tv_state.setText("设计中");
                    if (role.equals("1")) {
                        tv_update.setVisibility(View.VISIBLE);
                        btn_confirm.setVisibility(View.VISIBLE);
                    }
                    break;
                case -1:
                    tv_state.setText("已取消");
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
