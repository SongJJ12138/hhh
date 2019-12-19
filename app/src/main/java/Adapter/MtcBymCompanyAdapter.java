package Adapter;

import Bean.ElevatorMTC;
import Constans.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.maintenanceelevator.R;

import java.util.List;

public class MtcBymCompanyAdapter extends RecyclerView.Adapter<MtcBymCompanyAdapter.VH> {
    private Context context;
    private OnItemclickListener OnclickListener;
    private List<ElevatorMTC> list;
    public interface OnItemclickListener{
        void onComitclick(String str);
        void onConfirmclick(String str);
    }
    public MtcBymCompanyAdapter(Context context, List<ElevatorMTC> list, OnItemclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.OnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_selectorder,viewGroup,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        switch (list.get(i).getStatus()){
            case "unsubmitted":
                vh.tv_staute.setText("未提交");
                vh.tv_staute.setTextColor(Color.RED);
                break;
            case "submitted":
                vh.tv_staute.setText("待审核");
                vh.tv_staute.setTextColor(Color.YELLOW);
                break;
            case "confirmed":
                vh.tv_staute.setText("已验收");
                vh.tv_staute.setTextColor(Color.GREEN);
                break;
            default:break;
        }
        vh.tv_work.setText(list.get(i).getMcompany_staff());
        vh.tv_safe.setText(list.get(i).getPcompany_staff());
        vh.tv_title.setText(list.get(i).getElevator_name());
        SharedPreferences sharedPreferences=context.getSharedPreferences("config", context.MODE_PRIVATE);
        String permission=sharedPreferences.getString("permission","");
        if (!permission.equals("")){
            if (permission.equals("mcompany")){
                if (list.get(i).getStatus().equals("unsubmitted")){
                    vh.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OnclickListener.onComitclick(list.get(i).getPk());
                        }
                    });
                }
            }else{
                if (list.get(i).getStatus().equals("submitted")){
                    vh.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OnclickListener.onConfirmclick(list.get(i).getPk());
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class VH extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private TextView tv_staute;
        private LinearLayout layout;
        private TextView tv_work;
        private TextView tv_safe;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_selectlog);
            tv_staute=itemView.findViewById(R.id.tv_logStatus);
            layout=itemView.findViewById(R.id.layout_selectlog);
            tv_work=itemView.findViewById(R.id.tv_workName);
            tv_safe=itemView.findViewById(R.id.tv_safeName);
        }
    }
}
