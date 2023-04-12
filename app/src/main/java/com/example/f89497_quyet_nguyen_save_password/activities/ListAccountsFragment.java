package com.example.f89497_quyet_nguyen_save_password.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.ListFragment;

import com.example.f89497_quyet_nguyen_save_password.R;
import com.example.f89497_quyet_nguyen_save_password.models.ExpandableHeightListView;
import com.example.f89497_quyet_nguyen_save_password.sqlite.DBManager;
import com.example.f89497_quyet_nguyen_save_password.sqlite.DatabaseHelper;

public class ListAccountsFragment extends ListFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    DBManager dbManager;
    SimpleCursorAdapter adapter;
    SharedPreferences sharedPreferences;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.WEBSITE, DatabaseHelper.USERNAME,DatabaseHelper.PASSWORD};

    final int[] to = new int[] { R.id.tvListViewAccountId, R.id.tvListViewAccountWebsite, R.id.tvListViewAccountUsername, R.id.tvListViewAccountPassword };


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //------------SQLite---------------
        //get data from database
        dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        //create adapter and give data of adapter to list view
        adapter = new SimpleCursorAdapter(getActivity(),R.layout.list_row,cursor,from,to,0);
        adapter.notifyDataSetChanged();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this::onItemClick);
        getListView().setOnItemLongClickListener(this::onItemLongClick);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        sharedPreferences = this.getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        Intent intent = new Intent(getActivity(), UpdateAccountActivity.class);
        startActivity(intent);
        TextView idTextView = view.findViewById(R.id.tvListViewAccountId);
        TextView websiteTextView = view.findViewById(R.id.tvListViewAccountWebsite);
        TextView usernameTextView = view.findViewById(R.id.tvListViewAccountUsername);
        TextView passwordTextView = view.findViewById(R.id.tvListViewAccountPassword);

        String _id = idTextView.getText().toString();
        String website = websiteTextView.getText().toString();
        String username= usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",_id);
        editor.putString("website",website);
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Long _id = Long.parseLong(((TextView)view.findViewById(R.id.tvListViewAccountId)).getText().toString());
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Do you want to remove account: " + ((TextView)view.findViewById(R.id.tvListViewAccountId)).getText().toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.delete(_id);
                        Cursor cursor = dbManager.fetch();
                        adapter = new SimpleCursorAdapter(getActivity(),R.layout.list_row,cursor,from,to,0);
                        setListAdapter(adapter);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
        return true;
    };

}
