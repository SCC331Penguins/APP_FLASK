package app.android.scc331.rest_test.Fragements;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.android.scc331.rest_test.MainActivity;
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

        MainActivity.actuators = null;

        displayList();

        add_router.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                final View vw = layoutInflater.inflate(R.layout.add_router_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setCancelable(false);
                final EditText router_id_text = (EditText) vw.findViewById(R.id.router_id_edittext);
                final EditText router_name_text = (EditText) vw.findViewById(R.id.router_name_eddittext);
                final Button save = vw.findViewById(R.id.save_button_dialog);
                final Button cancel = vw.findViewById(R.id.cancel_button_dialog);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (router_id_text.getText().toString().equals("") || router_name_text.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Please fill out both fields", Toast.LENGTH_LONG).show();
                        }else{
                            //PERFORM REST
                            SetRouterRestOperation setRouterRestOperation = new SetRouterRestOperation(getContext());
                            String res = (String) setRouterRestOperation.Start(router_id_text.getText().toString());
                            Toast.makeText(getContext(), res,Toast.LENGTH_LONG).show();
                            if(res.equals("router registered")) {
                                MainActivity.savedState.saveRouter(router_id_text.getText().toString(), router_name_text.getText().toString());
                                MainActivity.savedState.save(getContext());
                                MainActivity.updateRouters(getContext());
                            }
                            displayList();
                            alertDialog.cancel();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                alertDialog.setView(vw);
                alertDialog.show();


            }
        });

        return v;
    }

    public void displayList(){

        GetRouterRestOperation getRouterRestOperation = new GetRouterRestOperation(getContext());
        ArrayList<Router> routerArrayList = null;
        try {
            routerArrayList = (ArrayList<Router>) getRouterRestOperation.Start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(routerArrayList != null) {
            MainActivity.routers = routerArrayList;
            Router[] routerrToArray = new Router[routerArrayList.size()];
            routerrToArray = routerArrayList.toArray(routerrToArray);
            RouterListAdapter routerListAdapter = new RouterListAdapter(getActivity(), routerrToArray);
            routerList.setAdapter(routerListAdapter);
        }
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
            String rname = MainActivity.savedState.getRouterName(router.getId());
            name.setText(rname);

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
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.fade_in_fragment, R.animator.fade_out_fragment);
                    SensorFragment nextFrag = SensorFragment.newInstance(router.getId());
                    fragmentTransaction.replace(R.id.main_content_pane, nextFrag,"sensor_fragment")
                            .addToBackStack(null)
                            .commit();
                }
            });


            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    final View vw = layoutInflater.inflate(R.layout.change_name_dialog, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setCancelable(false);
                    final EditText editname_text = (EditText) vw.findViewById(R.id.editname_edittext);
                    final Button save = vw.findViewById(R.id.save_button_dialog);
                    final Button cancel = vw.findViewById(R.id.cancel_button_dialog);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.savedState.saveRouter(router.getId(), editname_text.getText().toString());
                            MainActivity.savedState.save(getContext());
                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();
                        }
                    });


                    alertDialog.setView(vw);
                    alertDialog.show();

                    return false;
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
