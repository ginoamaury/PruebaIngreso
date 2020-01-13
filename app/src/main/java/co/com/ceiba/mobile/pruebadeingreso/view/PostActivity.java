package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.adapters.PostAdapter;
import co.com.ceiba.mobile.pruebadeingreso.adapters.UserAdapter;
import co.com.ceiba.mobile.pruebadeingreso.entities.Post;
import co.com.ceiba.mobile.pruebadeingreso.entities.User;

public class PostActivity extends Activity {
    private Gson gson;
    private  ArrayList<Post> post;
    private Intent intent;
    private TextView name,phone,email;
    private User user;
    private RecyclerView recyclerPost;
    private PostAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initVariables();
        String postResponse = intent.getStringExtra("post");
        String userJson =  intent.getStringExtra("user");
        user = gson.fromJson(userJson,User.class);
        showDataUser();
        Type postListType = new TypeToken<ArrayList<Post>>(){}.getType();
        post = gson.fromJson(postResponse,postListType);
        showPost(post);
    }

    public void initVariables(){
        gson = new Gson();
        intent = getIntent();
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        recyclerPost = (RecyclerView) findViewById(R.id.recyclerViewPostsResults);

    }

    public void showDataUser (){
        name.setText(user.getName());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());
    }

    private void showPost (ArrayList<Post> posts){
        recyclerPost.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(posts, this, getApplicationContext());
        recyclerPost.setItemAnimator(new DefaultItemAnimator());
        recyclerPost.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }


}
