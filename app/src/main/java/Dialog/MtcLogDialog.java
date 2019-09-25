package Dialog;

import Adapter.ElevatorSelectAdapter;
import Adapter.LogSelectAdapter;
import Bean.Elevator;
import Bean.ElevatorInspect;
import Bean.Inspect;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.List;


public class MtcLogDialog extends Dialog{
    //绑定区镇村数据
    private List<ElevatorInspect.MtcLogsBean> logs=new ArrayList<>();
    private RecyclerView rv_log;
    private TextView tv_log;
    private Context context;
    private onChooseLogListener onChooseLogListener;
    public MtcLogDialog(Context context, List<ElevatorInspect.MtcLogsBean> logs, onChooseLogListener onChooseElevatorListener) {
        super(context, R.style.CommontMyDialog);
        this.context=context;
        this.logs=logs;
        this.onChooseLogListener=onChooseElevatorListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectlog);
        setCanceledOnTouchOutside(false);
        init();
    }
    private void init() {
        tv_log=findViewById(R.id.tv_log);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        rv_log=findViewById(R.id.rv_logs);
        rv_log.setLayoutManager(layoutManager);
        LogSelectAdapter adapter=new LogSelectAdapter(context,logs, new LogSelectAdapter.tvOnclickListener(){
            @Override
            public void onclick(String str) {
                tv_log.setText(str);
                tv_log.setTextColor(Color.parseColor("#269fef"));
                onChooseLogListener.onChoose(str);
            }
        });
        rv_log.setAdapter(adapter);
    }
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
    public interface onChooseLogListener{
        void onChoose(String str);
    }
}

