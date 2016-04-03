package com.wilben.enddesign.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wilben.enddesign.R;
import com.wilben.enddesign.fragment.U_CaseFragment;
import com.wilben.enddesign.fragment.U_DesignerFragment;
import com.wilben.enddesign.fragment.D_ProjectFragment;
import com.wilben.enddesign.fragment.D_MyFragment;
import com.wilben.enddesign.fragment.U_MyFragment;
import com.wilben.enddesign.fragment.U_ProjectFragment;

public class U_MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private U_DesignerFragment designerFrament;
    private U_CaseFragment caseFragment;
    private U_ProjectFragment projectFragment;
    private U_MyFragment myFragment;
    private RadioButton radio_case;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_main);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        fragmentManager = getSupportFragmentManager();
        radio_case = (RadioButton) findViewById(R.id.radio_case);
        radio_case.setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        username = bundle.getString("username");
        changeFragment(0);

    }

    private void changeFragment(int index) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        hideFragments(beginTransaction);
        switch (index) {
            case 1:
                if (designerFrament == null) {
                    designerFrament = new U_DesignerFragment();
                    beginTransaction.add(R.id.main_content, designerFrament);
                } else {
                    beginTransaction.show(designerFrament);
                }
                break;
            case 0:
                if (caseFragment == null) {
                    caseFragment = new U_CaseFragment();
                    beginTransaction.add(R.id.main_content, caseFragment);
                } else {
                    beginTransaction.show(caseFragment);
                }
                break;
            case 2:
                if (projectFragment == null) {
                    projectFragment = new U_ProjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    projectFragment.setArguments(bundle);
                    beginTransaction.add(R.id.main_content, projectFragment);
                } else {
                    beginTransaction.show(projectFragment);
                }
                break;
            case 3:
                if (myFragment == null) {
                    myFragment = new U_MyFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
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
        if (designerFrament != null)
            transaction.hide(designerFrament);
        if (caseFragment != null)
            transaction.hide(caseFragment);
        if (projectFragment != null)
            transaction.hide(projectFragment);
        if (myFragment != null)
            transaction.hide(myFragment);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_case:
                changeFragment(0);
                break;
            case R.id.radio_designer:
                changeFragment(1);
                break;
            case R.id.radio_project:
                changeFragment(2);
                break;
            case R.id.radio_my:
                changeFragment(3);
                break;
            default:
                break;
        }
    }
}
