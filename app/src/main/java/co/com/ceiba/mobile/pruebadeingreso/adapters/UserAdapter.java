package co.com.ceiba.mobile.pruebadeingreso.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.entities.Post;
import co.com.ceiba.mobile.pruebadeingreso.entities.User;
import co.com.ceiba.mobile.pruebadeingreso.helper.Util;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;
import co.com.ceiba.mobile.pruebadeingreso.view.MainActivity;
import co.com.ceiba.mobile.pruebadeingreso.view.PostActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolderUser> implements Filterable {

    ArrayList<User> users;
    ArrayList<User> usersFiltered;

    private Context mcontext;
    private Activity mActivity;
    private  ArrayList<Post> post;
    private Gson gson;

    public UserAdapter(ArrayList<User> users, Context context, Activity activity) {
        this.users = users;
        this.mActivity = activity;
        this.mcontext = context;
        this.usersFiltered = users;
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(usersFiltered.size()>0) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
        }
        return new ViewHolderUser(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser holder, final int position) {
        holder.name.setText(usersFiltered.get(position).getName());
        holder.phone.setText(usersFiltered.get(position).getPhone());
        holder.email.setText(usersFiltered.get(position).getEmail());

        gson = new Gson();
        final String user;
        User userSelected = (User) usersFiltered.get(position);
        user = gson.toJson(userSelected);
        System.out.println("USER " + userSelected.getId());

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.openProggressDialog(MainActivity.progressDialog,mcontext.getResources().getString(R.string.generic_message_progress));
                OkHttpHandler handler = new OkHttpHandler();
                try {
                    handler.execute(Endpoints.URL_BASE + Endpoints.GET_POST_USER + "userId=" + usersFiltered.get(position).getId(), user).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
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
        }
    }

    private class OkHttpHandler extends AsyncTask<String, Void, byte[]> {

        OkHttpClient client = new OkHttpClient();
        String responseService;
        ArrayList<Util.Extra> extras = new ArrayList<>();
        String user;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected byte[] doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                responseService = responseBody.string();
                user = params[1];
                /*Type postListType = new TypeToken<ArrayList<Post>>(){}.getType();
                post = gson.fromJson(responseService,postListType);*/
                return response.body().bytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            try {
                    extras.add(new Util.Extra("post",responseService));
                    extras.add(new Util.Extra("user",user));
                    Util.closeProggressDialog(MainActivity.progressDialog);
                    Util.GoToNextActivityCleanStack(mActivity, PostActivity.class,false,extras);
            } catch (Exception e) {

            }
        }
    }
}
