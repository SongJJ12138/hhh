package com.example.maintenanceelevator.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.maintenanceelevator.R;

public class MineActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        tv_back=findViewById(R.id.title_back);
        tv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                this.finish();
                break;
        }
    }
}
