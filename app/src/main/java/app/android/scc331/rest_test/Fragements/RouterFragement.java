package app.android.scc331.rest_test.Fragements;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.android.scc331.rest_test.Objects.Router;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetRouterRestOperation;
import app.android.scc331.rest_test.Services.SetRouterRestOperation;

public class RouterFragement extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button add_router;
    private ListView routerList;

    public RouterFragement() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_router_fragement, container, false);
        add_router = v.findViewById(R.id.add_router_button);
        routerList = v.findViewById(R.id.router_list_view);

        GetRouterRestOperation getRouterRestOperation = new GetRouterRestOperation(getContext());
        ArrayList<Router> routerArrayList = null;
        try {
            routerArrayList = (ArrayList<Router>) getRouterRestOperation.Start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Router[] routerrToArray = new Router[routerArrayList.size()];
        routerrToArray = routerArrayList.toArray(routerrToArray);
        RouterListAdapter routerListAdapter = new RouterListAdapter(getActivity(),routerrToArray);
        routerList.setAdapter(routerListAdapter);

        add_router.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetRouterRestOperation setRouterRestOperation = new SetRouterRestOperation(getContext());
                setRouterRestOperation.Start("SCC33102_R03");
            }
        });

        return v;
    }

    class RouterListAdapter extends ArrayAdapter<Router>{

        Router[] routers;
        Context context;

        public RouterListAdapter(@NonNull Context context, Router[] routers) {
            super(context, R.layout.router_list_item, routers);
            this.routers = routers;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.router_list_item, parent, false);

            TextView id = row.findViewById(R.id.text_router_id);
            TextView name = row.findViewById(R.id.text_router_name);
            ImageView status = row.findViewById(R.id.router_status_image);

            final Router router = routers[position];

            id.setText(router.getId());
            name.setText(router.getId());

            if(router.isOnline()){
                Drawable d = getResources().getDrawable(R.drawable.on_green);
                status.setImageDrawable(d);
            }else{
                Drawable d = getResources().getDrawable(R.drawable.off_red);
                status.setImageDrawable(d);
            }

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("NEW FRAG", "HI PRESSED");
                    SensorFragment nextFrag = SensorFragment.newInstance(router.getId());
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.main_content_pane, nextFrag,"sensor_fragment")
                            .addToBackStack(null)
                            .commit();
                }
            });
            return row;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
