package Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.maintenanceelevator.R;

import java.util.List;

public class PictureSelectAdapter extends RecyclerView.Adapter<PictureSelectAdapter.VH>{
    private Context context;
    private List<Bitmap> list;
    public PictureSelectAdapter(Context context, List<Bitmap> list){
        this.context=context;
        this.list=list;
    }
    public void addPic(Bitmap pic){
        if (pic!=null){
            list.add(pic);
        }
    }
    @NonNull
    @Override
    public PictureSelectAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_picselect,viewGroup,false);
        return new PictureSelectAdapter.VH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PictureSelectAdapter.VH vh, final int i) {
        vh.im.setImageBitmap(list.get(i));
        vh.im.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (list.remove(i) != null) {
                            notifyDataSetChanged();
                            Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "删除失败请重试", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return false;
            }
        });
    }

public void clearPic(){
        list.clear();
}
    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Bitmap> getPic() {
        return list;
    }


    public class VH extends RecyclerView.ViewHolder{
        private ImageView im;
        public VH(@NonNull View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.image_item_selectPic);
        }
    }
}