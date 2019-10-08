package com.example.maintenanceelevator.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.maintenanceelevator.R;

public class updateIpActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_ip;
    private EditText ed_duankou;
    private Button bt_next;
    private TextView tv_back;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ip);
        initView();
        sharedPreferences=getApplicationContext().getSharedPreferences("config", getApplicationContext().MODE_PRIVATE);
        String ip=sharedPreferences.getString("ip","");
        String duankou=sharedPreferences.getString("port","");
        if (!ip.equals("")&&!duankou.equals("")){
            ed_ip.setText(ip);
            ed_duankou.setText(duankou);
        }
    }

    private void initView() {
        ed_ip=findViewById(R.id.ed_ip);
        ed_duankou=findViewById(R.id.ed_duankou);
        bt_next=findViewById(R.id.bt_ip);
        tv_back=findViewById(R.id.title_back);
        bt_next.setOnClickListener(this);
        tv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ip:
                if (!ed_duankou.getText().toString().equals("")&&!ed_ip.getText().toString().equals("")){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("ip",ed_ip.getText().toString());
                    editor.putString("port",ed_duankou.getText().toString());
                    editor.commit();
                    this.finish();
                }else
                    Toast.makeText(getApplicationContext(),"IP地址或端口号为空！",Toast.LENGTH_SHORT).show();

                break;
            case R.id.title_back:
                this.finish();
                break;
            default:break;
        }
    }
}
