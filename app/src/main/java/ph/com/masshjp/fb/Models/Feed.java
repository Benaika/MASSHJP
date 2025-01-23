package ph.com.masshjp.fb.Models;

import com.google.firebase.Timestamp;

public class Feed {

    public String id, profile_url, caption, first_name, last_name, mediaUrl,videoUrl, email, password, gender, birthday
            , age, address, mobile_number, role;

    public Timestamp timestamp;

    public Feed(){}

    public Feed(String id, String profile_url, Timestamp timestamp, String caption, String first_name,
                String last_name, String mediaUrl, String videoUrl, String email, String password, String gender,
                String birthday, String age, String address, String mobile_number, String role) {

        this.id = id;
        this.profile_url = profile_url;
        this.timestamp = timestamp;
        this.caption = caption;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mediaUrl = mediaUrl;
        this.videoUrl = videoUrl;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthday= birthday;
        this.age = age;
        this.address = address;
        this.mobile_number = mobile_number;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
