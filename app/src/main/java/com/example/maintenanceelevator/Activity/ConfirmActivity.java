package com.example.maintenanceelevator.Activity;

import Bean.ElevatorInspect;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.MtcLogDialog;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.maintenanceelevator.R;

public class ConfirmActivity extends BaseActivity implements View.OnClickListener {
    private ElevatorInspect inspect;
    private  HttpModel httpModel;
    private MtcLogDialog mtcLogDialog;
    private TextView tv_showlog;
    private EditText ed_remark;
    private LinearLayout layout_showPic;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    inspect= (ElevatorInspect) msg.obj;
                    initView(inspect);
                    break;
                case 400:
                    Toast.makeText(getApplicationContext(),"未找到维保工单，请检查网络",Toast.LENGTH_SHORT).show();
                    ConfirmActivity.this.finish();
                    break;
                case 300:
                    Toast.makeText(getApplicationContext(),"审核通过",Toast.LENGTH_SHORT).show();
                    ConfirmActivity.this.finish();
                    break;
                    default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Intent intent=getIntent();
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
                Message msg=new Message();
                msg.what=300;
                handler.sendMessage(msg);
            }
        });
        String pk=intent.getStringExtra("change");
        if (pk!=null&&!pk.equals("")){
            httpModel.getmtc(pk, Constants.GET_MTC);
        }
    }
    private void initView(ElevatorInspect inspect) {
        //初始化页面，默认第一项
        TextView tv_back = findViewById(R.id.title_back);
        TextView tv_confirm = findViewById(R.id.tv_commit);
        TextView tv_dianti = findViewById(R.id.tv_dianti);
        TextView tv_diantiSn = findViewById(R.id.tv_diantiSn);
        TextView tv_type = findViewById(R.id.tv_type);
        TextView tv_mCompany = findViewById(R.id.tv_mCompany);
        TextView tv_pCompany = findViewById(R.id.tv_pCompany);
        TextView tv_createTime = findViewById(R.id.tv_createTime);
        ed_remark=findViewById(R.id.ed_inspect_beizhu);
        layout_showPic=findViewById(R.id.layout_showPic);
        tv_showlog = findViewById(R.id.tv_showlog);
        Button bt_select = findViewById(R.id.bt_select);
        tv_showlog.setText(inspect.getMtc_logs().get(0).getContent());
        ed_remark.setText(inspect.getMtc_logs().get(0).getLog());
        if (inspect.getMtc_logs().get(0).getPhoto_num()==null){
            layout_showPic.setVisibility(View.GONE);
        }
        tv_back.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        bt_select.setOnClickListener(this);
        tv_dianti.setText(inspect.getElevator_name());
        tv_diantiSn.setText(inspect.getElevator_sn());
        tv_type.setText(inspect.getType());
        tv_mCompany.setText(inspect.getMcompany_staff());
        tv_pCompany.setText(inspect.getPcompany_staff());
        tv_createTime.setText(inspect.getSubmitted_date()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                this.finish();
                break;
            case R.id.tv_commit:
                httpModel.confirmMtc(Constants.CONFIRM,inspect.getPk());
                break;
            case R.id.bt_select:
                mtcLogDialog=new MtcLogDialog(ConfirmActivity.this, inspect.getMtc_logs(), new MtcLogDialog.onChooseLogListener() {
                    @Override
                    public void onChoose(String str) {
                        tv_showlog.setText(str);
                        changeView(str);
                        mtcLogDialog.dismiss();
                    }
                });
                mtcLogDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mtcLogDialog.show();
                break;
                default:break;
        }
    }

    private void changeView(String str) {
        for (int i=0;i<inspect.getMtc_logs().size();i++){
            if (inspect.getMtc_logs().get(i).getContent().equals(str)){
                tv_showlog.setText(inspect.getMtc_logs().get(i).getContent());
                ed_remark.setText(inspect.getMtc_logs().get(i).getLog());
                if (!inspect.getMtc_logs().get(i).isPhoto()){
                    layout_showPic.setVisibility(View.GONE);
                }
                return;
            }
        }
    }
}
