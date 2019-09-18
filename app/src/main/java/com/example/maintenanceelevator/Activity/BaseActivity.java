package com.example.maintenanceelevator.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.example.maintenanceelevator.R;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
    }
}
