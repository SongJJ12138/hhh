package Dialog;

import Adapter.ElevatorSelectAdapter;
import Bean.Elevator;
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


public class EleatorSelectDialog extends Dialog{
    private List<Elevator> elevators=new ArrayList<>();
    private RecyclerView rv_Elevator;
    private TextView tv_Elevator;
    private Context context;
    private onChooseElevatorListener onChooseElevatorListener;
    public EleatorSelectDialog(Context context, List<Elevator> elevators, onChooseElevatorListener onChooseElevatorListener) {
        super(context, R.style.CommontMyDialog);
        this.context=context;
        this.elevators=elevators;
        this.onChooseElevatorListener=onChooseElevatorListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectvillage);
        setCanceledOnTouchOutside(false);
        init();
    }
    private void init() {
        tv_Elevator=findViewById(R.id.tv_Elevator);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        rv_Elevator=findViewById(R.id.rv_Elevator);
        rv_Elevator.setLayoutManager(layoutManager);
        ElevatorSelectAdapter adapter=new ElevatorSelectAdapter(context,elevators, new ElevatorSelectAdapter.tvOnclickListener(){
            @Override
            public void onclick(String str,String pk) {
                tv_Elevator.setText(str);
                tv_Elevator.setTextColor(Color.parseColor("#269fef"));
                onChooseElevatorListener.onChoose(str,pk);
            }
        });
        rv_Elevator.setAdapter(adapter);
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
    public interface onChooseElevatorListener{
        void onChoose(String str,String pk);
    }
}
