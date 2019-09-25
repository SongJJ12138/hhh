package com.example.maintenanceelevator.Activity;

import Constans.Constants;
import Constans.HttpModel;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_name;
    private EditText ed_password;
    private CheckBox cb_remberword;
    private Button bt_forget;
    private Button bt_login;
    private SharedPreferences.Editor editor;
    private Boolean isCheckRember=false;
    private HttpModel model;
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
            };
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        });
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("config", getApplicationContext().MODE_PRIVATE);
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
                //忘记密码页面
                break;
            case R.id.login_btn:
                if (check()){
                    String name=ed_name.getText().toString();
                    String password=ed_password.getText().toString();
                    model.login("mcompany_staff","useruser",Constants.LOGININ);
                }
                break;
            default:
                break;
        }
    }

    private boolean check() {
        if (ed_name.getText().toString().equals("")||ed_password.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}
