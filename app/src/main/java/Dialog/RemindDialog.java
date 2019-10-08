package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.TextView;
import com.example.maintenanceelevator.R;

public class RemindDialog extends Dialog {

    private Context context;
    private TextView titleTv, contentTv;

    public RemindDialog(Context context) {
        super(context);
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.remind_dialog, null);
        setContentView(view);
        initWindow();

        titleTv = (TextView) findViewById(R.id.title_name);
        contentTv = (TextView) findViewById(R.id.text_view);

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

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setContent(String content) {
        contentTv.setText(content);
    }

}
