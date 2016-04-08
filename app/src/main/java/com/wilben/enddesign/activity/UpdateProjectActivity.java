package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.util.HttpUtils;
import com.wilben.enddesign.util.WriteJson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UpdateProjectActivity extends Activity {

    private ImageButton f_back;
    private EditText et_title, et_description;
    private TextView tv_upload, tv_style;
    private Bundle bundle;
    private String workId, title, description, style;
    private ProgressDialog p;
    private GridView gv_image; // 网格显示缩略图
    private final int IMAGE_OPEN = 1; // 打开图片标记
    private String pathImage; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter; // 适配器
    private List<String> list;
    private String uploadFile = "";
    private ArrayList<String> imageUrls;
    private String result;
    private String position;
    private String username;
    private Intent intent;
    private LinearLayout ll_style;
    private String[] items = new String[]{"现代简约", "地中海", "欧式"};
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateproject);
        bundle = this.getIntent().getExtras();
        workId = bundle.getString("workId");
        title = bundle.getString("title");
        username = bundle.getString("username");
        position = bundle.getString("position");
        description = bundle.getString("description");
        style = bundle.getString("style");
        imageUrls = new ArrayList<>();
        init();
        p = new ProgressDialog(this);
        
        /*
         * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
		 * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
		 */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        list = new ArrayList<>();

        // 获取控件对象
        gv_image = (GridView) findViewById(R.id.gv_image);

		/*
         * 载入默认图片添加图片加号 通过适配器实现 SimpleAdapter参数imageItem为数据源
		 * R.layout.griditem_addpic为布局
		 */
        bmp = BitmapFactory.decodeResource(getResources(),
                R.mipmap.gridview_addpic); // 加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.griditem_addpic, new String[]{"itemImage"},
                new int[]{R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如 map.put("itemImage",
		 * R.drawable.img); 解决方法: 1.自定义继承BaseAdapter实现 2.ViewBinder()接口实现
		 */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gv_image.setAdapter(simpleAdapter);

		/*
         * 监听GridView点击事件 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
		 */
        gv_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position == 0) { // 点击图片位置为+ 0对应0张图片
                    Toast.makeText(UpdateProjectActivity.this, "添加图片",
                            Toast.LENGTH_SHORT).show();
                    // 选择图片
                    intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    // 通过onResume()刷新数据
                } else {
                    dialog(position);
                }

            }
        });

    }

    public void init() {

        et_title = (EditText) findViewById(R.id.et_title);
        et_description = (EditText) findViewById(R.id.et_description);
        tv_style = (TextView) findViewById(R.id.tv_style);
        ll_style = (LinearLayout) findViewById(R.id.ll_style);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        et_title.setText(title);
        tv_style.setText(style);
        et_description.setText(description);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent();
                bundle = new Bundle();
                bundle.putString("position", position);
                bundle.putString("role", "1");
                bundle.putString("username", username);
                intent.putExtras(bundle);
                intent.setClass(UpdateProjectActivity.this, MyProjectActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ll_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateProjectActivity.this)
                        .setTitle("风格")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                index = which;
                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_style.setText(items[index]);
                                    }
                                })
                        .setNegativeButton("取消",
                                null).show();
            }
        });

        tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = et_title.getText().toString().trim();
                description = et_description.getText().toString().trim();
                style = tv_style.getText().toString().trim();
                p.setMessage("上传中...");
                if (title == null || title.length() <= 0) {
                    et_title.setError("项目标题不能为空");
                    et_title.requestFocus();
                    return;
                } else {
                    p.show();
                    new Thread(new Runnable() {
                        public void run() {
                            //上传修改头像
                            for (int i = 0; i < list.size(); i++) {
                                uploadFile = list.get(i);
                                if (uploadFile != null && uploadFile.length() > 0) {
                                    File file = new File(uploadFile);
                                    result = new HttpUtils().uploadFile(file, "FileInOutputStream");
                                    if (!"-1".equals(result)) {
                                        imageUrls.add(result);
                                    }
                                }
                            }
                            Project project = new Project("", title, "", "", Integer.parseInt(workId), description, imageUrls, 0, "", style);
                            List<Project> list = new ArrayList<Project>();
                            list.add(project);
                            WriteJson writeJson = new WriteJson();
                            // 将user对象写出json形式字符串
                            String jsonString = writeJson.getJsonData(list);
                            String result = null;
                            try {
                                result = new HttpUtils().updateProject("UpdateProject", jsonString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    }).start();

                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String msgobj = msg.obj.toString();
            p.dismiss();
            if (msgobj.equals("t")) {
                Toast.makeText(UpdateProjectActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdateProjectActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
            intent = new Intent();
            bundle = new Bundle();
            bundle.putString("position", position);
            bundle.putString("role", "1");
            bundle.putString("username", username);
            intent.putExtras(bundle);
            intent.setClass(UpdateProjectActivity.this, MyProjectActivity.class);
            startActivity(intent);
            finish();
            super.handleMessage(msg);
        }
    };
    /*
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */

    protected void dialog(final int position) {
        String string = "是否删除该图片";
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProjectActivity.this);
        builder.setMessage(string);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                list.remove(position - 1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    // 获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                // 查询选择图片
                Cursor cursor = getContentResolver().query(uri,
                        new String[]{MediaStore.Images.Media.DATA}, null,
                        null, null);
                // 返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                // 光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                list.add(pathImage);

            }
        } // end if 打开图片

    }

    // 刷新图片
    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this, imageItem,
                    R.layout.griditem_addpic, new String[]{"itemImage"},
                    new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gv_image.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            // 刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }


}

