package game.board;

import game.tokens.Token;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * A location on the board. The location can draw itself.
 */
public class Location extends JComponent {
    /**
     * The location's diameter.
     */
    public static final int DIAMETER = 10;

    /**
     * The location's normal color.
     */
    private static final Color MAIN_COLOR = Color.BLACK;

    /**
     * The location's color when highlighted.
     */
    private static final Color HIGHLIGHT_COLOR = Color.MAGENTA;

    /**
     * Indicates whether the location should be highlighted.
     */
    private boolean highlighted;

    /**
     * The token on this location (if any, null otherwise).
     */
    private Token token;

    /**
     * The neighbouring locations.
     * NOTE: May not need this later
     */
    private final List<Location> neighbours = new ArrayList<>();

    /**
     * Creates a new location without a position. The centre-point must be set later with Location::setCentre.
     */
    public Location() {
        super();
    }

    /**
     * Sets the centre-point of the location.
     * @param centrePoint The location's centre.
     */
    public void setCentre(Point centrePoint) {
        this.setBounds(
                (int) centrePoint.getX() - DIAMETER / 2, (int) centrePoint.getY() - DIAMETER / 2,
                DIAMETER, DIAMETER);
    }

    /**
     * Checks if the location is highlighted.
     * @return True if the location is highlighted, false otherwise.
     */
    public boolean isHighlighted(){
        return this.highlighted;
    }

    /**
     * Sets whether the location is highlighted.
     * @param highlighted True to highlight the location, false to turn off highlights.
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Checks if there is a token on this location.
     * @return True if there is a token on this location, false otherwise.
     */
    public boolean hasToken() {
        return this.token != null;
    }

    /**
     * Gets the token on this location.
     * @return The token on this location, or null if no token is on this locaiton.
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Places a token on the location.
     * @param token The token to be placed.
     * @throws IllegalStateException Thrown if the location already has a token on it.
     */
    public void setToken(Token token) throws IllegalStateException {
        if (this.hasToken()) {
            throw new IllegalStateException("This location already has a token on it.");
        }

        this.token = token;
    }

    /**
     * Removes the token from this location (if one exists).
     */
    public void removeToken() {
        this.token = null;
    }

    /**
     * Determines if a given location is adjacent to this location.
     * @param location The location to check for adjacency.
     * @return True if the given location is adjacent to this location, false otherwise.
     */
    public boolean hasNeighbour(Location location) {
        for (Location neighbour : this.neighbours) {
            if (neighbour == location) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the neighbours of this location.
     * @return A list of the adjacent locations.
     */
    public List<Location> getNeighbours() {
        return this.neighbours;
    }

    /**
     * Adds a neighbouring location.
     * @param location An adjacent location.
     */
    public void addNeighbour(Location location) {
        this.neighbours.add(location);
    }

    /**
     * Calculates the distance between this location and a given point.
     *
     * @param point The point to calculate the distance to.
     * @return The distance between this location and the given point.
     */
    public double distanceTo(Point point) {
        double dx = point.x - getX();
        double dy = point.y - getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Highlights all adjacent locations that don't have a token on them.
     */
    public void highlightAdjacentFreeLocations(){
        for (Location neighbour : this.getNeighbours()) {
            if (!neighbour.hasToken()) {
                neighbour.setHighlighted(true);
            }
        }
    }

    /**
     * Retrieves the information about the location.
     *
     * @return The location information in the format "x-y".
     */
    public String locationInfo(){
        return this.getX()+"-"+this.getY();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        graphics.setColor(this.highlighted ? HIGHLIGHT_COLOR : MAIN_COLOR);
        graphics.fillOval(this.getX(), this.getY(), DIAMETER, DIAMETER);
    }
}
