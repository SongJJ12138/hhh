package Adapter;

import Bean.Elevator;
import Bean.ElevatorInspect;
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

public class LogSelectAdapter extends RecyclerView.Adapter<LogSelectAdapter.VH> {
    private Context context;
    private List<ElevatorInspect.MtcLogsBean> list;
    private tvOnclickListener tvOnclickListener;

    public LogSelectAdapter(Context context, List<ElevatorInspect.MtcLogsBean> list, tvOnclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.tvOnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_selectlog,viewGroup,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        vh.tv_log.setText(list.get(i).getContent());
        if (list.get(i).isChecked()){
            vh.tv_statu.setText("已完成");
            vh.tv_log.setTextColor(Color.RED);
            vh.tv_statu.setTextColor(Color.RED);
            SharedPreferences sharedPreferences=context.getSharedPreferences("config", context.MODE_PRIVATE);;
            String permission=sharedPreferences.getString("permission","");
            if (permission.equals("pcompany")){
                vh.layoutl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvOnclickListener.onclick(list.get(i).getContent());
                    }
                });
            }
        }else{
            vh.tv_statu.setText("未完成");
            vh.layoutl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvOnclickListener.onclick(list.get(i).getContent());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private TextView tv_log;
        private TextView tv_statu;
        private LinearLayout layoutl;
        public VH(@NonNull View itemView) {
            super(itemView);
            layoutl=itemView.findViewById(R.id.layout_selectlog);
            tv_log=itemView.findViewById(R.id.tv_selectlog);
            tv_statu=itemView.findViewById(R.id.tv_logStatus);
        }
    }
    public interface tvOnclickListener{
        void onclick(String str);
    }
}