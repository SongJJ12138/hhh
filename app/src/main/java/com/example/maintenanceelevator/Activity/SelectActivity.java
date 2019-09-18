package com.example.maintenanceelevator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.maintenanceelevator.R;

public class SelectActivity extends BaseActivity implements View.OnClickListener {
private int state=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();
    }

    private void initView() {
        TextView tv_back = findViewById(R.id.title_back);
        Button bt_next = findViewById(R.id.btn_next);
        tv_back.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        RadioGroup rvGroup = findViewById(R.id.rv_group);
        rvGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_banyue) {
                    state=1;
                } else if (checkedId == R.id.rb_yue) {
                    state=2;
                } else if (checkedId == R.id.rb_bannian) {
                    state=3;
                } else if (checkedId == R.id.rb_jidu) {
                    state=4;
                } else if (checkedId == R.id.rb_nian) {
                    state=5;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                this.finish();
                break;
            case R.id.btn_next:
                if (state==0){
                    Toast.makeText(getApplicationContext(),"您还未选择需要维保项！",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(SelectActivity.this,InspectActivity.class);
                    intent.putExtra("type",state);
                    startActivity(intent);
                }
                break;
                default:break;
        }
    }
}
