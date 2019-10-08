package com.example.maintenanceelevator.Activity;

import Constans.Constants;
import Constans.HttpModel;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int BAIDU_READ_PHONE_STATE =100;
    private EditText ed_name;
    private EditText ed_password;
    private CheckBox cb_remberword;
    private Button bt_forget;
    private Button bt_login;
    private SharedPreferences.Editor editor;
    private Boolean isCheckRember=false;
    private HttpModel model;
    private SharedPreferences sharedPreferences;
    private final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA};
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 400:
                    Toast.makeText(getApplicationContext(),"服务器连接失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    JSONObject jsonObject=JSONObject.parseObject((String)msg.obj);
                    if (jsonObject.getInteger("code")==200){
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        LoginActivity.this.finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"用户名密码错误！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                    default:break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        }
        initView();
        model=new HttpModel(getApplicationContext(),new HttpModel.HttpClientListener() {
            @Override
            public void onError() {
                handler.sendEmptyMessage(400);
            }

            @Override
            public void onSuccess(Object obj) {
                Message msg=new Message();
                msg.what=200;
                msg.obj=obj;
                handler.sendMessage(msg);
            }

            @Override
            public void onaddWithCommit(String type) {

            }

        });
        sharedPreferences=getApplicationContext().getSharedPreferences("config", getApplicationContext().MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String password=sharedPreferences.getString("password","");
        String name=sharedPreferences.getString("name","");
        if (!password.equals("")&&!name.equals("")){
            ed_password.setText(password);
            ed_name.setText(name);
            isCheckRember=true;
            cb_remberword.setChecked(true);
        }else{
            cb_remberword.setChecked(false);
            isCheckRember=false;
        }
    }
    private void initView() {
        ed_name=findViewById(R.id.login_input_username);
        ed_password=findViewById(R.id.login_input_password);
        cb_remberword=findViewById(R.id.remember_pwd);
        bt_forget=findViewById(R.id.forgive_pwd);
        bt_login=findViewById(R.id.login_btn);
        cb_remberword.setOnClickListener(this);
        bt_forget.setOnClickListener(this);
        bt_login.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remember_pwd:
                if (isCheckRember){
                    cb_remberword.setChecked(false);
                    isCheckRember=false;
                    editor.remove("name");
                    editor.remove("password");
                    editor.commit();
                }else{
                    if (ed_password.getText().toString().equals("")||ed_name.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"用户名或密码为空！",Toast.LENGTH_SHORT).show();
                        cb_remberword.setChecked(false);
                    }else{
                        editor.putString("name",ed_name.getText().toString());
                        editor.putString("password",ed_password.getText().toString());
                        editor.commit();
                        cb_remberword.setChecked(true);
                        isCheckRember=true;
                    }
                }
                break;
            case R.id.forgive_pwd:
                //重置IP地址
                startActivity(new Intent(LoginActivity.this,updateIpActivity.class));
                break;
            case R.id.login_btn:
                if (check()){
                    String name=ed_name.getText().toString();
                    String password=ed_password.getText().toString();
                    model.login(name,password,Constants.LOGININ);
                }
                break;
            default:
                break;
        }
    }

    private boolean check() {
        if (ed_name.getText().toString().equals("")||ed_password.getText().toString().equals("")){
            return false;
        } else  if (sharedPreferences.getString("ip","").equals("")||sharedPreferences.getString("port","").equals("")){
            return  false;
        }
        return true;
    }
    /**
     * 申请权限
     *
     */
    public void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), BAIDU_READ_PHONE_STATE);
        }
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BAIDU_READ_PHONE_STATE:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }
                    }
                    if (!deniedPermissions.isEmpty()) {
//                        Toast.makeText(getApplicationContext(),"权限未通过，请重新获取！",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
