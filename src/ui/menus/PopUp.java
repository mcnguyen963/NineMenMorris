package ui.menus;

import java.awt.*;

/**
 * The PopUp interface defines a method for showing a popup window centered to its parent component.
 */
public interface PopUp {

    /**
     * Displays a popup window centered to the given parent component.
     *
     * @param parent the component to which the popup window will be centered
     */
    public void showCenteredToParent(Component parent);
}
