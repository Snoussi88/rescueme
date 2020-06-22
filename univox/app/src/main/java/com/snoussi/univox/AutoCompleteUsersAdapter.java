package com.snoussi.univox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteUsersAdapter extends ArrayAdapter<User> {
    private List<User> userListFull;
    public AutoCompleteUsersAdapter(@NonNull Context context, @NonNull List<User> userList) {
        super(context, 0, userList);
        userListFull = new ArrayList<>(userList);
    }

    public Filter getFilter() {
        return userFilter;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.userautocomplete,parent,false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        User user = getItem(position);
        if (user != null){
            textViewName.setText(user.getName());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"user selected "+getItem(position).getName(),Toast.LENGTH_LONG).show();
                Intent intent  = new Intent(getContext(),OthersProfiles.class);
                intent.putExtra("user_id", getItem(position).getKey());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<User> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                suggestions.addAll(userListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User item : userListFull){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count  = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((User) resultValue).getName();
        }
    };
}
