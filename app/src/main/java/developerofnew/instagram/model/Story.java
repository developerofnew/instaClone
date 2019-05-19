package developerofnew.instagram.model;

public class Story {

       private int id,publisher_id,likes;
       private String story_image,profile_image,username,title,time;

    public Story(int id, int publisher_id, int likes, String story_image, String title,String time, String profile_image, String username) {
        this.id = id;
        this.publisher_id = publisher_id;
        this.likes = likes;
        this.story_image = story_image;
        this.profile_image = profile_image;
        this.username = username;
        this.title = title;
        this.time = time;
    }

    public String getStory_image() {
        return story_image;
    }

    public void setStory_image(String story_image) {
        this.story_image = story_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
