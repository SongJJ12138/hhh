package Adapter;

import Bean.ElevatorType;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.maintenanceelevator.R;

import java.util.List;

public class ElevatorTypeAdapter extends RecyclerView.Adapter<ElevatorTypeAdapter.VH> {
    private Context context;
    private List<ElevatorType> list;
    private ElevatorTypeAdapter.OnclickListener tvOnclickListener;
    public ElevatorTypeAdapter(Context context, List<ElevatorType> list, OnclickListener onclickListener){
        this.context=context;
        this.list=list;
        this.tvOnclickListener=onclickListener;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_select_type,viewGroup,false);
        return new ElevatorTypeAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        int index=1;
        for (ElevatorType type:list){
            RadioButton  button=new RadioButton(context);
            setRaidBtnAttribute(button,type.getVerbose_name(),index);
            vh.rv.addView(button);
            index++;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return 1;
    }
    public interface OnclickListener {
        void onclick(int id);
    }
    public class VH extends RecyclerView.ViewHolder {
        private RadioGroup rv;
        public VH(@NonNull View itemView) {
            super(itemView);
            rv=itemView.findViewById(R.id.rv_group);
        }
    }
    private void setRaidBtnAttribute(final RadioButton codeBtn, String btnContent, final int id ){
        if( null == codeBtn ){
            return;
        }
        codeBtn.setId( id );
        codeBtn.setText( btnContent );
        codeBtn.setGravity( Gravity.LEFT );
        codeBtn.setTextSize(16);
        codeBtn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                tvOnclickListener.onclick(id);
            }
        });
    }
}
