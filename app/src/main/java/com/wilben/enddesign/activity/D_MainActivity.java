package com.wilben.enddesign.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wilben.enddesign.R;
import com.wilben.enddesign.fragment.D_ProjectFragment;
import com.wilben.enddesign.fragment.D_MyFragment;

public class D_MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private D_ProjectFragment projectFragment;
    private D_MyFragment myFragment;
    private RadioButton radio_project;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_main);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        fragmentManager = getSupportFragmentManager();
        radio_project = (RadioButton) findViewById(R.id.radio_project);
        radio_project.setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        changeFragment(0);
    }

    private void changeFragment(int index) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        hideFragments(beginTransaction);
        switch (index) {
            case 0:
                if (projectFragment == null) {
                    projectFragment = new D_ProjectFragment();
                    beginTransaction.add(R.id.main_content, projectFragment);
                } else {
                    beginTransaction.show(projectFragment);
                }
                break;
            case 1:
                if (myFragment == null) {
                    myFragment = new D_MyFragment();
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
