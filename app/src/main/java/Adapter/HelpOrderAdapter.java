package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maintenanceelevator.R;

import java.util.List;

import Bean.helpOrder;

public class HelpOrderAdapter extends RecyclerView.Adapter<HelpOrderAdapter.VH>{
    private Context context;
    private List<helpOrder> list;
    private OnclickListener OnclickListener;
    public HelpOrderAdapter(Context context, List<helpOrder> list, HelpOrderAdapter.OnclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.OnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_selecthelp,viewGroup,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnclickListener.onclick(list.get(i));
            }
        });
        vh.tv_title.setText(list.get(i).getElevator_name());
        vh.tv_phone.setText(list.get(i).getReporter_phone());
        vh.tv_people.setText(list.get(i).getReporter());
        vh.tv_qingkuang.setText(list.get(i).getSituation());
        vh.tv_setPeople.setText(list.get(i).getCuser_name());
        vh.tv_setTime.setText(list.get(i).getCtime());
        switch (list.get(i).getRescue_progress()){
            case "rescue_progress_waiting":
                vh.tv_status.setText("未响应！");
                vh.tv_status.setTextColor(Color.RED);
                break;
            case "rescue_progress_responded":
                vh.tv_status.setText("已响应！");
                vh.tv_status.setTextColor(Color.RED);
                break;
            case "rescue_progress_on_the_way":
                vh.tv_status.setText("救援在途！");
                vh.tv_status.setTextColor(Color.YELLOW);
                break;
            case "rescue_progress_arriver":
                vh.tv_status.setText("到达现场！");
                vh.tv_status.setTextColor(Color.YELLOW);
                break;
            case "rescue_progress_resuing":
                vh.tv_status.setText("救援中！");
                vh.tv_status.setTextColor(Color.GREEN);
                break;
            case "rescue_progress_done":
                vh.tv_status.setText("救援完成！");
                vh.tv_status.setTextColor(Color.GREEN);
                break;
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

    public void setList(List<helpOrder> helpOrderList) {
        list.addAll(helpOrderList);
        notifyDataSetChanged();
    }

    public interface OnclickListener {
        void onclick(helpOrder order);
    }
    public class VH extends RecyclerView.ViewHolder {
        private LinearLayout layout;
        private TextView tv_title;
        private TextView tv_phone;
        private TextView tv_people;
        private TextView tv_qingkuang;
        private TextView tv_setPeople;
        private TextView tv_setTime;
        private TextView tv_status;
        public VH(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.layout_help_choose);
            tv_title=itemView.findViewById(R.id.tv_help_elevator);
            tv_phone=itemView.findViewById(R.id.tv_help_phone);
            tv_people=itemView.findViewById(R.id.tv_help_name);
            tv_qingkuang=itemView.findViewById(R.id.tv_help_now);
            tv_setPeople=itemView.findViewById(R.id.tv_help_setName);
            tv_setTime=itemView.findViewById(R.id.tv_help_setTme);
            tv_status=itemView.findViewById(R.id.tv_help_status);
        }
    }
}
