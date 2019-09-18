package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.maintenanceelevator.R;

public class ElevatorInspectView extends LinearLayout implements View.OnClickListener {
    private String title;
    private String content;
    private Boolean isVisibile=true;
    private Boolean isGood=true;
    private  LinearLayout layout;
    private TextView tv_title;
    private Button bt_good;
    private TextView tv_content;
    public ElevatorInspectView(Context context) {
        super(context);
    }

    public ElevatorInspectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_elevatorinspect,this);
        tv_title=findViewById(R.id.tv_title);
        tv_content=findViewById(R.id.tv_content);
        layout=findViewById(R.id.layout_inspect);
        bt_good=findViewById(R.id.bt_good);
        tv_title.setText(title);
        tv_content.setText(content);
        tv_content.setVisibility(VISIBLE);
        bt_good.setOnClickListener(this);
        layout.setOnClickListener(this);
    }

    public ElevatorInspectView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_good:
                if (isGood){
                    isGood=false;
                    bt_good.setBackgroundResource(R.mipmap.off);
                }else {
                    isGood=true;
                    bt_good.setBackgroundResource(R.mipmap.on);
                }
                if (isVisibile){
                    isVisibile=false;
                    tv_content.setVisibility(GONE);
                }
                break;
            case R.id.layout_inspect:
                if (isVisibile){
                    isVisibile=false;
                    tv_content.setVisibility(GONE);
                }else {
                    isVisibile=true;
                    tv_content.setVisibility(VISIBLE);
                }
                break;
            default:break;
        }
    }

    public void setTitle(String content) {
        tv_title.setText(content);
    }

    public void setContent(String demand) {
        tv_content.setText(demand);
    }
}
