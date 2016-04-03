package com.wilben.enddesign.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.wilben.enddesign.R;
import com.wilben.enddesign.activity.MyProjectActivity;
import com.wilben.enddesign.adapter.ImageAdapter;

public class U_ProjectFragment extends Fragment {

    private String username;
    private ProgressDialog p;
    private GridView gv_project;
    private ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.u_projectfragment, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
        }
        init(view);
        p = new ProgressDialog(getActivity());
        p.setMessage("加载中...");
        gv_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MyProjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("position", String.valueOf(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private void init(View view) {
        gv_project = (GridView) view.findViewById(R.id.gv_project);
        listAdapter = new ImageAdapter(getActivity());
        gv_project.setAdapter(listAdapter);
    }


}
