package developerofnew.instagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import developerofnew.SharedPreferenceManager;
import developerofnew.instagram.model.Story;
import developerofnew.instagram.model.User;


public class HomeFragment extends Fragment {

    ListView feed_lv;
    ArrayList<Story> arrayListStory;
    StoryListAdapter storyListAdapter;
    JSONArray jsonArrayIds;
    String ids;
    ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        feed_lv = view.findViewById(R.id.feed_lv);
        progressBar = view.findViewById(R.id.progress_bar);



        arrayListStory = new ArrayList<Story>();
        storyListAdapter = new StoryListAdapter(getContext(),R.layout.feed_single_item,arrayListStory);
        feed_lv.setAdapter(storyListAdapter);


        progressBar.setVisibility(View.VISIBLE);

        getFollowingIds();


        
        return view;
    }

    private void getFollowingIds() {

        User user = SharedPreferenceManager.getInstance(getContext()).getUserData();
        final int user_id =user.getId();


        StringRequest stringrequest = new StringRequest(Request.Method.GET, URLS.get_following_ids+3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        if(response.trim().length() != 0){


                            Object json;
                            JSONObject jsonObject;

                            try {

                                json = (JSONObject) new JSONTokener(response).nextValue();

                                if(json != null ) {

                                    jsonObject = (JSONObject) json;



                                        JSONArray dataArray = jsonObject.getJSONArray("data");


                                        for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject jsonObjectFromData= dataArray.getJSONObject(i);
                                       // JSONObject   jsonObjectOtherUser = jsonObjectFromData.getJSONObject("other_user_id");
                                            ids = jsonObjectFromData.getString("other_user_id");

//                                      ids = ids.replace("[","");
//                                      ids = ids.replace("]","");




                                      }
                                        //




                                }
                                getLatestNewsFeed(ids);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Toast.makeText(LoginActivity.this,"Json"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            // YOUR CODE

                        }else{

                            Toast.makeText(getContext(),"Response is empty",Toast.LENGTH_LONG).show();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(LoginActivity.this,"AA"+error.getMessage(),Toast.LENGTH_LONG).show();
             //   mProgressDialog.dismiss();

            }
       });
//
//        {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map following = new HashMap<>();
//
//                following.put("user_id",user_id);
//
//                return following;
//            }
     //   }//end of string request


        VolleyHandler.getInstance(getContext().getApplicationContext()).addRequestToQueue(stringrequest);
    }


    private void getLatestNewsFeed(String ids){

        User user = SharedPreferenceManager.getInstance(getContext()).getUserData();
        final int user_id =user.getId();


        StringRequest stringrequest = new StringRequest(Request.Method.GET, URLS.latest_news_feed,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   mProgressDialog.dismiss();
                        progressBar.setVisibility(View.INVISIBLE);

                        if(response.trim().length() != 0){



                            Object json;
                            JSONObject jsonObject;

                            try {

                                json = (JSONObject) new JSONTokener(response).nextValue();

                                if(json != null ) {

                                    jsonObject = (JSONObject) json;



                                    JSONArray dataArray = jsonObject.getJSONArray("data");


                                    for (int i = 0; i < dataArray.length(); i++) {

                                        JSONObject jsonObjectSingleStory = dataArray.getJSONObject(i);



                                        int id= Integer.parseInt(jsonObjectSingleStory.getString("id"));
                                   int userId = Integer.parseInt(jsonObjectSingleStory.getString("user_id"));
                                   int likes = Integer.parseInt(jsonObjectSingleStory.getString("num_of_likes"));
                                   String storyImage = jsonObjectSingleStory.getString("image_url");

                                        String title = jsonObjectSingleStory.getString("title");
                                   String time = jsonObjectSingleStory.getString("time");
                                   String profileImage = jsonObjectSingleStory.getString("profile_image");
                                   String username = jsonObjectSingleStory.getString("username");


                                        Story story = new Story(id,userId,likes,storyImage,title,time,profileImage,username
                                   );





//                                        Story story = new Story(Integer.parseInt(jsonObjectSingleStory.getString("id")),Integer.parseInt(jsonObjectSingleStory.getString("user_id"))
//                                                ,Integer.parseInt(jsonObjectSingleStory.getString("likes")),jsonObjectSingleStory.getString("story_image")
//                                                ,jsonObjectSingleStory.getString("title"),jsonObjectSingleStory.getString("time")
//                                                ,jsonObjectSingleStory.getString("profile_image"),jsonObjectSingleStory.getString("username")
//                                        );


                                        arrayListStory.add(story);

//
                                    }



                                }

                                storyListAdapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Toast.makeText(LoginActivity.this,"Json"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            // YOUR CODE

                        }else{

                            Toast.makeText(getContext(),"Response is empty",Toast.LENGTH_LONG).show();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(LoginActivity.this,"AA"+error.getMessage(),Toast.LENGTH_LONG).show();
                //   mProgressDialog.dismiss();

            }
        });

//        {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map ids = new HashMap<>();
//
//                ids.put("following_ids",jsonArrayIds);
//
//                return ids;
//            }
//        };//end of string request
        VolleyHandler.getInstance(getContext().getApplicationContext()).addRequestToQueue(stringrequest);


    }


}
