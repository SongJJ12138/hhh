package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.maintenanceelevator.R;

public class ChooseDialog  extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tv_content;
    private Button bt_cancel;
    private Button bt_commit;
    private PostDialog.onChooseListener onChooseListener;
    public ChooseDialog(@NonNull Context context) {
        super(context, R.style.CommontMyDialog);
        this.context = context;
        init();
    }
    public void setListener(PostDialog.onChooseListener onChooseListener){
        this.onChooseListener=onChooseListener;
    }
    public void settitle(String  title){
        tv_content.setText(title);
    }
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.choose_dialog, null);
        setContentView(view);
        initWindow();
        tv_content = (TextView) findViewById(R.id.tv_dialog_title);
        bt_cancel = (Button) findViewById(R.id.bt_dialog_cancle);
        bt_commit = (Button) findViewById(R.id.bt_dialog_commit);
        bt_cancel.setOnClickListener(this);
        bt_commit.setOnClickListener(this);


    }
    private void initWindow() {
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(0));
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_dialog_cancle:
                this.dismiss();
                break;
            case R.id.bt_dialog_commit:
                if (onChooseListener!=null){
                    onChooseListener.onChoose("");
                }
                break;
        }
    }
}
