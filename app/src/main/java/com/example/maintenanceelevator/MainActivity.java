package com.example.maintenanceelevator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_name;
    private EditText ed_password;
    private CheckBox cb_remberword;
    private Button bt_forget;
    private Button bt_login;
    private SharedPreferences.Editor editor;
    private Boolean isCheckRember=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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
                    if (ed_name.getText().toString().equals("admin")||ed_password.getText().toString().equals("test")){
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                    }
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
