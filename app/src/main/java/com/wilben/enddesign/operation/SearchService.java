package com.wilben.enddesign.operation;

import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.entity.User;
import com.wilben.enddesign.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public List<Project> getCase(String path, List<Project> caseList) throws Exception {
        String s = new HttpUtils().getData(path);

        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("allproject");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Project caseItem = new Project();
            caseItem.setImage(jsonObject.getString("image"));
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

    public Designer getD_Info(String path, String username) throws Exception {
        String s = new HttpUtils().getInfo(path, username);

        JSONObject jsonObject1 = new JSONObject(s);
        JSONObject jsonObject = jsonObject1.getJSONObject("info");
        Designer designer = new Designer();
        designer.setUsername(jsonObject.getString("username"));
        designer.setRealname(jsonObject.getString("realname"));
        designer.setAge(jsonObject.getString("age"));
        designer.setSex(jsonObject.getString("sex"));
        designer.setAvatar(jsonObject.getString("avatar"));
        designer.setConcept(jsonObject.getString("concept"));
        designer.setMotto(jsonObject.getString("motto"));
        designer.setWork(jsonObject.getString("work"));
        designer.setArea(jsonObject.getString("area"));
        return designer;
    }

    public User getU_Info(String path, String username) throws Exception {
        String s = new HttpUtils().getInfo(path, username);

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

    public List<Project> getWorks(String path, String username, List<Project> listWork) throws JSONException {
        String s = new HttpUtils().getWorks(path, username);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("works");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Project work = new Project();
            work.setImage(jsonObject.getString("image"));
            work.setTitle(jsonObject.getString("title"));
            work.setWorkId(jsonObject.getInt("workId"));
            work.setState(jsonObject.getInt("state"));
            listWork.add(work);
        }
        return listWork;
    }

    public Project getWorkDetail(String path, String workId, String state) throws JSONException {
        String s = new HttpUtils().getWorkDetail(path, workId, state);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONObject jsonObject = jsonObject1.getJSONObject("workdetail");
        Project project = new Project();
        project.setTitle(jsonObject.getString("title"));
        project.setUsername(jsonObject.getString("username"));
        project.setDesignername(jsonObject.getString("designername"));
        project.setTime(jsonObject.getString("time"));
        project.setDescription(jsonObject.getString("description"));
        project.setState(jsonObject.getInt("state"));
        //解析照片路径
        ArrayList<String> listString = new ArrayList<String>();
        JSONArray jsonArray1 = jsonObject.getJSONArray("imageUrls");
        for (int j = 0; j < jsonArray1.length(); j++) {
            String msg = jsonArray1.getString(j);
            listString.add(msg);
        }
        project.setImageUrls(listString);

        return project;
    }

    public List<Project> getProject(String path, String username, String position, String role, List<Project> projectList) throws JSONException {
        String s = new HttpUtils().getProject(path, username, position, role);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("projects");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Project project = new Project();
            project.setImage(jsonObject.getString("image"));
            project.setTitle(jsonObject.getString("title"));
            project.setTime(jsonObject.getString("time"));
            project.setUsername(jsonObject.getString("username"));
            project.setDesignername(jsonObject.getString("designername"));
            project.setWorkId(jsonObject.getInt("workId"));
            project.setState(jsonObject.getInt("state"));
            projectList.add(project);
        }
        return projectList;
    }

    public List<Designer> searchDesigner(String path, String content, List<Designer> designerList) throws JSONException {
        String s = new HttpUtils().getDesigner(path, content);
        JSONObject jsonObject1 = new JSONObject(s);
        //返回json的数组
        JSONArray jsonArray = jsonObject1.getJSONArray("searchDesigner");
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
}
