package com.example.maintenanceelevator.Activity;

import Bean.Elevator;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.EleatorSelectDialog;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_dianti;
    private TextView tv_type;
    private TextView tv_time;
    private TextView tv_people;
    private Button bt_select;
    private Button bt_yulan;
    private RelativeLayout bt_gongdan;
    private RelativeLayout bt_weibao;
    private RelativeLayout bt_wode;
    private EleatorSelectDialog eleatorSelectDialog;
    private HttpModel httpModel;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 404:
                    Toast.makeText(getApplicationContext(),"未找到电梯数据，请检查网络！",Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    String elevayors=(String) msg.obj;
                    List<Elevator> elevatorsList=new ArrayList<>();
                    JSONArray array  = JSONObject.parseArray(elevayors);
                    Iterator<Object> iterator = array.iterator();
                    while(iterator.hasNext()) {
                        JSONObject obj = (JSONObject)  iterator.next();
                        Elevator elevator=new Elevator();
                        elevator.setName((String) obj.get("name"));
                        elevator.setPk((String) obj.get("pk"));
                        elevator.setSn((String) obj.get("sn"));
                        elevatorsList.add(elevator);
                    }
                    showdialog(elevatorsList);
                    break;
                    default:
                        break;
            };
        }
    };

    private void showdialog(List elevatorsList) {
        eleatorSelectDialog=new EleatorSelectDialog(HomeActivity.this, elevatorsList, new EleatorSelectDialog.onChooseElevatorListener() {
            @Override
            public void onChoose(String str) {
                tv_dianti.setText(str);
                onchooseElevator();
                eleatorSelectDialog.dismiss();
            }
        });
        eleatorSelectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        eleatorSelectDialog.show();
    }

    /**
     * 选择完电梯后操作
     */
    private void onchooseElevator() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        httpModel=new HttpModel(new HttpModel.HttpClientListener() {
            @Override
            public void onError() {
                handler.sendEmptyMessage(404);
            }

            @Override
            public void onSuccess(String obj) {
                Message msg=new Message();
                msg.obj=obj;
                msg.what=100;
                handler.sendMessage(msg);
            }
        });
    }

    private void initView() {
        tv_dianti = findViewById(R.id.tv_dianti);
        tv_type = findViewById(R.id.tv_type);
        tv_time = findViewById(R.id.tv_time);
        tv_people = findViewById(R.id.tv_people);
        bt_select = findViewById(R.id.bt_select);
        bt_yulan = findViewById(R.id.bt_yulan);
        bt_gongdan = findViewById(R.id.bt_gongdan);
        bt_weibao = findViewById(R.id.bt_weibao);
        bt_wode = findViewById(R.id.bt_wode);
        bt_select.setOnClickListener(this);
        bt_yulan .setOnClickListener(this);
        bt_gongdan .setOnClickListener(this);
        bt_weibao .setOnClickListener(this);
        bt_wode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //选择电梯
            case R.id.bt_select:
                httpModel.get("", Constants.GETALL_ELEVATOR);
                break;
            //预览维保数据
            case R.id.bt_yulan:

                break;
            //工单管理
            case R.id.bt_gongdan:

                break;
            //维保
            case R.id.bt_weibao:
                if (!tv_dianti.getText().toString().equals("请选择对应电梯！")){
                    startActivity(new Intent(HomeActivity.this,SelectActivity.class));
                }else {
                    Toast.makeText(getApplicationContext(),"您还未选择电梯！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_wode:

                break;
        }
    }
}
