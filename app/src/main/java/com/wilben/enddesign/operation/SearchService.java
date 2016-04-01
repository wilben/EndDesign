package com.wilben.enddesign.operation;

import android.content.Context;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.entity.ItemEntity;
import com.wilben.enddesign.entity.User;
import com.wilben.enddesign.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchService {
    //得到所有的头条新闻

    public List<ItemEntity> getCase(Context context, List<ItemEntity> headerNews) throws Exception {
        String path = context.getResources().getString(R.string.Infourl);
        String s = new HttpUtils().getData(path);

        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("case");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ItemEntity headerNew = new ItemEntity();
            headerNew.setAvatar(jsonObject.getString("avatar"));
            headerNew.setTitle(jsonObject.getString("title"));
            headerNew.setContent(jsonObject.getString("content"));
            //解析照片路径
            ArrayList<String> listString = new ArrayList<String>();
            JSONArray jsonArray1 = jsonObject.getJSONArray("imageUrls");
            for (int j = 0; j < jsonArray1.length(); j++) {
                String msg = jsonArray1.getString(j);
                listString.add(msg);
            }
            headerNew.setImageUrls(listString);

            headerNews.add(headerNew);
        }

        return headerNews;
    }

    public List<Designer> getAllDesigner(Context context, List<Designer> designerList) throws Exception {
        String path = context.getResources().getString(R.string.Designerurl);
        String s = new HttpUtils().getData(path);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("allDesigner");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Designer designer = new Designer();
            designer.setAvatar(jsonObject.getString("avatar"));
            designer.setUsername(jsonObject.getString("username"));
            designer.setArea(jsonObject.getString("area"));
            designer.setStyle(jsonObject.getString("style"));
            designerList.add(designer);
        }
        return designerList;
    }

    public User getInfo(String path, String username) throws Exception {
        String s = new HttpUtils().getU_info(path, username);

        JSONObject jsonObject1 = new JSONObject(s);
        JSONObject jsonObject = jsonObject1.getJSONObject("info");
        User user = new User();
        user.setUsername(jsonObject.getString("username"));
        user.setRealname(jsonObject.getString("realname"));
        user.setAge(jsonObject.getString("age"));
        user.setSex(jsonObject.getString("sex"));
        user.setAvatar(jsonObject.getString("avatar"));
        return user;
    }

    public Designer getDesignerDetail(String path, String username) throws Exception {
        String s = new HttpUtils().getDesignerDetail(path, username);

        JSONObject jsonObject1 = new JSONObject(s);
        JSONObject jsonObject = jsonObject1.getJSONObject("designDetail");
        Designer designer = new Designer();
        designer.setAvatar(jsonObject.getString("avatar"));
        designer.setConcept(jsonObject.getString("concept"));
        designer.setMotto(jsonObject.getString("motto"));
        designer.setStyle(jsonObject.getString("style"));
        designer.setWork(jsonObject.getString("work"));
        designer.setPeriod(jsonObject.getString("period"));
        designer.setArea(jsonObject.getString("area"));
        designer.setUsername(jsonObject.getString("username"));
        return designer;
    }
}
