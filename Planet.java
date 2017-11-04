import java.text.DateFormat;
import java.util.GregorianCalendar;

/**
 * Created by austinzhang on 5/11/16.
 */
public class Planet {
    Location location;
    long radius;
    double year;
    double velocity;
    String name;

    public Planet(String n, Long r, double y, double v) {
        location = new Location(r, 0);
        radius = r;
        year = y;
        velocity = v;
        name = n;
    }

    public double getVelocity(){
        return velocity;
    }

    public Location getLocation(int day) {
        location = new Location((long) (radius * Math.cos(2 * Math.PI * day / year)), (long) (radius * Math.sin(2 * Math.PI * day / year)));
        return location;
    }

    public String getName() {
        return name;
    }

    public long getRadius() {
        return radius;
    }

    public Location getLocation (GregorianCalendar date) {
       return getLocation((int) (((date.getTimeInMillis() / 1000) - 16483651200L) / 86400));
    }
}
