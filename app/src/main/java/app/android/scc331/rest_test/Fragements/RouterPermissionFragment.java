package app.android.scc331.rest_test.Fragements;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import app.android.scc331.rest_test.Objects.UserData;
import app.android.scc331.rest_test.R;

public class RouterPermissionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String router_id;

    private Button addUser;

    private ListView userList;

    private ArrayList<UserData> userData;

    private UserAdapter adapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_router_permission, container, false);
        return v;
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
            View rowView = inflater.inflate(R.layout.linechart_element, parent, false);

            return rowView;
        }
    }
}
