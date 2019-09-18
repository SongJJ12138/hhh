package com.example.maintenanceelevator.Activity;

import Adapter.InspectAdapter;
import Bean.Inspect;
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
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InspectActivity extends BaseActivity {
private HttpModel httpModel;
private InspectAdapter adapter;
private   List<Inspect> InspectsList=new ArrayList<>();
@SuppressLint("HandlerLeak")
private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 404:
                Toast.makeText(getApplicationContext(),"未找到维保数据，请检查网络！",Toast.LENGTH_SHORT).show();
                InspectActivity.this.finish();
                break;
            case 100:
                String elevayors=(String) msg.obj;
                JSONArray array  = JSONObject.parseArray(elevayors);
                Iterator<Object> iterator = array.iterator();
                while(iterator.hasNext()) {
                    JSONObject obj = (JSONObject)  iterator.next();
                    Inspect inspect=new Inspect();
                    inspect.setContent((String) obj.get("content"));
                    inspect.setDemand((String) obj.get("demand"));
                    inspect.setOrder((int) obj.get("order"));
                    inspect.setPhoto((boolean) obj.get("photo"));
                    inspect.setPhoto_num((Object) obj.get("photo_num"));
                    InspectsList.add(inspect);
                }
                adapter.setList(InspectsList);
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
        setContentView(R.layout.activity_inspect);
        initView();
        Intent intent=getIntent();
        int state=intent.getIntExtra("type",0);
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
        String url="";
        if (state==0){
            Toast.makeText(getApplicationContext(),"未找到巡检数据",Toast.LENGTH_SHORT).show();
            this.finish();
        }else{
            switch (state){
                case 1:
                    url= Constants.BANYUE;
                break;
                case 2:
                    url= Constants.YUE;
                    break;
                case 3:
                    url= Constants.JIDU;
                    break;
                case 4:
                    url= Constants.BANNIAN;
                    break;
                case 5:
                    url= Constants.NIAN;
                    break;
                default:break;
            }
            if (!url.equals("")){
                httpModel.get("",url);
            }else{
                Toast.makeText(getApplicationContext(),"未找到巡检数据",Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }

    }

    private void initView() {
        TextView tv_back=findViewById(R.id.title_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectActivity.this.finish();
            }
        });
        RecyclerView rv_inspect=findViewById(R.id.ry_inspect);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        rv_inspect.setLayoutManager(layoutManager);
        adapter=new InspectAdapter(getApplicationContext(),InspectsList, new InspectAdapter.OnclickListener(){
            @Override
            public void onclick() {

            }
        });
        rv_inspect.setAdapter(adapter);
    }
}
