package fragment;
;
import Adapter.HelpOrderAdapter;
import Bean.helpOrder;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.ChooseDialog;
import Dialog.PostDialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maintenanceelevator.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment implements PostDialog.onChooseListener {
    private HttpModel httpModel;
    private ChooseDialog chooseDialog;
    private PostDialog postDialog;
    private RecyclerView rv_selectType;
    private HelpOrderAdapter adapter;
    private List<helpOrder> helpOrderList=new ArrayList<>();
    private  helpOrder order;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 400:
                    Toast.makeText(getContext(),"由于网络原因上传失败！",Toast.LENGTH_LONG).show();
                    break;
                case 200:
                    if (postDialog.isShowing()){
                        postDialog.dismiss();
                    }else {
                        chooseDialog.dismiss();
                    }
                    updateDate();
                    Toast.makeText(getContext(),"救援状态已改变！",Toast.LENGTH_LONG).show();
                    break;
                case 300:
                    List<helpOrder> list= (List<helpOrder>) msg.obj;
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    break;
                case 100:
                    order= (helpOrder) msg.obj;
                    if (order.getRescue_progress().equals("rescue_progress_waiting")){
                        chooseDialog.show();
                    }else if(order.getRescue_progress().equals("rescue_progress_done")){
                        Toast.makeText(getContext(),"救援已经完成，无需继续进行！",Toast.LENGTH_LONG).show();
                    }else{
                        switch (order.getRescue_progress()){
                            case "rescue_progress_responded":
                                postDialog.settitle("是否正在路上");
                                break;
                            case "rescue_progress_on_the_way":
                                postDialog.settitle("是否到达");
                                break;
                            case "rescue_progress_arrived":
                                postDialog.settitle("是否救援");
                                break;
                            case "rescue_progress_rescuing":
                                postDialog.settitle("是否完成");
                                break;
                        }
                        postDialog.show();
                    }
                    break;
                default:
                    break;
            };
        }
    };

    private void updateDate() {
        httpModel.getHelpOrder(Constants.GET_HELPORDER);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(sticky = true)
    public void GetOrder(Object helporders) {
        helpOrderList= (List<helpOrder>) helporders;
        adapter.setList(helpOrderList);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        chooseDialog=new ChooseDialog(getActivity());
        chooseDialog.settitle("是否要相应此次救援？");
        chooseDialog.setListener(this);
        postDialog=new PostDialog(getActivity());
        postDialog.setListener(this);
        httpModel=new HttpModel(getContext(),new HttpModel.HttpClientListener(){
            @Override
            public void onError() {
                handler.sendEmptyMessage(400);
            }

            @Override
            public void onSuccess(Object obj) {
                Message msg=new Message();
                msg.obj=obj;
                msg.what=300;
                handler.sendMessage(msg);
            }

            @Override
            public void onaddWithCommit(String type) {
                handler.sendEmptyMessage(200);
            }

        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);
        rv_selectType=v.findViewById(R.id.rv_selectType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        rv_selectType.setLayoutManager(layoutManager);
        adapter=new HelpOrderAdapter(getContext(),helpOrderList, new HelpOrderAdapter.OnclickListener(){
            @Override
            public void onclick(helpOrder order) {
                Message msg=new Message();
                msg.obj=order;
                msg.what=100;
               handler.sendMessage(msg);
            }
        });
        rv_selectType.setAdapter(adapter);
        return v;
    }

    @Override
    public void onChoose(String desc) {
        switch (order.getRescue_progress()){
            case "rescue_progress_waiting":
               order.setRescue_progress("rescue_progress_responded");
                break;
            case "rescue_progress_responded":
                order.setRescue_progress("rescue_progress_on_the_way");
                break;
            case "rescue_progress_on_the_way":
                order.setRescue_progress("rescue_progress_arrived");
                break;
            case "rescue_progress_arrived":
                order.setRescue_progress("rescue_progress_rescuing");
                break;
            case "rescue_progress_rescuing":
                order.setRescue_progress("rescue_progress_done");
                break;
        }
        httpModel.updateHelp(order, desc, Constants.UPDATE_HELP);
    }
}
