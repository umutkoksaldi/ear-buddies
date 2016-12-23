package ch.epfl.sweng.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static ch.epfl.sweng.project.util_constant.GlobalSetting.SNIPPED_DESCRIPTION_LENGTH;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    // Length of the user description on the map
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
            if (description.length() > SNIPPED_DESCRIPTION_LENGTH) {
                description = description.substring(0, SNIPPED_DESCRIPTION_LENGTH) + "...";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public long getCurrentMusicId() {
        return currentMusicId;
    }

    public void setCurrentMusicId(long currentMusicId) {
        this.currentMusicId = currentMusicId;
    }


}