package developerofnew.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import developerofnew.SharedPreferenceManager;

public class LoginActivity extends AppCompatActivity {

    LinearLayout mLoginContainer;
    AnimationDrawable mAnimationDrawable;

    private static final String KEY_SUCESS = "status";


    EditText username_edt,password_edt;
    ProgressDialog mProgressDialog;
    TextView sign_up_btn,forgot_btn;
    Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginContainer = findViewById(R.id.login_container);
        mAnimationDrawable =(AnimationDrawable) mLoginContainer.getBackground();
        mAnimationDrawable.setEnterFadeDuration(5000);
        mAnimationDrawable.setExitFadeDuration(2000);

        username_edt = findViewById(R.id.user_name);
        password_edt = findViewById(R.id.user_password);
        mProgressDialog  = new ProgressDialog(this);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        forgot_btn = findViewById(R.id.forgot_btn);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logIn();
            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent signUpIntent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(signUpIntent);

            }
        });

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    private void logIn(){



        mProgressDialog.setTitle("Log In");
        mProgressDialog.setMessage("Please wait....");
        mProgressDialog.show();

    final String username = username_edt.getText().toString();
    final String password = password_edt.getText().toString();

        if(TextUtils.isEmpty(username)){
            username_edt.setError("Please fill in this field");
            username_edt.requestFocus();
            mProgressDialog.dismiss();
            return;
        }

        if(TextUtils.isEmpty(password)){
            password_edt.setError("Please fill in this field");
            password_edt.requestFocus();
           mProgressDialog.dismiss();
            return;
        }



        StringRequest stringrequest = new StringRequest(Request.Method.POST, URLS.login_api,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    mProgressDialog.dismiss();

                    if(response.trim().length() != 0){


                     Object json;
                       JSONObject jsonObject;

                        try {

                             json = (JSONObject) new JSONTokener(response).nextValue();

                             if(json != null ) {

                                 jsonObject = (JSONObject) json;

                                 //check if log in was sucessful or not
                                 if (jsonObject.getString(KEY_SUCESS).equals("true")) {


                                     JSONArray dataArray = jsonObject.getJSONArray("data");


                                     for (int i = 0; i < dataArray.length(); i++) {





//                                         JSONObject jsonObjectUser = dataArray.getJSONObject(i);
//
//                                         User user = new User(jsonObjectUser.getInt("id"), jsonObjectUser.getString("email")
//                                                 , jsonObjectUser.getString("username"), "image");

                                         //store user data inside sharedPrefrence
                                       //  SharedPreferenceManager.getInstance(getApplicationContext()).storeUserData(user);

                                     }

                                     //let user in
                                     finish();
                                     startActivity(new Intent(LoginActivity.this, MainActivity.class));


                                 } //finish json null or not
                                 else{

                                     Toast.makeText(LoginActivity.this, "You need to sign up", Toast.LENGTH_SHORT).show();
                                 }


                                 //finish if its true logged in


                             }

                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(LoginActivity.this,"Json"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        // YOUR CODE

                    }else{

                        Toast.makeText(LoginActivity.this,"Response is empty",Toast.LENGTH_LONG).show();

                    }



                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

       // Toast.makeText(LoginActivity.this,"AA"+error.getMessage(),Toast.LENGTH_LONG).show();
         mProgressDialog.dismiss();


            if (error instanceof TimeoutError  ) {

                Toast.makeText(LoginActivity.this,"time out "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
            else if (error instanceof NoConnectionError) {
                Toast.makeText(LoginActivity.this,"No connection"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
            else if (error instanceof AuthFailureError) {
                Toast.makeText(LoginActivity.this,"authfail"+error.getMessage(),Toast.LENGTH_LONG).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(LoginActivity.this,"server error"+error.getMessage(),Toast.LENGTH_LONG).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(LoginActivity.this,"network error"+error.getMessage(),Toast.LENGTH_LONG).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(LoginActivity.this,"parce"+error.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }){

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Map <String,String>  authenticationVariables = new HashMap<>();

            authenticationVariables.put("username",username);
            authenticationVariables.put("password",password);

            return authenticationVariables;
        }
    };//end of string request

        VolleyHandler.getInstance(getApplicationContext()).addRequestToQueue(stringrequest);

//.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));




    }



    @Override
    protected void onResume() {
        super.onResume();

        if(mAnimationDrawable != null && !mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mAnimationDrawable != null && mAnimationDrawable.isRunning()){

            mAnimationDrawable.stop();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

       boolean isUserLoggedIn = SharedPreferenceManager.getInstance(getApplicationContext()).isUserLoggedIn();

       if(isUserLoggedIn){

           startActivity(new Intent(LoginActivity.this,MainActivity.class));
       } else {


       }
    }
















}
