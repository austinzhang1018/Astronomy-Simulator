import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Simulator designed to show how the planets are aligned at any given time.
 * Note: The simulator won't be exact, since it assumes all the planets align
 * in the year 2854. In reality, the planets won't be exactly aligned,
 * but will be grouped within 30 degrees of each other.
 */
public class SolarSystem extends JComponent{

    Planet[] planets;
    private Image[] planetImages;
    GregorianCalendar day;
    JFrame frame;
    boolean animated;
    int updateSpeed;

    public static void main(String[] args) {
        SolarSystem s = new SolarSystem(true, new GregorianCalendar(2016, 5, 25), 12);
    }

    //Update alters how many days pass per frame.
    public SolarSystem(boolean isAnimated, GregorianCalendar date, int update) {

        day = date;

        planets = new Planet[8];
        planetImages = new Image[planets.length];

        // Name, Orbit Radius in Km, Year Length in Days, Velocity in km/s
        planets[0] = new Planet("Mercury", (long) (57.9 * Math.pow(10, 6)), 88, 47.4);
        planets[1] = new Planet("Venus", (long) (108.2 * Math.pow(10, 6)), 224.7, 35.0);
        planets[2] = new Planet("Earth", (long) (149.6 * Math.pow(10, 6)), 365.2, 39.8);
        planets[3] = new Planet("Mars", (long) (227.9 * Math.pow(10, 6)), 687, 24.1);
        planets[4] = new Planet("Jupiter", (long) (778.6 * Math.pow(10, 6)), 4331, 13.1);
        planets[5] = new Planet("Saturn", (long) (1433.5 * Math.pow(10, 6)), 10747, 9.7);
        planets[6] = new Planet("Uranus", (long) (2872.5 * Math.pow(10, 6)), 30589, 6.8);
        planets[7] = new Planet("Neptune", (long) (4495.1 * Math.pow(10, 6)), 59800, 5.4);


        frame = new JFrame();
        setPreferredSize(new Dimension(600, 600));
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Planets!");


        for (int i = 0; i < planets.length; i++) {
            String fileName = planets[i].getName() + ".png";
            URL url = getClass().getResource(fileName);
            if (url == null)
                throw new RuntimeException("file not found:  " + fileName);
            planetImages[i] = new ImageIcon(url).getImage();
        }
        animated = isAnimated;
        updateSpeed = update;
    }

    public void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(255, 0, 0));

        //Orbits
        for (int i = 0; i < planets.length; i++) {
            int radius = (int) (planets[i].getRadius() / (5500000000L / 600));
            g.drawOval(600 / 2 - radius / 2 + 5, 600 / 2 - radius / 2 + 5, radius - 5, radius - 5);
        }

        Location[] loc = new Location[8];

        for (int i = 0; i < loc.length; i++) {
            loc[i] = planets[i].getLocation(day);
            g.drawImage(planetImages[i], 600 / 2 - ((int) (loc[i].getX() / (5500000000L / 600))) / 2, 600 / 2 - ((int) (loc[i].getY() / (5500000000L / 600))) / 2, null);
          //  System.out.println(planets[i].getName() + "X " + (getWidth() / 2 - ((int) (loc[i].getX() / (5500000000L / getWidth())))) + "Y " + (getHeight() / 2 - ((int) (loc[i].getY() / (5500000000L / getHeight())))));
        }
        if (animated) {
            day.add(GregorianCalendar.HOUR_OF_DAY, updateSpeed);
        }
        frame.setTitle("Date: " + day.get(GregorianCalendar.MONTH) + "/" + day.get(GregorianCalendar.DAY_OF_MONTH) + "/" + day.get(GregorianCalendar.YEAR));
        if (!(day.get(GregorianCalendar.YEAR) < 2492)) {
            if (day.get(GregorianCalendar.MONTH) > 4) {
                if (day.get(GregorianCalendar.DAY_OF_MONTH) < 6) {
                    repaint();
                }
            }
            else {
                repaint();
            }
        }
        else {
            repaint();
        }

    }

    //In km
    public int getDistance(String name1, String name2, GregorianCalendar date) {

        int days = (int) (((date.getTimeInMillis() / 1000) - 16483651200l) / 86400);
        //int days = (int)(365.24 * (date.getTimeInMillis() - 2492)) + date.getDayOfYear() - 126;

        Planet one = null;
        Planet two = null;
        for (int i = 0; i < planets.length; i++) {
            if (planets[i].getName().equals(name1)) {
                one = planets[i];
            }
            if (planets[i].getName().equals(name2)) {
                two = planets[i];
            }
        }
        Location oneLoc = one.getLocation(days);
        Location twoLoc = two.getLocation(days);

        return (int) (Math.sqrt(Math.pow(oneLoc.getX() - twoLoc.getX(), 2) + Math.pow(oneLoc.getY() - twoLoc.getY(), 2)));
    }

    public double relativeSpeed(String name1, String name2, GregorianCalendar date) {
        int days = (int) (((date.getTimeInMillis() / 1000) - 16483651200l) / 86400);
        Planet one = null;
        Planet two = null;
        for (int i = 0; i < planets.length; i++) {
            if (planets[i].getName().equals(name1)) {
                one = planets[i];
            }
            if (planets[i].getName().equals(name2)) {
                two = planets[i];
            }
        }
        Location oneLoc = one.getLocation(days);
        Location twoLoc = two.getLocation(days);
        double xLoc1 = oneLoc.getX();
        double yLoc1 = oneLoc.getY();
        double xLoc2 = twoLoc.getX();
        double yLoc2 = twoLoc.getY();

        double xPrime1 = one.radius * 2 * Math.PI / one.year * -Math.sin(days * 2 * Math.PI / one.year);
        double yPrime1 = one.radius * 2 * Math.PI / one.year * Math.cos(days * 2 * Math.PI / one.year);
        double xPrime2 = two.radius * 2 * Math.PI / one.year * -Math.sin(days * 2 * Math.PI / one.year);
        double yPrime2 = two.radius * 2 * Math.PI / one.year * Math.cos(days * 2 * Math.PI / one.year);

        double top = (xLoc1 - xLoc2) * (xPrime1 - xPrime2) + (yLoc1 - yLoc2) * (yPrime1 - yPrime2);
        double bot = Math.sqrt(Math.pow(xLoc1 - xLoc2, 2) + Math.pow(yLoc1 - yLoc2, 2));
        return ((double) ((int) ((top/bot) /86400 * 1000)))/1000;
    }

    public void draw() {
        GridDisplay display = new GridDisplay(800, 800);

            //draw sun
            Color sunColor = new Color(255,255,255);


            for (int row = 0; row < display.getNumRows(); row++) {
                for (int col = 0; col < display.getNumCols(); col++) {
                    //Set color to black
                    //display.setColor(new Location(row, col), new Color(0, 0, 0));
                }
        }


        for (int i = -1; i < 2; i ++) {
            for (int j = -1; j < 2; j++) {
                //display.setColor(new Location(display.getNumRows()/2 + i, display.getNumCols()/2 + j), sunColor);
            }
        }

        GregorianCalendar day = new GregorianCalendar(2016, 5, 21);
        display.setTitle("Date: " + day.get(GregorianCalendar.MONTH) + "/" + day.get(GregorianCalendar.DAY_OF_MONTH) + "/" + day.get(GregorianCalendar.YEAR));
        Location[] loc = new Location[8];

        for (int i = 0; i < loc.length; i++) {
            loc[i] = planets[i].getLocation(day);
            display.setImage(new Location((int) (loc[i].getX() / (10000000000L/display.getNumRows()) + display.getNumRows() / 2), (int) (loc[i].getY() / (10000000000L/display.getNumRows()) + display.getNumCols() / 2)), planets[i].getName() + ".png");
            System.out.println(planets[i].getName() + " " + new Location((int) (loc[i].getX() / (10000000000L/display.getNumRows()) + display.getNumRows() / 2), (int) (loc[i].getY() / (10000000000L/display.getNumRows()) + display.getNumCols() / 2)));
            System.out.println(planets[i].getName() + " " + loc[i]);
        }

        while (true) {
            for (int i = 0; i < loc.length; i++) {
                display.setImage(new Location((int) (loc[i].getX() / (10000000000L/display.getNumRows()) + display.getNumRows() / 2), (int) (loc[i].getY() / (10000000000L / display.getNumRows()) + display.getNumCols() / 2)), null);
                loc[i] = planets[i].getLocation(day);
                display.setImage(new Location((int) (loc[i].getX() / (10000000000L/display.getNumRows()) + display.getNumRows() / 2), (int) (loc[i].getY() / (10000000000L/display.getNumRows()) + display.getNumCols() / 2)), planets[i].getName() + ".png");
                day.add(GregorianCalendar.HOUR_OF_DAY, 3);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(2);
            } catch (InterruptedException ex) {
            }
            display.setTitle("Date: " + day.get(GregorianCalendar.MONTH) + "/" + day.get(GregorianCalendar.DAY_OF_MONTH) + "/" + day.get(GregorianCalendar.YEAR));

        }
    }

    public double relativeSpeedTest(String name1, String name2) {
        GregorianCalendar date = new GregorianCalendar(2441, 6, 24);
        double maxSpeed = 0;
        for (int i = 0; i < 10000000; i++) {
            double temp = relativeSpeed(name1, name2, date);
            if (maxSpeed < temp)
                maxSpeed = temp;
            date.add(GregorianCalendar.HOUR_OF_DAY, 24);
        }
        return maxSpeed;
    }


}