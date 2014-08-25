package com.fei_ke.AndroidHook.ui.fragment;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fei_ke.AndroidHook.R;
import com.fei_ke.AndroidHook.entity.HookEntity;
import com.fei_ke.AndroidHook.utils.PreferenceUtil;

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
            editParaType;

    @ViewById
    protected Spinner
            spinnerReturnType,
            spinnerHookType;


    @FragmentArg
    protected HookEntity hookEntity;


    private static final String[] hookTypes = new String[]{
            "fw", "user", "user field"
    };
    private static final String[] returnTypes = new String[]{
            "void",
            "int",
            "String",
            "float",
            "boolean",
    };

    public static EditFragment getInstance(HookEntity hookEntity) {
        return EditFragment_.builder().hookEntity(hookEntity).build();
    }

    @Override
    protected void onAfterViews() {

        ArrayAdapter<String> adapterReturnType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, returnTypes);
        spinnerReturnType.setAdapter(adapterReturnType);

        ArrayAdapter<String> adapterHookType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, hookTypes);
        spinnerHookType.setAdapter(adapterHookType);

        initValue();
    }

    private void initValue() {
        if (hookEntity != null) {
            editAliasName.setText(hookEntity.getAlias());
            editPackageName.setText(hookEntity.getPackageName());
            editClassName.setText(hookEntity.getClassName());
            editMethodName.setText(hookEntity.getMethodName());
            editParaType.setText(hookEntity.getParamType());
            spinnerReturnType.setSelection(hookEntity.getReturnType());
            spinnerHookType.setSelection(hookEntity.getHookType());
            //editReturnType.setText(hookEntity.getReturnType());
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
        hookEntity.setReturnType(spinnerReturnType.getSelectedItemPosition());
        hookEntity.setHookType(spinnerHookType.getSelectedItemPosition());

        if (!TextUtils.isEmpty(hookEntity.getClassName()) && !TextUtils.isEmpty(hookEntity.getMethodName())) {
            PreferenceUtil.putFWHookEntity(hookEntity, spinnerHookType.getSelectedItemPosition());
        }
    }


}
