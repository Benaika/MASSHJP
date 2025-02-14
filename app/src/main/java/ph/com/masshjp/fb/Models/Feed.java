package ph.com.masshjp.fb.Models;

import com.google.firebase.Timestamp;

public class Feed {

    public String userid, profileImage, caption, firstname, lastname, mediaUrl,videoUrl, email, password, gender, birthday
            , age, address, mobile_number, order;

    public Timestamp timestamp;

    public Feed(){}

    public Feed(String userid, String profileImage, Timestamp timestamp, String caption, String firstname,
                String lastname, String mediaUrl, String videoUrl, String email, String password, String gender,
                String birthday, String age, String address, String mobile_number, String order) {

        this.userid = userid;
        this.profileImage = profileImage;
        this.timestamp = timestamp;
        this.caption = caption;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mediaUrl = mediaUrl;
        this.videoUrl = videoUrl;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthday= birthday;
        this.age = age;
        this.address = address;
        this.mobile_number = mobile_number;
        this.order = order;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String id) {
        this.userid = userid;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfile_url(String profileImage) {
        this.profileImage = profileImage;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLast_name(String last_name) {
        this.lastname = last_name;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
