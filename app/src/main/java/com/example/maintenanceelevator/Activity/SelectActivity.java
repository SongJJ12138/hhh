package com.example.maintenanceelevator.Activity;

import Adapter.ElevatorTypeAdapter;
import Adapter.InspectAdapter;
import Bean.Elevator;
import Bean.ElevatorType;
import Constans.Constants;
import Constans.HttpModel;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectActivity extends BaseActivity implements View.OnClickListener {
private int state=0;
private RecyclerView rv_selectType;
private ElevatorTypeAdapter adapter;
private HttpModel httpModel;
private List<ElevatorType> typeList=new ArrayList<>();
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
                    JSONArray array  = JSONObject.parseArray(elevayors);
                    Iterator<Object> iterator = array.iterator();
                    while(iterator.hasNext()) {
                        JSONObject obj = (JSONObject)  iterator.next();
                        ElevatorType elevatorType=new ElevatorType();
                        elevatorType.setName((String) obj.get("name"));
                        elevatorType.setVerbose_name((String) obj.get("verbose_name"));
                        typeList.add(elevatorType);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            };
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        httpModel=new HttpModel(getApplicationContext(),new HttpModel.HttpClientListener(){
            @Override
            public void onError() {
                handler.sendEmptyMessage(404);
            }

            @Override
            public void onSuccess(Object obj) {
                Message msg=new Message();
                msg.obj=obj;
                msg.what=100;
                handler.sendMessage(msg);
            }
        });
        httpModel.get("", Constants.GETELEVATOR_TYPE);
        initView();
    }


    private void initView() {
        TextView tv_back = findViewById(R.id.title_back);
        Button bt_next = findViewById(R.id.btn_next);
        tv_back.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        rv_selectType=findViewById(R.id.rv_selectType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        rv_selectType.setLayoutManager(layoutManager);
        adapter=new ElevatorTypeAdapter(getApplicationContext(),typeList, new ElevatorTypeAdapter.OnclickListener(){
            @Override
            public void onclick(int id) {
                state=id;
            }
        });
        rv_selectType.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                this.finish();
                break;
            case R.id.btn_next:
                if (state==0){
                    Toast.makeText(getApplicationContext(),"您还未选择需要维保项！",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(SelectActivity.this,InspectActivity.class);
                    startActivity(intent);
                }
                break;
                default:break;
        }
    }
}
