package ch.epfl.sweng.project.Model;


@SuppressWarnings("unused")
public class Location {

    private double latitude;
    private double longitude;


    public Location(){

    }

    public double getLattitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLattitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    // The method should be overridden for jackson.
    @Override
    public String toString() {
        return "{" +
                " \"latitude\" : " + latitude +
                ", \"longitude\": \" " + longitude + "\"" +
                '}';
    }

}
