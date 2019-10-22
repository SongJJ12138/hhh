package fragment;

import Adapter.MtcBymCompanyAdapter;
import Bean.ElevatorMTC;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.maintenanceelevator.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElevatorFragment extends Fragment {
private List<ElevatorMTC> list=new ArrayList<>();
private OnItemclickListener onItemclickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemclickListener) {
            onItemclickListener = (OnItemclickListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        onItemclickListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        list.clear();
        super.onDestroyView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String mtc = bundle.getString("mtc");
        String type = bundle.getString("type");
        JSONArray array = JSONObject.parseArray(mtc);
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject obj = (JSONObject) iterator.next();
            ElevatorMTC elevatorMTC = new ElevatorMTC();
            elevatorMTC.setConfirmed_date((String) obj.get("confirmed_date"));
            elevatorMTC.setElevator_name((String) obj.get("elevator_name"));
            elevatorMTC.setMcompany_staff((String) obj.get("mcompany_staff"));
            elevatorMTC.setPcompany_staff((String) obj.get("pcompany_staff"));
            elevatorMTC.setPk((String) obj.get("pk"));
            elevatorMTC.setStatus((String) obj.get("status"));
            elevatorMTC.setSubmitted_date((String) obj.get("submitted_date"));
            elevatorMTC.setType((String) obj.get("type"));
            int i=Integer.parseInt(type);
            switch (i) {
                case 0:
                    if (elevatorMTC.getType().equals("half_month")) {
                        list.add(elevatorMTC);
                    }
                    break;
                case 1:
                    if (elevatorMTC.getType().equals("month")) {
                        list.add(elevatorMTC);
                    }
                    break;
                case 2:
                    if (elevatorMTC.getType().equals("quarter")) {
                        list.add(elevatorMTC);
                    }
                    break;
                case 3:
                    if (elevatorMTC.getType().equals("half_year")) {
                        list.add(elevatorMTC);
                    }
                    break;
                case 4:
                    if (elevatorMTC.getType().equals("year")) {
                        list.add(elevatorMTC);
                    }
                    break;
                default:
                    break;
            }
        }
            View v = inflater.inflate(R.layout.fragment, container, false);
            RecyclerView recyclerView = v.findViewById(R.id.rv_elevator_mtc);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            MtcBymCompanyAdapter adapter = new MtcBymCompanyAdapter(getContext(), list, new MtcBymCompanyAdapter.OnItemclickListener() {
                @Override
                public void onComitclick(String str) {
                    onItemclickListener.onComitclick(str);
                }

                @Override
                public void onConfirmclick(String str) {
                    onItemclickListener.onConfirmclick(str);
                }
            });
            recyclerView.setAdapter(adapter);
            return v;
        }
    public interface OnItemclickListener{
        void onComitclick(String str);
        void onConfirmclick(String str);
    }
}
