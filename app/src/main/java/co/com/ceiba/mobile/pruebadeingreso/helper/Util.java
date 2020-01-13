package co.com.ceiba.mobile.pruebadeingreso.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.R;

public class Util {

    public  static Dialog dialog;

    // For TABLE USER
    public static final String TABLE_USER="user";
    public static final String ATRIBUTE_USER_ID="id";
    public static final String ATRIBUTE_USER_NAME="name";
    public static final String ATRIBUTE_USER_USERNAME="username";
    public static final String ATRIBUTE_USER_EMAIL="email";
    public static final String ATRIBUTE_USER_PHONE="phone";
    public static final String ATRIBUTE_USER_WEBSITE="website";

    // For TABLE ADDRESS
    public static final String TABLE_ADDRESS="address";
    public static final String ATRIBUTE_ADDRESS_STREET="street";
    public static final String ATRIBUTE_ADDRESS_USERID="userid";
    public static final String ATRIBUTE_ADDRESS_SUITE="suite";
    public static final String ATRIBUTE_ADDRESS_CITY="city";
    public static final String ATRIBUTE_ADDRESS_ZIPCODE="zipcode";

    // For TABLE GEO
    public static final String TABLE_GEO="geo";
    public static final String ATRIBUTE_GEO_ADDRESSID="addressid";
    public static final String ATRIBUTE_GEO_LAT="lat";
    public static final String ATRIBUTE_GEO_LGN="lgn";

    // for TABLE COMPANY
    public static final String TABLE_COMPANY="company";
    public static final String ATRIBUTE_COMPANY_NAME="name";
    public static final String ATRIBUTE_COMPANY_USERID="userid";
    public static final String ATRIBUTE_COMPANY_CATCHPHRASE="catchPhrase";
    public static final String ATRIBUTE_COMPANY_BS="bs";

    public static final String CREATE_TABLE_USER = "CREATE TABLE "+TABLE_USER+"("+ATRIBUTE_USER_ID+" TEXT,"+ATRIBUTE_USER_NAME+" TEXT,"+ATRIBUTE_USER_USERNAME+
            " TEXT, "+ATRIBUTE_USER_EMAIL+" TEXT, "+ATRIBUTE_USER_PHONE+" TEXT, "+ATRIBUTE_USER_WEBSITE+" TEXT)";


    public static final String CREATE_TABLE_ADDRESS = "CREATE TABLE "+TABLE_ADDRESS+"("+ATRIBUTE_ADDRESS_USERID+" TEXT,"+ATRIBUTE_ADDRESS_STREET+" TEXT,"+ATRIBUTE_ADDRESS_SUITE+
            " TEXT, "+ATRIBUTE_ADDRESS_CITY+" TEXT, "+ATRIBUTE_ADDRESS_ZIPCODE+" TEXT)";

    public static final String CREATE_TABLE_GEO = "CREATE TABLE "+TABLE_GEO+"("+ATRIBUTE_ADDRESS_USERID+" TEXT,"+ATRIBUTE_GEO_LAT+" TEXT,"+ATRIBUTE_GEO_LGN+ " TEXT)";

    public static final String CREATE_TABLE_COMPANY = "CREATE TABLE "+TABLE_COMPANY+"("+ATRIBUTE_COMPANY_USERID+" TEXT,"+ATRIBUTE_COMPANY_NAME+" TEXT,"+ATRIBUTE_COMPANY_CATCHPHRASE+
            " TEXT, "+ATRIBUTE_COMPANY_BS+" TEXT)";


    public static final String QUERY_USERS = "select user.id,user.name,user.username, user.email, address.street, address.suite, address.city, address.zipcode, geo.lat,geo.lgn,user.phone,user.website,company.name,company.catchPhrase,company.bs from user,address,geo,company where user.id = address.userid and user.id = geo.userid and user.id = company.userid group by user.id";

    public Util() {
    }



    public static void snackBarAndContinue (String msg, int tiempo , final Activity actual , final Class toGo, final boolean finaliza, final ArrayList<Extra> params ){
        try{
            View contextView = actual.getCurrentFocus();
            if(contextView != null){
                Snackbar.make(contextView, msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GoToNextActivityCleanStack(actual,toGo,finaliza,params);
            }
        }, tiempo);

    }

    public static void GoToNextActivityCleanStack(Activity activity, Class clase, boolean finaliza, ArrayList<Extra> params)
    {
        Intent intent = new Intent(activity, clase ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if(params!=null){
            for (Extra param: params) {
                intent.putExtra(param.getClave(),param.getValor());
            }
        }
        activity.startActivity(intent);

        if (finaliza){
            activity.finish();
        }
    }

    public static boolean veirifyConnection (Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            View contextView = activity.findViewById(R.id.content).getRootView();
            Snackbar.make(contextView, activity.getResources().getString(R.string.errConnect), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

    }

    public static void openProggressDialog (ProgressDialog progressDialog, String mensaje){
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage(mensaje);
        progressDialog.show();
    }

    public static void closeProggressDialog (ProgressDialog progressDialog){
        progressDialog.dismiss();
    }


    public static class Extra {
        private String clave;
        private String valor;

        public Extra(String clave, String valor) {
            this.clave = clave;
            this.valor = valor;
        }

        public String getClave() {
            return clave;
        }

        public void setClave(String clave) {
            this.clave = clave;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }
}
