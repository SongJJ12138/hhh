package Adapter;

import Bean.Elevator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.maintenanceelevator.R;
import java.util.List;

public class ElevatorSelectAdapter extends RecyclerView.Adapter<ElevatorSelectAdapter.VH> {
    private Context context;
    private List<Elevator> list;
    private tvOnclickListener tvOnclickListener;

    public ElevatorSelectAdapter(Context context, List<Elevator> list, tvOnclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.tvOnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_selectelevator,viewGroup,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        vh.tv.setText(list.get(i).getName());
        vh.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOnclickListener.onclick(list.get(i).getName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private TextView tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv_selectElevator);
        }
    }
    public interface tvOnclickListener{
       void onclick(String str);
    }
}
