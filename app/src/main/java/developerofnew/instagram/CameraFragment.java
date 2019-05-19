package developerofnew.instagram;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import developerofnew.SharedPreferenceManager;
import developerofnew.instagram.model.User;

import static android.app.Activity.RESULT_OK;


public class CameraFragment extends Fragment {

    String currentImagePath;
    Button upload_btn, capture_btn;
    ImageView capture_iv;
    Uri mImageUri;
    final int CAPTURE_IMAGE = 1,GALLERY_PICK = 2;
    Bitmap bitmap;
    String mStoryTitle,imageToString, mProfileImage;
    String dateOfImage,currentTime;

    ProgressDialog mProgressDialog;
    boolean okToUpload;

    public CameraFragment() {
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
        View view = inflater.inflate(R.layout.fragment_camera,container,false);


        upload_btn = view.findViewById(R.id.upload_btn);
        capture_btn = view.findViewById(R.id.capture_btn);
        capture_iv = view.findViewById(R.id.capture_iv);

        okToUpload = false;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  getProfileImage();


        capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String [] options = {"Choose From Gallery","Take Photo"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Change Image");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i){

                            case 0:
                                Intent galleryIntent = new Intent();
                                galleryIntent.setType("image/*");
                                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(galleryIntent,"Choose Picture"), GALLERY_PICK);

                                break;

                            case 1:

                                capturePhoto();
                                break;
                        }
                    }
                });

              builder.show();


            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                storyAndImageTitle();
            }
        });

    }

    private void capturePhoto() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      //  String imageName = "image.jpg";

        if(cameraIntent.resolveActivity(getActivity().getPackageManager())!= null){

            File imageFile = null;

            try {
                imageFile = getImageFile();


            } catch (IOException e) {
                e.printStackTrace();
            }

            if(imageFile != null){

                Uri imageUri = FileProvider.getUriForFile(getContext(),"com.example.android.fileprovider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            }

        }

        startActivityForResult(cameraIntent,CAPTURE_IMAGE);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAPTURE_IMAGE){

            if(resultCode == RESULT_OK){

              //  if(mImageUri != null){

                    //convert Uri to bitmap
//                    try {
                       // bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),mImageUri);

//                okToUpload = true;
              //  bitmap = (Bitmap)data.getExtras().get("data");
                bitmap = BitmapFactory.decodeFile(currentImagePath);


                      if(bitmap != null){

                          okToUpload = true;
                        capture_iv.setImageBitmap(bitmap);
                          Log.i("bitmap",bitmap.toString());
                      }

//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
             //   }

            }
        }


        if(requestCode == GALLERY_PICK){

            if(resultCode == RESULT_OK){

                Uri uri = data.getData();

                if(uri != null){

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);

                        if(bitmap != null){

                            okToUpload = true;
                            capture_iv.setImageBitmap(bitmap);
                            Log.i("bitmap",bitmap.toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    }

    private void storyAndImageTitle() {

        final EditText editText = new EditText(getContext());
        editText.setTextColor(Color.BLACK);
        editText.setHint("Set Title/Tag for story");
        editText.setHintTextColor(Color.GRAY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Story Title");
        builder.setCancelable(false);
        builder.setView(editText);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(okToUpload){

                    mStoryTitle = editText.getText().toString();
                    imageToString = convertImageToString();
                    uploadStory();


                }else{
                    Toast.makeText(getContext(),"Please take a photo first!",Toast.LENGTH_LONG).show();

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();

    }


    private File getImageFile() throws IOException
    {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_"+timeStamp+"_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;

    }


    private String convertImageToString() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageByteArray = baos.toByteArray();
        // String result =

        return Base64.encodeToString(imageByteArray,Base64.DEFAULT);
    }



    private void uploadStory(){

       // mProgressDialog.show();

        if(!okToUpload){

            return;
        }

         dateOfImage = dateOfImage();
         currentTime = currentReadableTime();
        User user = SharedPreferenceManager.getInstance(getContext()).getUserData();
        final String username = user.getUsername();
        final int user_id = user.getId();
        final String profile_image = mProfileImage;

//        mProgressDialog.setTitle("Sign up");
//        mProgressDialog.setMessage("Please wait");
//        mProgressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLS.upload_story_image, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(),"CHECK  sucess volley response",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getContext(),"CHECK  sucess volley response"+response,Toast.LENGTH_LONG).show();


                  //  if(!jsonObject.getBoolean("error")){
                    if(response.trim().length() != 0){
//                        mProgressDialog.dismiss();
                     //   JSONObject jsonObjectUser = jsonObject.getJSONObject("image");

                        Toast.makeText(getContext(), "story was uploaded", Toast.LENGTH_SHORT).show();

                     FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                         ft.replace(R.id.main_fragment_content,new HomeFragment());
                         ft.commit();
                    }
                    

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

              //  Toast.makeText(getContext(),"CHECK fial"+error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

             Map<String,String> imageMap = new HashMap<>();
              imageMap.put("image_name",dateOfImage);
                imageMap.put("image_encoded",imageToString);
                imageMap.put("title",mStoryTitle);
                imageMap.put("time",currentTime);
                imageMap.put("username","ben");
                imageMap.put("user_id", "4");
                imageMap.put("profile_image","image profile");


                return imageMap;
            }
        };

       VolleyHandler.getInstance(getContext().getApplicationContext()).addRequestToQueue(stringRequest);
       okToUpload = false;
    }




    private String dateOfImage(){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    private String currentReadableTime() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());
        return date.toString();

    }


    private void getProfileImage() {

        User user = SharedPreferenceManager.getInstance(getContext()).getUserData();
        int user_id = 1;//user.getId();

        StringRequest stringrequest = new StringRequest(Request.Method.GET, URLS.get_user_data+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Toast.makeText(getContext(),"getprofile"+mProfileImage,Toast.LENGTH_LONG).show();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){



                                JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                                mProfileImage = jsonObjectUser.getString("image");



                            }else {

                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),"error what"+error.getMessage(),Toast.LENGTH_LONG).show();



            }
        });


         VolleyHandler.getInstance(getContext().getApplicationContext()).addRequestToQueue(stringrequest);
}


}
