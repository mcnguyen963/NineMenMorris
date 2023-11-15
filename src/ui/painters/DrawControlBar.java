package ui.painters;

import ui.ControlBar;

import javax.swing.*;
import java.awt.*;

public class DrawControlBar extends JComponent {
    ControlBar controlBar;
    public DrawControlBar(){
        controlBar=new ControlBar();
    }

    @Override
    public void paintComponent(Graphics g) {
        controlBar.paint(g);
    }
}
