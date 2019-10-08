package com.example.maintenanceelevator.Activity;

import Constans.Constants;
import Constans.HttpModel;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.maintenanceelevator.R;
import fragment.ElevatorFragment;
import Adapter.FragmentAdapter;

import java.util.ArrayList;

public class ShowActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener,ElevatorFragment.OnItemclickListener {

    private ViewPager myvirwpager;
    private Button one, two, three,four,five;
    // 指示标签的横坐标
    private float cursorX = 0;
    private Button[] ButtonArgs;
    private ArrayList<Fragment> list;
    private FragmentAdapter adapter;
    private HttpModel httpModel;
    private FragmentManager fm;
    private TextView tv_back;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 200:
                   String mtc=(String) msg.obj;
                   init(mtc);
                   break;
               case 400:break;
               case 404:
                   Toast.makeText(getApplicationContext(),"服务器异常，未找到维保数据！",Toast.LENGTH_SHORT).show();
                   ShowActivity.this.finish();
                   break;
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        fm=getSupportFragmentManager();
        httpModel=new HttpModel(getApplicationContext(), new HttpModel.HttpClientListener() {
            @Override
            public void onError() {
                handler.sendEmptyMessage(404);
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
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("config", getApplicationContext().MODE_PRIVATE);;
        String permission=sharedPreferences.getString("permission","");
        if (!permission.equals("")){
            if (permission.equals("mcompany")){
                httpModel.getmtcsbymcompany(Constants.GET_MTC_BY_MCOMPANY);
            }else{
                httpModel.getmtcsbymcompany(Constants.GET_MTC_BY_PCOMPANY);
            }
        }

    }

    private void init(String mtc) {
        myvirwpager = (ViewPager) findViewById(R.id.myviewpager);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        tv_back=findViewById(R.id.title_back);
        ButtonArgs = new Button[] { one, two, three ,four,five};
        tv_back.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        list = new ArrayList<Fragment>();
        for (int i=0;i<5;i++){
            Fragment fragment=new ElevatorFragment();
            Bundle bundle = new Bundle();
            bundle.putString("mtc", mtc);
            bundle.putString("type", i+"");
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        adapter = new FragmentAdapter(fm, list);
        myvirwpager.setAdapter(adapter);
        myvirwpager.setOnPageChangeListener(this);
        resetButtonColor();
        one.setTextColor(Color.BLACK);
    }

    // 设置按钮颜色
    public void resetButtonColor() {
        one.setBackgroundColor(Color.parseColor("#269fef"));
        two.setBackgroundColor(Color.parseColor("#269fef"));
        three.setBackgroundColor(Color.parseColor("#269fef"));
        four.setBackgroundColor(Color.parseColor("#269fef"));
        five.setBackgroundColor(Color.parseColor("#269fef"));
        one.setTextColor(Color.WHITE);
        two.setTextColor(Color.WHITE);
        three.setTextColor(Color.WHITE);
        four.setTextColor(Color.WHITE);
        five.setTextColor(Color.WHITE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                myvirwpager.setCurrentItem(0);
                break;
            case R.id.two:
                myvirwpager.setCurrentItem(1);
                break;
            case R.id.three:
                myvirwpager.setCurrentItem(2);
                break;
            case R.id.four:
                myvirwpager.setCurrentItem(3);
                break;
            case R.id.five:
                myvirwpager.setCurrentItem(4);
                break;
            case R.id.title_back:
                this.finish();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        resetButtonColor();
        ButtonArgs[arg0].setTextColor(Color.BLACK);

    }

    @Override
    public void onComitclick(String str) {
        Intent intent=new Intent(ShowActivity.this, InspectActivity.class);
        intent.putExtra("change",str);
        intent.putExtra("pk","");
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onConfirmclick(String str) {
        Intent intent=new Intent(ShowActivity.this, ConfirmActivity.class);
        intent.putExtra("change",str);
        startActivity(intent);
        this.finish();
    }
}
