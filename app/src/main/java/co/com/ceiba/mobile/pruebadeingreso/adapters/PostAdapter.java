package co.com.ceiba.mobile.pruebadeingreso.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.entities.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost>  {

    private ArrayList<Post> posts;
    private Activity mActivity;
    private Context mcontext;
    private Gson gson;

    public PostAdapter(ArrayList<Post> posts, Activity mActivity, Context mcontext) {
        this.posts = posts;
        this.mActivity = mActivity;
        this.mcontext = mcontext;
        this.gson = new Gson();
    }

    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        return new ViewHolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.body.setText(posts.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolderPost extends RecyclerView.ViewHolder{

        TextView title,body;

        public ViewHolderPost(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
        }
    }
}
