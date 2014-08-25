package com.fei_ke.AndroidHook.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.fei_ke.AndroidHook.R;
import com.fei_ke.AndroidHook.constant.Constant;
import com.fei_ke.AndroidHook.entity.HookEntity;
import com.fei_ke.AndroidHook.utils.HookEntityUtil;
import com.fei_ke.AndroidHook.utils.PreferenceUtil;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewById
    protected ListView listView;
    protected ArrayAdapter<HookEntity> mAdapter;
    private SharedPreferences preResult;
    private List<HookEntity> entities;

    @Override
    protected void onAfterViews() {
        preResult = getSharedPreferences(Constant.PREF_RETURN_RESULT, Context.MODE_WORLD_READABLE);

        entities = new ArrayList<HookEntity>();
        mAdapter = new ArrayAdapter<HookEntity>(this, android.R.layout.simple_list_item_1, entities);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(EditActivity.getStartIntent(MainActivity.this, entities.get(position)));
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final HookEntity hookEntity = entities.get(position);
                final EditText editText = new EditText(MainActivity.this);
                editText.setText(preResult.getString(hookEntity.getStoreKey(), ""));
                editText.setSelection(editText.length());

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("set return")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String returnResult = editText.getText().toString();
                                saveResult(returnResult, hookEntity);
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .setNeutralButton("save", null)
                        .setView(editText)
                        .create();
                alertDialog.show();

                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String returnResult = editText.getText().toString();
                        saveResult(returnResult, hookEntity);
                    }
                });
            }
        });
    }

    private void saveResult(String returnResult, HookEntity hookEntity) {
        if (TextUtils.isEmpty(returnResult)) {
            preResult.edit().remove(hookEntity.getStoreKey()).commit();
        } else {
            preResult.edit().putString(hookEntity.getStoreKey(), returnResult).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reLoad();
    }

    private void reLoad() {
        entities.clear();
        List<HookEntity> listFw = HookEntityUtil.getAllHookEntity(PreferenceUtil.preHookable);
        entities.addAll(listFw);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(EditActivity.getStartIntent(this));
                break;
        }
        return true;
    }
}
