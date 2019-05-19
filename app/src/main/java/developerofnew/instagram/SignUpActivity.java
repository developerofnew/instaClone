package developerofnew.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import developerofnew.instagram.model.User;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout mLoginContainer;
    AnimationDrawable mAnimationDrawable;

    EditText email_edt,username_edt,password_edt,password_confirm_edt;
    Button sign_up_btn;
    TextView go_to_login_btn;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        mLoginContainer = findViewById(R.id.login_container);
        mAnimationDrawable =(AnimationDrawable) mLoginContainer.getBackground();
        mAnimationDrawable.setEnterFadeDuration(5000);
        mAnimationDrawable.setExitFadeDuration(2000);


        email_edt = findViewById(R.id.user_email);
        username_edt = findViewById(R.id.user_name);
        password_edt = findViewById(R.id.user_password);
        password_confirm_edt = findViewById(R.id.confirm_password);
        mProgressDialog = new ProgressDialog(this);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        go_to_login_btn = (TextView) findViewById(R.id.go_to_login_btn);




        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                register();
            }
        });

        go_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });


    }


    private void register(){



        mProgressDialog.setTitle("Sign up");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.show();
        final String email = email_edt.getText().toString();
        final String username = username_edt.getText().toString();
        final String password = password_edt.getText().toString();
        String password_confirm = password_confirm_edt.getText().toString();

        if(!email.contains("@")){

            email_edt.setError("This is not Valid email");
            email_edt.requestFocus();
            mProgressDialog.dismiss();
            return;
        }

        if(username.isEmpty() ){

            username_edt.setError("Please fill in ");
            username_edt.requestFocus();
            mProgressDialog.dismiss();
            return;
        }

        if(password.isEmpty()){

            password_edt.setError("Please fill in this field");
            password_edt.requestFocus();
            mProgressDialog.dismiss();
            return;
        }

        if(password_confirm.isEmpty()){

            password_confirm_edt.setError("Please fill in the field");
            password_confirm_edt.requestFocus();
            mProgressDialog.dismiss();
            return;

        }

        if(!password.equals(password_confirm)){

            password_confirm_edt.setError("Password dont match");
            password_confirm_edt.requestFocus();
            mProgressDialog.dismiss();
            return;
        }


        StringRequest stringrequest = new StringRequest(Request.Method.POST, URLS.sign_up_api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(response.trim().length() != 0){

                      // Object json = null;

                        try {


                          Object  json = new JSONTokener(response).nextValue();

                            JSONObject jsonObject =   (JSONObject)json;

                            JSONArray jsonArray = jsonObject.getJSONArray("data");


                            for(int i =0; i < jsonArray.length();i++){

                           JSONObject jsonObjectUserInfo = jsonArray.getJSONObject(i);


                                User user = new User(Integer.parseInt(jsonObjectUserInfo.getString("id")), jsonObjectUserInfo.getString("email")
                                        , jsonObjectUserInfo.getString("username"),jsonObjectUserInfo.getString("image"));


                                SharedPreferenceManager.getInstance(getApplicationContext()).storeUserData(user);


                            }

                                    mProgressDialog.dismiss();



                                    //store user data inside sharedPrefrence


                                    //let user in and no need sign up activity after in profile page
                                    finish();
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));

//


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUpActivity.this,"volley error"+e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.i("Volley","volley"+e.getMessage().toString());
                        }

                        }else{

                            Toast.makeText(SignUpActivity.this,"Response is empty",Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

              // Toast.makeText(SignUpActivity.this,"blank"+error.getMessage(),Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(SignUpActivity.this,"time out or no connection"+error.getMessage(),Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(SignUpActivity.this,"authfail"+error.getMessage(),Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(SignUpActivity.this,"server error"+error.getMessage(),Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(SignUpActivity.this,"network error"+error.getMessage(),Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(SignUpActivity.this,"parce"+error.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String>  authenticationVariables = new HashMap<>();


                authenticationVariables.put("username",username);
                authenticationVariables.put("email",email);
                authenticationVariables.put("password",password);


                return authenticationVariables;
            }
        };//end of string request


        VolleyHandler.getInstance(getApplicationContext()).addRequestToQueue(stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));



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

            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
        } else {


        }
    }











}
