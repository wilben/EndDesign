package com.wilben.enddesign.operation;

import com.wilben.enddesign.entity.Case;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.entity.User;
import com.wilben.enddesign.entity.Work;
import com.wilben.enddesign.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public List<Case> getCase(String path, List<Case> caseList) throws Exception {
        String s = new HttpUtils().getData(path);

        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("allproject");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Case caseItem = new Case();
            caseItem.setAvatar(jsonObject.getString("avatar"));
            caseItem.setUsername(jsonObject.getString("username"));
            caseItem.setDescription(jsonObject.getString("description"));
            //解析照片路径
            ArrayList<String> listString = new ArrayList<String>();
            JSONArray jsonArray1 = jsonObject.getJSONArray("imageUrls");
            for (int j = 0; j < jsonArray1.length(); j++) {
                String msg = jsonArray1.getString(j);
                listString.add(msg);
            }
            caseItem.setImageUrls(listString);

            caseList.add(caseItem);
        }

        return caseList;
    }

    public List<Designer> getAllDesigner(String path, List<Designer> designerList) throws Exception {
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

    public List<Work> getWorks(String path, String username, List<Work> listWork) throws JSONException {
        String s = new HttpUtils().getWorks(path, username);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("works");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Work work = new Work();
            work.setImageUrl(jsonObject.getString("imageUrl"));
            work.setTitle(jsonObject.getString("title"));
            work.setWorkId(jsonObject.getInt("workId"));
            listWork.add(work);
        }
        return listWork;
    }

    public ArrayList<String> getWorkDetail(String path, String workId, ArrayList<String> imageList) throws JSONException {
        String s = new HttpUtils().getWorkDetail(path, workId);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("workdetail");
        for (int i = 0; i < jsonArray.length(); i++) {
            String msg = jsonArray.getString(i);
            imageList.add(msg);
        }
        return imageList;
    }
}
