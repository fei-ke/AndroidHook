package com.fei_ke.AndroidHook.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewById
    protected ListView listView;
    protected ArrayAdapter<HookEntity> mAdapter;
    private SharedPreferences preHookableFW;
    private SharedPreferences preResult;
    private List<HookEntity> entities;

    @Override
    protected void onAfterViews() {
        preHookableFW = getSharedPreferences(Constant.PREF_HOOKABLE_FW, MODE_WORLD_READABLE);
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

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("set return")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String returnResult = editText.getText().toString();
                                preResult.edit().putString(hookEntity.getStoreKey(), returnResult).commit();
                            }
                        })
                        .setView(editText)
                        .create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reLoad();
    }

    private void reLoad() {
        entities.clear();
        List<HookEntity> list = HookEntityUtil.getAllHookEntity(preHookableFW);
        entities.addAll(list);
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
