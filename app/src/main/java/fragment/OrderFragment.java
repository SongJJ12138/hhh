package fragment;


import Adapter.FragmentAdapter;
import Constans.Constants;
import Constans.HttpModel;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;

public class OrderFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private FragmentManager fm;
    private FragmentAdapter adapter;
    private ViewPager myvirwpager;
    private Button one, two, three,four,five;
    private  SharedPreferences sharedPreferences;
    // 指示标签的横坐标
    private float cursorX = 0;
    private Button[] ButtonArgs;
    private ArrayList<Fragment> list;
    private HttpModel httpModel;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    String mtc=(String) msg.obj;
                    initData(mtc);
                    break;
                case 400:break;
                case 404:
                    Toast.makeText(getContext(),"服务器异常，未找到维保数据！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm=getFragmentManager();
        httpModel=new HttpModel(getContext(), new HttpModel.HttpClientListener() {
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
        sharedPreferences=getContext().getSharedPreferences("config", getContext().MODE_PRIVATE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            String permission=sharedPreferences.getString("permission","");
            if (!permission.equals("")){
                if (permission.equals("mcompany")){
                    httpModel.getmtcsbymcompany(Constants.GET_MTC_BY_MCOMPANY);
                }else{
                    httpModel.getmtcsbymcompany(Constants.GET_MTC_BY_PCOMPANY);
                }
            }
        }
    }


    private void initData(String mtc) {
        ButtonArgs = new Button[] { one, two, three ,four,five};
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_show, container, false);
        myvirwpager = v.findViewById(R.id.myviewpager);
        one = v.findViewById(R.id.one);
        two = v.findViewById(R.id.two);
        three = v.findViewById(R.id.three);
        four = v.findViewById(R.id.four);
        five = v.findViewById(R.id.five);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        return v;
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
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        resetButtonColor();
        ButtonArgs[i].setTextColor(Color.BLACK);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
