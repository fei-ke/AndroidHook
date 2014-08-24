package com.fei_ke.AndroidHook;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditFragment extends Fragment implements View.OnClickListener {
    private EditText
            editPackageName,
            editClassName,
            editMethodName,
            editParaType,
            editReturnType;

    private Button buttonSave;

    private HookEntity hookEntity;

    private SharedPreferences preHookable, preResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        editPackageName = (EditText) view.findViewById(R.id.editPackageName);
        editClassName = (EditText) view.findViewById(R.id.editClassName);
        editMethodName = (EditText) view.findViewById(R.id.editMethodName);
        editParaType = (EditText) view.findViewById(R.id.editParaType);
        editReturnType = (EditText) view.findViewById(R.id.editReturnType);
        buttonSave = (Button) view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        preHookable = getActivity().getSharedPreferences(Constant.PREF_HOOKABLE_FW, Context.MODE_WORLD_READABLE);
        preResult = getActivity().getSharedPreferences(Constant.PREF_RETURN_RESULT, Context.MODE_WORLD_READABLE);

        initValue();
        return view;
    }

    private void initValue() {
        hookEntity = new HookEntity();
        hookEntity.setPackageName("");
        hookEntity.setClassName(TelephonyManager.class.getName());
        hookEntity.setMethodName("getDeviceId");
        hookEntity.setParamType(null);
        hookEntity.setReturnType(HookEntity.RETURN_TYPE_STRING);

        editPackageName.setText(hookEntity.getPackageName());
        editClassName.setText(hookEntity.getClassName());
        editMethodName.setText(hookEntity.getMethodName());
        editParaType.setText("");
        editReturnType.setText(hookEntity.getReturnType() + "");
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSave) {
            preHookable.edit().putStringSet(hookEntity.getResultKey(), hookEntity.writeToSet()).commit();
            preResult.edit().putString(hookEntity.getResultKey(), "123456789").commit();
        }
    }
}
