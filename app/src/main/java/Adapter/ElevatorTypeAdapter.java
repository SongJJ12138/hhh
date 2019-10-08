package Adapter;

import Bean.ElevatorType;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.maintenanceelevator.R;

import java.util.List;

public class ElevatorTypeAdapter extends RecyclerView.Adapter<ElevatorTypeAdapter.VH> {
    private Context context;
    private List<ElevatorType> list;
    private OnclickListener OnclickListener;
    public ElevatorTypeAdapter(Context context, List<ElevatorType> list, OnclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.OnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_select_type,viewGroup,false);
        return new ElevatorTypeAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnclickListener.onclick(list.get(i).getName());
            }
        });
        vh.tv_title.setText(list.get(i).getVerbose_name());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface OnclickListener {
        void onclick(String type);
    }
    public class VH extends RecyclerView.ViewHolder {
        private RelativeLayout layout;
        private TextView tv_title;
        public VH(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.btn_inspect);
            tv_title=itemView.findViewById(R.id.tv_selectTitle);
        }
    }
}
