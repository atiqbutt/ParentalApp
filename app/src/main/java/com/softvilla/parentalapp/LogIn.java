package com.softvilla.parentalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    private EditText userName,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userName = (EditText) findViewById(R.id.txt_email);
        password = (EditText) findViewById(R.id.txt_password);
    }

    private void ParentLogin() {

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LogIn.this);

        if(!preferences.getString("isLogin","").equalsIgnoreCase("1")){
            Intent intent = new Intent(LogIn.this, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            LogIn.this.finish();
        }
        else {
            Intent intent = new Intent(LogIn.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            LogIn.this.finish();
        }*/



        String username = userName.getText().toString();
        String Password = password.getText().toString();



        boolean isEmptyName=false;
        if(TextUtils.isEmpty(userName.getText().toString())) {
            userName.setError("Enter Your Name");
            isEmptyName = true;
        }

        boolean isEmptyPass=false;
        if(TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Enter Password");
            isEmptyPass = true;
        }

        if(!isEmptyName && !isEmptyPass) {

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Signing in...");
            pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
            pDialog.show();

            AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/ParentSignUpChecking")
                    .addBodyParameter("name", username)
                    .addBodyParameter("password", Password)
                    //.addBodyParameter("child_id", id)

                    //.addBodyParameter("phone", Phno)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

try {
    JSONArray jsonArray = new JSONArray(response);
    JSONObject jsonObject = jsonArray.getJSONObject(0);
    String msg = jsonObject.getString("msg");
    if (msg.equalsIgnoreCase("false")) {
        Toast.makeText(LogIn.this, "Wrong Username Or Password", Toast.LENGTH_SHORT).show();
        pDialog.dismiss();
    }

    else {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("parent_id", jsonObject.getString("id"));
        editor.putString("isLogin", "1");
        editor.apply();
        startActivity(new Intent(LogIn.this, SetPin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        // do anything with response
        pDialog.dismiss();
    }



    // String response_id = response;
    //  Toast.makeText(LogIn.this, response, Toast.LENGTH_SHORT).show();

}catch (Exception e){
   // Toast.makeText(LogIn.this, "Error....", Toast.LENGTH_SHORT).show();

}
                        }


                        @Override
                        public void onError(ANError error) {
                            Toast.makeText(LogIn.this, "Server Error...."/*error.toString()+"klk"*/, Toast.LENGTH_SHORT).show();
                            // handle error
                            pDialog.dismiss();
                        }
                    });


        }
    }

    public void Login(View view) {
        ParentLogin();
    }
}
