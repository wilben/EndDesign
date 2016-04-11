package com.wilben.enddesign.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wilben.enddesign.R;
import com.wilben.enddesign.fragment.MyFragment;
import com.wilben.enddesign.fragment.ProjectFragment;

/**
 * 设计师-主界面
 */
public class D_MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private ProjectFragment projectFragment;
    private MyFragment myFragment;
    private RadioButton radio_project;
    private String username;
    private String role;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_main);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        radio_project = (RadioButton) findViewById(R.id.radio_project);
        fragmentManager = getSupportFragmentManager();
        radioGroup.setOnCheckedChangeListener(this);
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        username = bundle.getString("username");
        role = bundle.getString("role");
        radio_project.setChecked(true);
    }

    private void changeFragment(int index) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        hideFragments(beginTransaction);
        switch (index) {
            case 0:
                if (projectFragment == null) {
                    projectFragment = new ProjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("role", role);
                    projectFragment.setArguments(bundle);
                    beginTransaction.add(R.id.main_content, projectFragment);
                } else {
                    beginTransaction.show(projectFragment);
                }
                break;
            case 1:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("role", role);
                    myFragment.setArguments(bundle);
                    beginTransaction.add(R.id.main_content, myFragment);
                } else {
                    beginTransaction.show(myFragment);
                }
                break;


            default:
                break;
        }
        beginTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (projectFragment != null)
            transaction.hide(projectFragment);
        if (myFragment != null)
            transaction.hide(myFragment);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_project:
                changeFragment(0);
                break;
            case R.id.radio_my:
                changeFragment(1);
                break;
            default:
                break;
        }
    }
}
