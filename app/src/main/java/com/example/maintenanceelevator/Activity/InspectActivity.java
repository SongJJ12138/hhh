package com.example.maintenanceelevator.Activity;

import Adapter.InspectAdapter;
import Bean.ElevatorInspect;
import Bean.Inspect;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.EleatorSelectDialog;
import Dialog.MtcLogDialog;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.maintenanceelevator.R;

public class InspectActivity extends BaseActivity {
private HttpModel httpModel;
private ElevatorInspect inspect;
private TextView rv_title;
private TextView tv_diantiSn;
private TextView tv_dianti;
private TextView rv_content;
private LinearLayout layout_addPic;
private int checked=0;
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
                inspect=(ElevatorInspect) msg.obj;
                showView(inspect);
                break;
            default:
                break;
        };
    }

    private void showView(ElevatorInspect inspect) {
        tv_dianti.setText(inspect.getElevator_name());
        tv_diantiSn.setText(inspect.getElevator_sn());
        for (int i=0;i<inspect.getMtc_logs().size();i++){
            if (!inspect.getMtc_logs().get(i).isChecked()){
                checked=i;
                rv_title.setText(inspect.getMtc_logs().get(i).getContent());
                rv_content.setText(inspect.getMtc_logs().get(i).getDemand());
            }
            if (!inspect.getMtc_logs().get(i).isPhoto()){
                layout_addPic.setVisibility(View.GONE);
            }
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        httpModel=new HttpModel(getApplicationContext(),new HttpModel.HttpClientListener() {
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
        httpModel.post("",Constants.ADD_MTC);
        initView();
    }


    private void initView() {
        layout_addPic=findViewById(R.id.layout_addPic);
        Button bt_select=findViewById(R.id.bt_select);
        rv_content=findViewById(R.id.rv_content);
        rv_title=findViewById(R.id.rv_title);
        tv_diantiSn=findViewById(R.id.tv_diantiSn);
        tv_dianti=findViewById(R.id.tv_dianti);
        TextView tv_back=findViewById(R.id.title_back);
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MtcLogDialog mtcLogDialog=new MtcLogDialog(InspectActivity.this, elevatorsList, new MtcLogDialog.onChooseLogListener() {
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
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectActivity.this.finish();
            }
        });
    }
}
