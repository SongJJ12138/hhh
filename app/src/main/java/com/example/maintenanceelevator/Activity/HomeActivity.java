package com.example.maintenanceelevator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.maintenanceelevator.R;
import com.next.easynavigation.view.EasyNavigationBar;
import fragment.ElevatorFragment;
import fragment.MainFragment;
import fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ElevatorFragment.OnItemclickListener {
    private String[] tabText = {"首页", "工单", "维保","救援", "我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.main_off, R.mipmap.gd_off, R.mipmap.wb_off, R.mipmap.jiu_off,R.mipmap.mine_off};
    //选中时icon
    private int[] selectIcon = {R.mipmap.main_on, R.mipmap.gd_on, R.mipmap.wb_on, R.mipmap.jiu_on,R.mipmap.ming_on};
    private List<android.support.v4.app.Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

    }

    private void initView() {
        EasyNavigationBar navigationBar = findViewById(R.id.navigationBar);
        fragments.add(new MainFragment());
        fragments.add(new OrderFragment());
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .build();
        navigationBar.setMsgPointCount(2, 109);
        navigationBar.setMsgPointCount(0, 5);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //选择电梯

//            //预览维保数据
//            case R.id.bt_yulan:
//
//                break;
//            //工单管理
//            case R.id.bt_gongdan:
//                startActivity(new Intent(HomeActivity.this,ShowActivity.class));
//                break;
//            //维保
//            case R.id.bt_weibao:
//                if (!tv_dianti.getText().toString().equals("请选择对应电梯！")){
//                    Intent intent=new Intent(HomeActivity.this,SelectActivity.class);
//                    intent.putExtra("pk",pk);
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(getApplicationContext(),"您还未选择电梯！",Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.bt_wode:
//                startActivity(new Intent(HomeActivity.this,MineActivity.class));
//                break;
        }
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
