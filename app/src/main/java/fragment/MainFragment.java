package fragment;

import Bean.Elevator;
import Constans.Constants;
import Constans.HttpModel;
import Dialog.EleatorSelectDialog;
import android.annotation.SuppressLint;
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
import com.example.maintenanceelevator.R;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener {
    private EleatorSelectDialog eleatorSelectDialog;
    private TextView tv_dianti;
    private HttpModel httpModel;
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
//        TextView tv_type = findViewById(R.id.tv_type);
//        TextView tv_time = findViewById(R.id.tv_time);
//        TextView tv_people = findViewById(R.id.tv_people);
        Button bt_select =v.findViewById(R.id.bt_select);
        bt_select.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_select:
                httpModel.get("", Constants.GETALL_ELEVATOR);
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
    }
}
