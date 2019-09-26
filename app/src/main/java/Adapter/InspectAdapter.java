package Adapter;

import Bean.Elevator;
import Bean.Inspect;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.maintenanceelevator.R;
import view.ElevatorInspectView;

import java.util.List;

public class InspectAdapter extends RecyclerView.Adapter<InspectAdapter.VH> {
    private Context context;
    private List<Inspect> list;
    private InspectAdapter.OnclickListener tvOnclickListener;
    public InspectAdapter(Context context, List<Inspect> list, OnclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.tvOnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_inspect,viewGroup,false);
        return new InspectAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.inspectView.setTitle(list.get(i).getContent());
        vh.inspectView.setContent(list.get(i).getDemand());
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
        void onclick();
    }
    public class VH extends RecyclerView.ViewHolder{
        private ElevatorInspectView inspectView;
        public VH(@NonNull View itemView) {
            super(itemView);
            inspectView=itemView.findViewById(R.id.view_inspect);
        }
    }
}
