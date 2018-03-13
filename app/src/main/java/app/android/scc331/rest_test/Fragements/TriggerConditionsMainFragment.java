package app.android.scc331.rest_test.Fragements;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.*;
import java.util.ArrayList;
import app.android.scc331.rest_test.Fragements.TriggerConditionBuilderFragment.OnSubmitListener;
import app.android.scc331.rest_test.Objects.TriggerCondition;
import app.android.scc331.rest_test.R;

public class TriggerConditionsMainFragment extends Fragment implements OnSubmitListener
{
    ArrayList<TriggerCondition> conditions = new ArrayList<>();
    String router_id = "";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_trigger_conditions_main, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            router_id = bundle.getString("router_id", "o");
        }

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TriggerConditionManagerFragment fragment = new TriggerConditionManagerFragment();
        bundle = new Bundle();
        bundle.putString("router_id", router_id);
        fragment.setArguments(bundle);
        ft.replace(R.id.fragmentFrameMain, fragment);
        ft.commit();
        // Inflate the alarm_toggle_states for this fragment
        return view;
    }

    public void replaceFragment(Fragment newFragment)
    {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentFrameMain, newFragment);
        ft.commit();
    }

    @Override
    public void onSubmitClicked(TriggerCondition triggerCondition)
    {
        if(triggerCondition != null)
            conditions.add(triggerCondition);
        TriggerConditionManagerFragment triggerConditionManager = new TriggerConditionManagerFragment();
        replaceFragment(triggerConditionManager);
        triggerConditionManager.update(conditions);
    }
}
