package game.board;

import game.tokens.Token;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;

/**
 * The game board. The board draws itself.
 */
public class Board extends JComponent {
    /**
     * The point of the top-left corner of the board.
     */
    private static final Point CORNER = new Point(160, 120);

    /**
     * The width and height of the square board.
     */
    private static final int SIZE = 680;

    /**
     * The space between the board squares.
     */
    private static final int SPACING = 100;

    /**
     * The locations on the board.
     */
    private final Location[][] locations = new Location[3][8];

    /**
     * Creates a new board with locations and no tokens.
     */
    public Board() {
        for (int square = 0; square < 3; square++) {
            for (int offset = 0; offset < 8; offset++) {
                this.locations[square][offset] = new Location();
            }
        }

        for (int square = 0; square < 3; square++) {
            for (int offset = 0; offset < 8; offset++) {
                // Set up the location's position
                this.locations[square][offset].setCentre(
                        new Point(this.calculateX(square, offset), this.calculateY(square, offset)));

                // Link the location to the neighbours in the same square
                this.locations[square][offset].addNeighbour(this.locations[square][(offset + 7) % 8]);
                this.locations[square][offset].addNeighbour(this.locations[square][(offset + 1) % 8]);

                // Link the edges between the squares
                if (offset % 2 == 1 && square == 1) {
                    this.locations[square][offset].addNeighbour(this.locations[0][offset]);
                    this.locations[square][offset].addNeighbour(this.locations[2][offset]);
                } else if (offset % 2 == 1) {
                    this.locations[square][offset].addNeighbour(this.locations[1][offset]);
                }
            }
        }
    }

    /**
     * Calculate the x-coordinate of the given location.
     * @param square The location's square.
     * @param offset The location's square offset.
     * @return The location's centre-point x-coordinate.
     */
    private int calculateX(int square, int offset) {
        int margin = this.calculateMargin(square);

        return switch (offset) {
            case 0, 6, 7 -> (int) CORNER.getX() + margin;
            case 1, 5 -> (int) CORNER.getX() + SIZE / 2;
            case 2, 3, 4 -> (int) CORNER.getX() + SIZE - margin;
            default -> 0;
        };
    }

    /**
     * Calculate the y-coordinate of the given location.
     * @param square The location's square.
     * @param offset The location's square offset.
     * @return The location's centre-point y-coordinate.
     */
    private int calculateY(int square, int offset) {
        int margin = this.calculateMargin(square);

        return switch (offset) {
            case 0, 1, 2 -> (int) CORNER.getY() + margin;
            case 3, 7 -> (int) CORNER.getY() + SIZE / 2;
            case 4, 5, 6 -> (int) CORNER.getY() + SIZE - margin;
            default -> 0;
        };
    }

    /**
     * Calculates the margin from the board edge to the given square.
     * @param square The square index.
     * @return The margin for the given square.
     */
    private int calculateMargin(int square) {
        return Token.DIAMETER + square * SPACING;
    }

    /**
     * Retrieves a list of all locations on the board.
     *
     * @return The list of locations.
     */
    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();

        for (Location[] sub : this.locations) {
            locations.addAll(Arrays.asList(sub));
        }

        return locations;
    }

    /**
     * Checks if the given token is in a mill.
     * @param token The token to check for.
     * @return A list of tokens in the mill, null if there is no mill.
     */
    public List<Token> checkForMill(Token token) {
        //
        Location location = token.getBoardLocation();
        // Checking each neighbour of the token
        for (Location neighbour1 : location.getNeighbours()) {
            Token neighbour1Token = neighbour1.getToken();
            if (neighbour1Token != null && neighbour1Token.getPlayer() == token.getPlayer()) {
                // 2 ways to form a mill - move a piece to the edge of a mill
                //  or move a piece to the middle of the mill
                // perform middle check by checking other names from original token
                for (Location neighbour2 : location.getNeighbours()) {
                    Token neighbour2Token = neighbour2.getToken();
                    if (neighbour1 != neighbour2 && neighbour2Token != null && neighbour2Token.getPlayer() == token.getPlayer()) {
                        // Checking if the locations are in a straight line.
                        int dx1 = Math.abs(neighbour1.getX() - location.getX());
                        int dy1 = Math.abs(neighbour1.getY() - location.getY());
                        int dx2 = Math.abs(neighbour2.getX() - location.getX());
                        int dy2 = Math.abs(neighbour2.getY() - location.getY());
                        if (dx1 == dx2 && dy1 == dy2) {
                            ArrayList<Token> mill = new ArrayList<>();
                            mill.add(location.getToken());
                            mill.add(neighbour1.getToken());
                            mill.add(neighbour2.getToken());
                            return mill;
                        }
                    }
                }

                // no middle mill found, so check neighbours of neighbour to check for edge mill
                for (Location neighbour2 : neighbour1.getNeighbours()) {
                    Token neighbour2Token = neighbour2.getToken();
                    if (neighbour2Token != null && neighbour2Token.getPlayer() == token.getPlayer()) {
                        // Checking if the locations are in a straight line.
                        int dx1 = neighbour1.getX() - location.getX();
                        int dy1 = neighbour1.getY() - location.getY();
                        int dx2 = neighbour2.getX() - neighbour1.getX();
                        int dy2 = neighbour2.getY() - neighbour1.getY();
                        if (dx1 == dx2 && dy1 == dy2) {
                            ArrayList<Token> mill = new ArrayList<>();
                            mill.add(location.getToken());
                            mill.add(neighbour1.getToken());
                            mill.add(neighbour2.getToken());
                            return mill;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Retrieves the closest location to the given point.
     *
     * @param point The point to compare against.
     * @return The closest location.
     */
    public Location getClosestLocation(Point point) {
        Location closestLocation = null;
        double minDistance = Double.MAX_VALUE;
        for (Location[] locations : locations) {
            for (Location location : locations) {
                double distance = location.distanceTo(point);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestLocation = location;
                }
            }
        }
        return closestLocation;
    }

    /**
     * Finds the location nearest to the release point which is empty and close enough.
     * @param releasePoint The point where the mouse was released.
     * @param precision how far from the point the mouse release so it can be accept
     * @return The nearest location, or null if there is no such valid location.
     */
    public Location findNearbyEmptyLocation(Point releasePoint, int precision) {
        Location nearestLocation = this.getClosestLocation(releasePoint);

        if (nearestLocation == null || nearestLocation.hasToken() ||
                nearestLocation.distanceTo(releasePoint) > precision) {
            return null;
        } else {
            return nearestLocation;
        }
    }
    /**
     * Highlights all locations that don't have a token on them.
     */
    public void highlightAllFreeLocations(){
        for (Location[] locations : this.locations) {
            for (Location location : locations) {
                if (!location.hasToken()) {
                    location.setHighlighted(true);
                }
            }
        }
    }
    /**
     * Clears highlights on all locations.
     */
    public void clearAllLocationHighlights(){
        for (Location[] locations : this.locations) {
            for (Location location : locations) {
                location.setHighlighted(false);
            }
        }
    }

    /**
     * Retrieves the location at the given coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The location at the specified coordinates, or null if not found.
     */
    public Location getLocation(int x, int y){
        for (Location[] locations : this.locations) {
            for (Location location : locations) {
                if (location.getX() == x && location.getY() == y) {
                    return location;
                }
            }
        }
        return null;
    }

    /**
     * Resets the state of all locations on the board by removing any tokens.
     */
    public void resetLocation(){
        for (Location[] locations : this.locations) {
            for (Location location : locations) {
                if (location.hasToken()) {
                    location.removeToken();
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        // Draw the board background
        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.fillRect((int) CORNER.getX(), (int) CORNER.getY(), SIZE, SIZE);

        // Draw the four axial lines
        graphics.setColor(Color.BLACK);
        graphics.drawLine(
                this.calculateX(0, 7), this.calculateY(0, 7),
                this.calculateX(2, 7), this.calculateY(2, 7));
        graphics.drawLine(
                this.calculateX(0, 1), this.calculateY(0, 1),
                this.calculateX(2, 1), this.calculateY(2, 1));
        graphics.drawLine(
                this.calculateX(2, 3), this.calculateY(2, 3),
                this.calculateX(0, 3), this.calculateY(0, 3));
        graphics.drawLine(
                this.calculateX(2, 5), this.calculateY(2, 5),
                this.calculateX(0, 5), this.calculateY(0, 5));

        // Draw the three squares
        graphics.drawRect(
                this.calculateX(0, 0), this.calculateY(0, 0),
                SIZE - 2 * this.calculateMargin(0), SIZE - 2 * this.calculateMargin(0));
        graphics.drawRect(
                this.calculateX(1, 0), this.calculateY(1, 0),
                SIZE - 2 * this.calculateMargin(1), SIZE - 2 * this.calculateMargin(1));
        graphics.drawRect(
                this.calculateX(2, 0), this.calculateY(2, 0),
                SIZE - 2 * this.calculateMargin(2), SIZE - 2 * this.calculateMargin(2));

        // Draw the locations
        for (int square = 0; square < 3; square++) {
            for (int offset = 0; offset < 8; offset++) {
                this.locations[square][offset].paintComponent(graphics);
            }
        }
    }
}
