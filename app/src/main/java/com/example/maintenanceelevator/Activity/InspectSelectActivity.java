package com.example.maintenanceelevator.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Adapter.ElevatorTypeAdapter;
import Bean.ElevatorType;
import Constans.Constants;
import Constans.HttpModel;

public class InspectSelectActivity extends BaseActivity {
    private HttpModel httpModel;
    private RecyclerView rv_selectType;
    private ElevatorTypeAdapter adapter;
    private String pk;
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
    @Subscribe(sticky = true)
    public void GetPK(String pk) {
        this.pk=pk;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        EventBus.getDefault().register(this);
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

            @Override
            public void onaddWithCommit(String type) {

            }

        });
        httpModel.get("", Constants.GETELEVATOR_TYPE);
        initView();
    }

    private void initView() {
        rv_selectType=findViewById(R.id.rv_selectType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        rv_selectType.setLayoutManager(layoutManager);
        RelativeLayout bt_back=findViewById(R.id.title_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InspectSelectActivity.this.finish();
            }
        });
        adapter=new ElevatorTypeAdapter(getApplicationContext(),typeList, new ElevatorTypeAdapter.OnclickListener(){
            @Override
            public void onclick(String type) {
                Intent intent=new Intent(getApplicationContext(), InspectActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("pk",pk);
                intent.putExtra("change","");
                startActivity(intent);
            }
        });
        rv_selectType.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
