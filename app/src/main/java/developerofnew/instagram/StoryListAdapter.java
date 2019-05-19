package developerofnew.instagram;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import developerofnew.instagram.model.Story;

public class StoryListAdapter extends ArrayAdapter<Story> {

    private Context mContext;
    ArrayList<Story> storyArrayList;

    public StoryListAdapter(Context context, int resource, ArrayList<Story> list ){
        super(context,resource,list);
        this.mContext = context;
        this.storyArrayList = list;
         }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Story getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Story item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


       View view = convertView;

       if(view == null){

           LayoutInflater li = LayoutInflater.from(mContext);
          view = li.inflate(R.layout.feed_single_item,null);
       }

       Story story = getItem(position);


       if(story != null){

        CircleImageView profile_photo = (CircleImageView) view.findViewById(R.id.profile_photo);
        SquareImageView story_image = view.findViewById(R.id.story_image);
        TextView username_tv =  view.findViewById(R.id.username_tv);
         TextView num_of_likes = view.findViewById(R.id.num_of_likes);
         TextView image_tags = view.findViewById(R.id.image_tags);
        // TextView image_time = view.findViewById(R.id.image_time);
         TextView date = view.findViewById(R.id.image_time);

           Picasso.get().load(story.getProfile_image()).error(R.drawable.userimage).into(profile_photo);

           Picasso.get().load(story.getStory_image()).error(R.drawable.userimage).into(story_image);


//         story_image.setImageURI(Uri.parse(story.getStory_image()));

         username_tv.setText(story.getUsername());
         num_of_likes.setText(Integer.toString(story.getLikes()));
         image_tags.setText(story.getTitle());
         date.setText(story.getTime());
       }

        return view;
    }
}
