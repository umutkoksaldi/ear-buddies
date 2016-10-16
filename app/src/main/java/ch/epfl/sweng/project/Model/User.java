package ch.epfl.sweng.project.Model;

import java.util.Date;

@SuppressWarnings("unused")
public class User {


    private long idApiConnection;
    private String backgroundPicture;
    private String descrition;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String profilePicture;
    private boolean seeBirth;
    private Location location;

    public User(){

    }

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
        return descrition;
    }

    public void setDescrition (String descrition) {
        this.descrition = descrition;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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


    @Override
    public String toString() {
        return "{" +
                " \"idApiConnection\" : " + idApiConnection +
                ", \"backgroundPicture\": \" " + backgroundPicture + "\"" +
                ", \"descrition\": \"" + descrition + "\"" +
                ", \"firstname\": \"" + firstname + "\"" +
                ", \"lastname\" : \"" + lastname + "\"" +
                ", \"birthdate\" : " + birthdate +
                ", \"profilePicture\" : \" " + profilePicture + "\"" +
                ", \"seeBirth\" : " + seeBirth +
                ", \"location\" : " + location.toString() +
                '}';
    }

}