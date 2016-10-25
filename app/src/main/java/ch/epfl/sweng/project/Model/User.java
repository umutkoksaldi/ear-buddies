package ch.epfl.sweng.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {


    private long idApiConnection;
    private String backgroundPicture;
    private String description;
    private String firstname;
    private String lastname;
    private int age;
    private String profilePicture;
    private boolean seeBirth;
    private String email;
    private Location location;

    public User(){}

    public long getIdApiConnection() {
        return idApiConnection;
    }

    public void setIdApiConnection(long idApiConnection) {
        this.idApiConnection = idApiConnection;
    }

    public boolean isSeeBirth() {
        return seeBirth;
    }

    public void setSeeBirth(boolean seeBirth) {
        this.seeBirth = seeBirth;
    }

    public String getBackgroundPicture() {
        return backgroundPicture;
    }

    public void setBackgroundPicture(String abackgroundPicture) {
        backgroundPicture = abackgroundPicture;
    }

    public String getDescrition() {
        return description;
    }

    public void setDescrition(String description) {
        this.description = description;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String aLastname) {
        lastname = aLastname;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String isEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
                " \"idApiConnection\" : " + idApiConnection +
                ", \"backgroundPicture\": \" " + backgroundPicture + "\"" +
                ", \"description\": \"" + description + "\"" +
                ", \"firstname\": \"" + firstname + "\"" +
                ", \"lastname\" : \"" + lastname + "\"" +
                ", \"email\" : \"" + email + "\"" +
                ", \"age\" : " + age +
                ", \"profilePicture\" : \" " + profilePicture + "\"" +
                ", \"seeBirth\" : " + seeBirth +
                ", \"location\" : " + location.toString() +
                '}';
    }

}