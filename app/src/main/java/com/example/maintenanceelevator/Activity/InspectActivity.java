package com.example.maintenanceelevator.Activity;

import Bean.ElevatorInspect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import utils.*;
import Adapter.PictureSelectAdapter;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.MtcLogDialog;
import Dialog.RemindDialog;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.List;

public class InspectActivity extends BaseActivity implements View.OnClickListener {
    private final static int REQUCODE_PAIZHAO=0x01;
    private RemindDialog remindDialog;
    private HttpModel httpModel;
    private ElevatorInspect inspect;
    private TextView rv_title;
    private TextView tv_diantiSn;
    private TextView tv_dianti;
    private TextView rv_content;
    private TextView tv_commit;
    private LinearLayout layout_addPic;
    private MtcLogDialog mtcLogDialog;
    private TextView tv_showlog;
    private EditText ed_remark;
    private Intent intent;
    private int checked=0;
    private int Maxsize;
    private PictureSelectAdapter pictureSelectAdapter;
    private List<Bitmap> imList=new ArrayList<>();
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
            case 200:
                inspect.getMtc_logs().get(checked).setChecked(true);
                showView(inspect);
                break;
            case 300:
                Toast.makeText(getApplicationContext(),"提交成功！",Toast.LENGTH_SHORT).show();
               InspectActivity.this.finish();
                break;
            default:
                break;
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

            @Override
            public void onaddWithCommit(String type) {
                if (type.equals("ADDLOG")){
                    handler.sendEmptyMessage(200);
                }else{
                    handler.sendEmptyMessage(300);
                }
            }


        });
        intent=getIntent();
        if (intent.getStringExtra("change")!=null&&!intent.getStringExtra("change").equals("")){
            httpModel.getmtc(intent.getStringExtra("change"),Constants.GET_MTC);
        }else {
            String pk=intent.getStringExtra("pk");
            if (pk!=null&&!pk.equals("")){
                String type=intent.getStringExtra("type");
                httpModel.addmtc(pk,Constants.ADD_MTC,type);
            }
        }
        initView();
    }


    private void initView() {
        ImageView imtake = findViewById(R.id.im_paizhao);
        imtake.setOnClickListener(this);
        remindDialog=new RemindDialog(InspectActivity.this);
        ed_remark=findViewById(R.id.ed_inspect_beizhu);
        tv_commit=findViewById(R.id.tv_commit);
        tv_showlog=findViewById(R.id.tv_showlog);
        layout_addPic=findViewById(R.id.layout_addPic);
        Button bt_select=findViewById(R.id.bt_select);
        rv_content=findViewById(R.id.rv_content);
        rv_title=findViewById(R.id.rv_title);
        tv_diantiSn=findViewById(R.id.tv_diantiSn);
        tv_dianti=findViewById(R.id.tv_dianti);
        TextView tv_back=findViewById(R.id.title_back);
        tv_commit.setOnClickListener(this);
        bt_select.setOnClickListener(this);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectActivity.this.finish();
            }
        });
        RecyclerView rv_pic=findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));
        pictureSelectAdapter=new PictureSelectAdapter(InspectActivity.this,imList);
        rv_pic.setAdapter(pictureSelectAdapter);
        rv_pic.setHasFixedSize(true);
        rv_pic.setNestedScrollingEnabled(false);
    }
    private void showView(ElevatorInspect inspect) {
        tv_dianti.setText(inspect.getElevator_name());
        tv_diantiSn.setText(inspect.getElevator_sn());
        for (int i=0;i<inspect.getMtc_logs().size();i++){
            if (!inspect.getMtc_logs().get(i).isChecked()){
                tv_showlog.setText(inspect.getMtc_logs().get(i).getContent());
                checked=i;
                ed_remark.setText("");
                rv_title.setText(inspect.getMtc_logs().get(i).getContent());
                rv_content.setText(inspect.getMtc_logs().get(i).getDemand());
                if (!inspect.getMtc_logs().get(i).isPhoto()){
                    layout_addPic.setVisibility(View.GONE);
                }else{
                    Maxsize=(int)inspect.getMtc_logs().get(i).getPhoto_num();
                }
                return;
            }
        }
        showCommit();
    }

    private void showCommit() {
        AlertDialog.Builder builder=new AlertDialog.Builder(InspectActivity.this);
        builder.setMessage("所有工单已全部完成，是否提交维保工单？");
        builder.setTitle("提交数据");

        //添加AlertDialog.Builder对象的setPositiveButton()方法
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpModel.commitMtc(inspect.getPk(),Constants.SUBMIT_MTC);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void changeView(String str) {
        for (int i=0;i<inspect.getMtc_logs().size();i++){
            if (inspect.getMtc_logs().get(i).getContent().equals(str)){
                tv_showlog.setText(inspect.getMtc_logs().get(i).getContent());
                checked=i;
                rv_title.setText(inspect.getMtc_logs().get(i).getContent());
                rv_content.setText(inspect.getMtc_logs().get(i).getDemand());
                ed_remark.setText(inspect.getMtc_logs().get(i).getLog());
                if (!inspect.getMtc_logs().get(i).isPhoto()){
                    layout_addPic.setVisibility(View.GONE);
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //拍照回调
            case REQUCODE_PAIZHAO:
                if (data!=null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Bitmap map = TakePhoto.compressImage(bitmap);
                    pictureSelectAdapter.addPic(map);
                    pictureSelectAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_paizhao:
                if (pictureSelectAdapter.getPic().size()<Maxsize){
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(openCameraIntent,REQUCODE_PAIZHAO);
                }else{
                    remindDialog.setContent("当前需要照片数量为"+Maxsize+",请长按删除对应照片后继续添加！");
                    remindDialog.setTitle("照片以达所需最大");
                    remindDialog.show();
                }
                break;
            case R.id.tv_commit:
                String remark=ed_remark.getText().toString().trim();
                if (remark.equals("")){
                    Toast.makeText(getApplicationContext(),"备注未填写！",Toast.LENGTH_LONG).show();
                }else{
                    if (inspect.getMtc_logs().get(checked).isPhoto()){
                        if (pictureSelectAdapter.getPic().size()!=Maxsize){
                            Toast.makeText(getApplicationContext(),"照片数量不符 "+pictureSelectAdapter.getPic().size()+"/"+Maxsize,Toast.LENGTH_LONG).show();
                        }else{
                            httpModel.postLogByphoto(remark,inspect.getMtc_logs().get(checked).getPk(),Constants.ADD_LOG,pictureSelectAdapter.getPic());
                        }
                    }else{
                        httpModel.postLogNophoto(remark,inspect.getMtc_logs().get(checked).getPk(),Constants.ADD_LOG);
                    }
                }
                break;
            case R.id.bt_select:
                mtcLogDialog=new MtcLogDialog(InspectActivity.this, inspect.getMtc_logs(), new MtcLogDialog.onChooseLogListener() {
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
}
