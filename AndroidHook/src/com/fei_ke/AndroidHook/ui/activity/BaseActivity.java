package com.fei_ke.AndroidHook.ui.activity;

import android.app.Activity;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Activity基类
 *
 * @author fei-ke
 * @2014-6-14
 */
@EActivity
public abstract class BaseActivity extends Activity {

    /**
     * 切勿调用和复写此方法
     */
    @AfterViews
    final protected void onPrivateAfterViews() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        onAfterViews();
    }

    /**
     * 此方法在onCreate之后调用,勿在此方法上添加@AfterViews注解
     */
    protected abstract void onAfterViews();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
