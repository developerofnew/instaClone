package developerofnew.instagram;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyHandler {


    private  static VolleyHandler mVolleyHandler;
    private RequestQueue requestQueue;
    private static Context mContext;


    private VolleyHandler(Context mContext) {
        this.mContext = mContext;
        this.requestQueue =getRequestQueue();
    }

    public static synchronized  VolleyHandler getInstance(Context context){

        if(mVolleyHandler == null){

            mVolleyHandler = new VolleyHandler(context);
        }

        return mVolleyHandler;
    }


    public RequestQueue getRequestQueue(){

        if(requestQueue == null){

            requestQueue =  Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addRequestToQueue(Request<T>req){


        getRequestQueue().add(req);

    }


}
