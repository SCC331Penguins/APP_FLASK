package app.android.scc331.rest_test.Fragements;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.android.scc331.rest_test.Objects.UserData;
import app.android.scc331.rest_test.R;
import app.android.scc331.rest_test.Services.GetAuthedUserRestOp;
import app.android.scc331.rest_test.Services.GrantPermissionRestOperation;
import app.android.scc331.rest_test.Services.RestPaths;

public class RouterPermissionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String router_id;

    private Button addUser;

    private ListView userList;

    private ArrayList<UserData> userData;

    private UserAdapter adapter;

    private RouterPermissionFragment self = this;

    public RouterPermissionFragment() {}

    public static RouterPermissionFragment newInstance(String router_id) {
        RouterPermissionFragment fragment = new RouterPermissionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, router_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            router_id = getArguments().getString(ARG_PARAM1);
        }
    }

    public void onUserData(ArrayList<UserData> userData){
        this.userData = userData;
        adapter = new UserAdapter(getActivity(), userData);
        userList.setAdapter(adapter);
    }

    public void removeUserData(UserData userData){
        UserData toRemove = null;
        for(UserData u : this.userData){
            if(u.getUsername().equals(userData.getUsername()))
                toRemove = u;
        }

        if(toRemove != null){
            this.userData.remove(toRemove);
            adapter.notifyDataSetChanged();
        }
    }

    public void addUserData(UserData userData){
        this.userData.add(userData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_router_permission, container, false);

        v.findViewById(R.id.grant_permission).setOnClickListener(grantPermissionClick());

        userList = v.findViewById(R.id.auth_list);

        GetAuthedUserRestOp getAuthedUserRestOp = new GetAuthedUserRestOp(getActivity(), self);
        getAuthedUserRestOp.run(router_id);

        return v;
    }


    private View.OnClickListener grantPermissionClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                final View vw = layoutInflater.inflate(R.layout.change_name_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setCancelable(false);
                final Button save = vw.findViewById(R.id.save_button_dialog);
                final Button cancel = vw.findViewById(R.id.cancel_button_dialog);
                EditText textBox = vw.findViewById(R.id.editname_edittext);
                TextView title = vw.findViewById(R.id.title);

                title.setText("Add User");
                title.setTextColor(Color.BLACK);

                textBox.setTextColor(Color.BLACK);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO Add a new user

                        if(textBox.getText().toString().length() == 0)
                            return;

                        GrantPermissionRestOperation grantPermissionRestOperation = new GrantPermissionRestOperation(getActivity(), self);
                        grantPermissionRestOperation.run(router_id, textBox.getText().toString(), RestPaths.GRANT_PERMISSION);

                        alertDialog.cancel();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                alertDialog.setView(vw, 0, 0, 0, 0);
                alertDialog.show();
            }
        };
    }

    private View.OnClickListener removeButtonClick(UserData userData){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrantPermissionRestOperation grantPermissionRestOperation = new GrantPermissionRestOperation(getActivity(), self);
                grantPermissionRestOperation.run(router_id, userData.getUsername(), RestPaths.REVOKE_PERMISSION);

            }
        };
    }

    private class UserAdapter extends ArrayAdapter<UserData> {

        private Context context;
        private ArrayList<UserData> users;

        public UserAdapter(@NonNull Context context, ArrayList<UserData> users) {
            super(context, -1, users);
            this.context = context;
            this.users = users;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // TODO CHANGE LINECHART ELEMENT
            View rowView = inflater.inflate(R.layout.userdata_list_item, parent, false);

            Button remove = rowView.findViewById(R.id.remove_button);
            TextView username = rowView.findViewById(R.id.username);

            UserData userData = users.get(position);

            remove.setOnClickListener(removeButtonClick(userData));

            username.setText(userData.getUsername());

            return rowView;
        }
    }
}
