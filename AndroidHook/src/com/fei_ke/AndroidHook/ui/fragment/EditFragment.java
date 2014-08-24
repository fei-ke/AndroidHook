package com.fei_ke.AndroidHook.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.EditText;

import com.fei_ke.AndroidHook.R;
import com.fei_ke.AndroidHook.constant.Constant;
import com.fei_ke.AndroidHook.entity.HookEntity;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_edit)
public class EditFragment extends BaseFragment {
    @ViewById
    protected EditText
            editAliasName,
            editPackageName,
            editClassName,
            editMethodName,
            editParaType,
            editReturnType;


    @FragmentArg
    protected HookEntity hookEntity;

    protected SharedPreferences preHookable;


    public static EditFragment getInstance(HookEntity hookEntity) {
        return EditFragment_.builder().hookEntity(hookEntity).build();
    }

    @Override
    protected void onAfterViews() {
        preHookable = getActivity().getSharedPreferences(Constant.PREF_HOOKABLE_FW, Context.MODE_WORLD_READABLE);

        initValue();
    }

    private void initValue() {
        if (hookEntity != null) {
            editAliasName.setText(hookEntity.getAlias());
            editPackageName.setText(hookEntity.getPackageName());
            editClassName.setText(hookEntity.getClassName());
            editMethodName.setText(hookEntity.getMethodName());
            editParaType.setText(hookEntity.getParamType());
            editReturnType.setText(hookEntity.getReturnType() + "");
        } else {
            hookEntity = new HookEntity();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    public void save() {
        hookEntity.setAlias(editAliasName.getText().toString().trim());
        hookEntity.setPackageName(editPackageName.getText().toString().trim());
        hookEntity.setClassName(editClassName.getText().toString().trim());
        hookEntity.setMethodName(editMethodName.getText().toString().trim());
        hookEntity.setParamType(editParaType.getText().toString().trim());
        String returnType = editReturnType.getText().toString().trim();
        hookEntity.setReturnType(TextUtils.isEmpty(returnType) ? 0 : Integer.valueOf(returnType));

        if (!TextUtils.isEmpty(hookEntity.getClassName()) && !TextUtils.isEmpty(hookEntity.getMethodName())) {
            preHookable.edit().putString(hookEntity.getStoreKey(), hookEntity.getStoreString()).commit();
        }
    }


}
