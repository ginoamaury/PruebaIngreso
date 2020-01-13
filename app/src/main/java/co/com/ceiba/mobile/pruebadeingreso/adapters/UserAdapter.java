package co.com.ceiba.mobile.pruebadeingreso.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.entities.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolderUser> implements Filterable {

    ArrayList<User> users;
    ArrayList<User> usersFiltered;

    private Context mcontext;
    private Activity mActivity;


    public UserAdapter(ArrayList<User> users, Context context, Activity activity) {
        this.users = users;
        this.mActivity = activity;
        this.mcontext = context;
        this.usersFiltered = users;
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, null, false);
        return new ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser holder, int position) {
            holder.name.setText(usersFiltered.get(position).getName());
            holder.phone.setText(usersFiltered.get(position).getPhone());
            holder.email.setText(usersFiltered.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return usersFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    usersFiltered = users;
                } else {
                    ArrayList <User> filteredList = new ArrayList<>();
                    for (User row : users) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    usersFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usersFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usersFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {

        TextView name,phone,email;
        Button post;


        public ViewHolderUser(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView)itemView.findViewById(R.id.phone);
            email =(TextView) itemView.findViewById(R.id.email);
            post =(Button) itemView.findViewById(R.id.btn_view_post);

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("SIIII");
                }
            });


        }
    }
}
