import java.awt.*;  //for Graphics
import java.awt.Color;
import javax.swing.*;  //for JComponent, JFrame


public class Draw extends JComponent {

    private Image mercury;
    private Image venus;
    private Image earth;
    private Image mars;
    private Image jupiter;
    private Image saturn;
    private Image uranus;
    private Image neptune;


    public Draw() {
        JFrame frame = new JFrame();
        setPreferredSize(new Dimension(400, 400));
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Draw d = new Draw();
    }

    public void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(255, 0, 0));
        g.drawOval(0, 0, 400, 400);
    }
}