package com.example.maintenanceelevator.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.maintenanceelevator.R;
import fragment.ElevatorFragment;
import fragment.FragmentAdapter;

import java.util.ArrayList;

public class ShowActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    private ViewPager myvirwpager;
    private Button one, two, three,four,five;
    // 指示标签的横坐标
    private float cursorX = 0;
    private Button[] ButtonArgs;
    private ArrayList<Fragment> list;
    private FragmentAdapter adapter;
    FragmentManager fm=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Init();
    }

    private void Init() {
        myvirwpager = (ViewPager) findViewById(R.id.myviewpager);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        ButtonArgs = new Button[] { one, two, three ,four,five};
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        list = new ArrayList<Fragment>();
        for (int i=0;i<5;i++){
            Fragment fragment=new ElevatorFragment();
            Bundle bundle = new Bundle();
            String strValue = "这是第"+i+1+"个界面";
            bundle.putString("str", strValue);
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
}
