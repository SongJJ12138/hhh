package com.example.maintenanceelevator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.maintenanceelevator.R;
import com.next.easynavigation.view.EasyNavigationBar;

import org.greenrobot.eventbus.EventBus;

import Bean.helpOrder;
import Constans.Constants;
import Constans.HttpModel;
import fragment.ElevatorFragment;
import fragment.HelpFragment;
import fragment.MainFragment;
import fragment.OrderFragment;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements ElevatorFragment.OnItemclickListener {
    private HttpModel httpModel;
    private String[] tabText = {"首页", "工单", "救援", "我的"};
    private  EasyNavigationBar navigationBar;
    //未选中icon
    private int[] normalIcon = {R.mipmap.main_off, R.mipmap.gd_off, R.mipmap.jiu_off,R.mipmap.mine_off};
    //选中时icon
    private int[] selectIcon = {R.mipmap.main_on, R.mipmap.gd_on, R.mipmap.jiu_on,R.mipmap.ming_on};
    private List<android.support.v4.app.Fragment> fragments = new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    List<helpOrder> list= (List<helpOrder>) msg.obj;
                    int i=0;
                    for (helpOrder order:list){
                        if (!order.getRescue_progress().equals("rescue_progress_done")){
                            i++;
                        };
                    }
                    navigationBar.setMsgPointCount(2, i);
                    break;
                case 400:
                    Toast.makeText(getApplicationContext(),"未找到救援数据，请检查网络！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        httpModel=new HttpModel(getApplicationContext(), new HttpModel.HttpClientListener() {
            @Override
            public void onError() {
               handler.sendEmptyMessage(400);
            }

            @Override
            public void onSuccess(Object obj) {
                EventBus.getDefault().postSticky(obj);
                Message msg=new Message();
                msg.obj=obj;
                msg.what=200;
                handler.sendMessage(msg);
            }

            @Override
            public void onaddWithCommit(String type) {

            }
        });
        initView();
        httpModel.getHelpOrder(Constants.GET_HELPORDER);

    }

    private void initView() {
        navigationBar = findViewById(R.id.navigationBar);
        fragments.add(new MainFragment());
        fragments.add(new OrderFragment());
        fragments.add(new HelpFragment());
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .build();
    }


    @Override
    public void onComitclick(String str) {
        Intent intent=new Intent(getApplicationContext(), InspectActivity.class);
        intent.putExtra("change",str);
        intent.putExtra("pk","");
        startActivity(intent);
    }

    @Override
    public void onConfirmclick(String str) {
        Intent intent=new Intent(getApplicationContext(), ConfirmActivity.class);
        intent.putExtra("change",str);
        startActivity(intent);
    }
}
