package ph.com.masshjp.fb.Models;

public class Users {

    private String profileImage;
    private String firstname;
    private String lastname;
    private String nickname;
    private String age;
    private String order;

    // Constructor
    public Users(String profileImage, String firstname, String lastname, String nickname, String age, String order) {
        this.profileImage = profileImage;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.age = age;
        this.order = order;
    }

    // Getters
    public String getProfileImage() {
        return profileImage;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getNickname() {
        return nickname;
    }
    public String getAge() {
        return age;
    }
    public String getOrder() {
        return order;
    }
}
