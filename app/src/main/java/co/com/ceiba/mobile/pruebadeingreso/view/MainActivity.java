package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.adapters.UserAdapter;
import co.com.ceiba.mobile.pruebadeingreso.entities.User;
import co.com.ceiba.mobile.pruebadeingreso.helper.ConectionSQLiteHelper;
import co.com.ceiba.mobile.pruebadeingreso.helper.Util;
import co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends Activity {

    private OkHttpClient httpClient;
    private Gson gson;
    private ConectionSQLiteHelper conection;
    private RecyclerView recyclerUsers;
    private EditText search;
    private  ArrayList<User> users;
    private UserAdapter adapter;
    static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        users = new ArrayList<>();
        recyclerUsers = (RecyclerView) findViewById(R.id.recyclerViewSearchResults);
        conection = new ConectionSQLiteHelper(this,"bd_user",null,2);

        if(Util.veirifyConnection(this)){
            //if(!verifyDB(Util.TABLE_USER)){
                getUsers();
            //}
        }


    }

    public void filter (){
        search = (EditText) findViewById(R.id.editTextSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("SIIIIIIIIIIIIII");
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable);
            }
        });
    }

    private void getUsers (){
        this.httpClient =  new OkHttpClient();
        this.gson = new Gson();

        progressDialog = new ProgressDialog(this);
        Util.openProggressDialog(progressDialog,getResources().getString(R.string.generic_message_progress));


        OkHttpHandler handler = new OkHttpHandler();
        try {
            handler.execute(Endpoints.URL_BASE+Endpoints.GET_USERS).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private boolean saveUsers (ArrayList <User> users){
        boolean response = false;
        SQLiteDatabase db = conection.getWritableDatabase();
        try{
            for (User user: users) {
                ContentValues valuesUser = new ContentValues();
                valuesUser.put(Util.ATRIBUTE_USER_ID,user.getId());
                valuesUser.put(Util.ATRIBUTE_USER_NAME,user.getName());
                valuesUser.put(Util.ATRIBUTE_USER_USERNAME,user.getUsername());
                valuesUser.put(Util.ATRIBUTE_USER_EMAIL,user.getEmail());
                valuesUser.put(Util.ATRIBUTE_USER_PHONE,user.getPhone());
                valuesUser.put(Util.ATRIBUTE_USER_WEBSITE,user.getWebsite());
                db.insert(Util.TABLE_USER,Util.ATRIBUTE_USER_ID,valuesUser);

                ContentValues valuesAdress = new ContentValues();
                valuesAdress.put(Util.ATRIBUTE_ADDRESS_USERID,user.getId());
                valuesAdress.put(Util.ATRIBUTE_ADDRESS_STREET,user.getAddress().getStreet());
                valuesAdress.put(Util.ATRIBUTE_ADDRESS_SUITE,user.getAddress().getSuite());
                valuesAdress.put(Util.ATRIBUTE_ADDRESS_CITY,user.getAddress().getCity());
                valuesAdress.put(Util.ATRIBUTE_ADDRESS_ZIPCODE,user.getAddress().getZipcode());
                db.insert(Util.TABLE_ADDRESS,Util.ATRIBUTE_ADDRESS_USERID,valuesAdress);

                ContentValues valuesGeo = new ContentValues();
                valuesGeo.put(Util.ATRIBUTE_ADDRESS_USERID,user.getId());
                valuesGeo.put(Util.ATRIBUTE_GEO_LAT,user.getAddress().getGeo().getLat());
                valuesGeo.put(Util.ATRIBUTE_GEO_LGN,user.getAddress().getGeo().getLng());
                db.insert(Util.TABLE_GEO,Util.ATRIBUTE_ADDRESS_USERID,valuesGeo);

                ContentValues valuesCompany = new ContentValues();
                valuesCompany.put(Util.ATRIBUTE_COMPANY_USERID,user.getId());
                valuesCompany.put(Util.ATRIBUTE_COMPANY_NAME,user.getCompany().getName());
                valuesCompany.put(Util.ATRIBUTE_COMPANY_CATCHPHRASE,user.getCompany().getCatchPhrase());
                valuesCompany.put(Util.ATRIBUTE_COMPANY_BS,user.getCompany().getBs());
                db.insert(Util.TABLE_COMPANY,Util.ATRIBUTE_COMPANY_USERID,valuesCompany);
            }
            response = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return response;
    }

    private void showUsers (ArrayList<User> users){
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(users, getApplicationContext(), this);
        recyclerUsers.setItemAnimator(new DefaultItemAnimator());
        filter();
        recyclerUsers.setAdapter(adapter);

    }

    private boolean verifyDB (String name){
        boolean isExist= false;
        SQLiteDatabase db = conection.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from bd_user where tbl_name = '" + name + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private class OkHttpHandler extends AsyncTask<String, Void, byte[]> {

        OkHttpClient client = new OkHttpClient();

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
                String responseService = responseBody.string();
                Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
                users = gson.fromJson(responseService,userListType);
                if (saveUsers(users)) System.out.println("INSERTO BIEN");

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
                showUsers(users);
                Util.closeProggressDialog(progressDialog);
            } catch (Exception e) {

            }
        }
    }

}