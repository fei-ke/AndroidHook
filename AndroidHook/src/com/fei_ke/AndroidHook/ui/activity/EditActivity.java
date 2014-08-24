package com.fei_ke.AndroidHook.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.fei_ke.AndroidHook.R;
import com.fei_ke.AndroidHook.entity.HookEntity;
import com.fei_ke.AndroidHook.ui.fragment.EditFragment;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_edit)
public class EditActivity extends BaseActivity {
    @Extra
    protected HookEntity hookEntity;
    private EditFragment editFragment;

    public static Intent getStartIntent(Context context, HookEntity hookEntity) {
        return EditActivity_.intent(context).hookEntity(hookEntity).get();
    }

    public static Intent getStartIntent(Context context) {
        return EditActivity_.intent(context).get();
    }

    @Override
    protected void onAfterViews() {
        editFragment = EditFragment.getInstance(hookEntity);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, editFragment)
                .commit();
    }
}
