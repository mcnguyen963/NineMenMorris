package ui;

import javax.swing.*;
import java.awt.*;

public class ControlBar extends JPanel {
    private final int length = 1000;
    private final int deep = 80;
    public ControlBar(){
        this.setPreferredSize(new Dimension(this.length,this.deep));
    }
    public void paint(Graphics g){
        // coop to 2d graphic
        Graphics2D g2d=(Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        int starting_location = 0;
        g2d.fillRect(starting_location, starting_location,this.length,this.deep);
    }
}
