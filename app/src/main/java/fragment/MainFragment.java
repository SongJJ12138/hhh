package fragment;

import Bean.Elevator;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.EleatorSelectDialog;
import utils.GlideImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.Activity.InspectSelectActivity;
import com.example.maintenanceelevator.R;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener {
    private EleatorSelectDialog eleatorSelectDialog;
    private TextView tv_dianti;
    private HttpModel httpModel;
    private Boolean isChooseEle=false;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 404:
                    Toast.makeText(getContext(),"未找到电梯数据，请检查网络！",Toast.LENGTH_SHORT).show();
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
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpModel=new HttpModel(getContext(),new HttpModel.HttpClientListener() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        tv_dianti =v.findViewById(R.id.tv_dianti);
        Banner banner = (Banner)v.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<Integer> images=new ArrayList<>();
        images.add(R.mipmap.lunbo);
        images.add(R.mipmap.lunbo2);
        images.add(R.mipmap.lunbo3);
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        Button bt_select =v.findViewById(R.id.bt_select);
        bt_select.setOnClickListener(this);
        Button bt_weibao =v.findViewById(R.id.bt_weibao);
        bt_weibao.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_select:
                httpModel.get("", Constants.GETALL_ELEVATOR);
                break;
            case R.id.bt_weibao:
                if (isChooseEle){
                    getActivity().startActivity(new Intent(getContext(), InspectSelectActivity.class));
                }else{
                    Toast.makeText(getContext(),"您还未选择需要维保的电梯",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void showdialog(List elevatorsList) {
        eleatorSelectDialog=new EleatorSelectDialog(getContext(), elevatorsList, new EleatorSelectDialog.onChooseElevatorListener() {
            @Override
            public void onChoose(String str,String str2) {
                tv_dianti.setText(str);
                onchooseElevator();
                eleatorSelectDialog.dismiss();
                EventBus.getDefault().postSticky(str2);
            }
        });
        eleatorSelectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        eleatorSelectDialog.show();
    }
    /**
     * 选择完电梯后操作
     */
    private void onchooseElevator() {
        isChooseEle=true;
    }
}
