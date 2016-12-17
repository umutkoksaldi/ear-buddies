package ch.epfl.sweng.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    // Length of the user description on the map
    private static int snippedDescriptionLength = 17;
    private long idApiConnection;
    private String backgroundPicture;
    private String description;
    private String firstname;
    private String lastname;
    private int age;
    private String profilePicture;
    private String email;
    private Location location;
    private long currentMusicId;
    private Setting setting;

    public User() {
    }

    public long getIdApiConnection() {
        return idApiConnection;
    }

    public void setIdApiConnection(long idApiConnection) {
        this.idApiConnection = idApiConnection;
    }

    public String getBackgroundPicture() {
        return backgroundPicture;
    }

    public void setBackgroundPicture(String abackgroundPicture) {
        backgroundPicture = abackgroundPicture;
    }

    public String getDescription() {
        return description;
    }

    public String getSnippetDescription() {
        if (description != null) {
            if (description.length() > snippedDescriptionLength) {
                description = description.substring(0, snippedDescriptionLength) + "...";
            }
        }
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


    public long getCurrentMusicId() {
        return currentMusicId;
    }

    public void setCurrentMusicId(long currentMusicId) {
        this.currentMusicId = currentMusicId;
    }


}