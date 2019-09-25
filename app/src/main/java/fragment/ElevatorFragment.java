package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.maintenanceelevator.R;

public class ElevatorFragment extends Fragment {
    private TextView txt,content;
    private String flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment, container,false);
        txt=(TextView) v.findViewById(R.id.txt);
        content=(TextView) v.findViewById(R.id.content);
        flag=getArguments().getString("str");
        txt.setText(flag);
        return v;
    }
}
