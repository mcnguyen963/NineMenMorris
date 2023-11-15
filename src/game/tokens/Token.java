package game.tokens;

import game.board.Board;
import game.board.Location;
import game.players.Player;
import game.players.PlayerID;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;

/**
 * A token in the game. The token knows when it has been clicked, and can draw itself.
 */
public class Token extends JComponent {
    /**
     * The token diameter in screen pixels.
     */
    public static final int DIAMETER = 50;

    /**
     * The token's main color.
     */
    private final Color mainColor;

    /**
     * The token's outline color.
     */
    private final Color outlineColor;

    /**
     * The player who owns this token.
     */
    private final Player player;

    /**
     * The point where the token starts, and where it will be placed once dead.
     */
    private final Point defaultPoint;

    /**
     * The point where the token is in screen coordinates.
     */
    private Point point;

    /**
     * The location object on the board where the token is at. Null if the token isn't on the board.
     */
    private Location location;

    /**
     * The status of the token.
     */
    private TokenStatus status;

    /**
     * Indicates whether the token has been selected by a player.
     */
    private boolean selected;

    /**
     * Creates a token at the given point belonging to the given player.
     * It is assumed that the token isn't on a location.
     * @param player The player owning this token.
     * @param initialPoint The initial point to place this token.
     */
    public Token(Player player, Point initialPoint) {
        super();
        this.player = player;
        this.defaultPoint = initialPoint;
        this.point = initialPoint;
        this.location = null;
        this.status = TokenStatus.OFF_BOARD;
        this.selected = false;

        if (this.player.getPlayerID() == PlayerID.BLACK) {
            this.mainColor = Color.BLACK;
        } else {
            this.mainColor = Color.WHITE;
        }

        this.outlineColor = Color.MAGENTA;
    }

    /**
     * Finds which player this token belongs to.
     * @return The player who owns this token.
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * @return The point the token is located on.
     */
    public Point getPoint(){
        return this.point;
    }

    /**
     * Sets the point where the token is located on.
     * @param point The new token point.
     */
    public void setPoint(Point point){
        this.point = point;
    }

    /**
     * Gets the location on which the token is placed.
     * @return The token's current location.
     */
    public Location getBoardLocation() {
        return this.location;
    }

    /**
     * Sets the location on which the token is placed.
     * @param location The location where the token is on.
     */
    public void setBoardLocation(Location location) {
        this.location = location;
    }

    /**
     * @return The current token status.
     */
    public TokenStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the token status.
     * @param status The new token status.
     */
    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    /**
     * Updates the token selection for a move.
     * @param isSelected True to select the token, false to deselect it.
     */
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    /**
     * @return True if the token is selected, false if not.
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Removes the token from its location and out of the game.
     */
    public void killToken() {
        if (this.location != null) {
            this.getBoardLocation().removeToken();
            this.setBoardLocation(null);
        }

        this.point = this.defaultPoint;
        this.setStatus(TokenStatus.DEAD);
    }

    public String tokenInfor(){
        String stringLocation;
        if(this.getStatus()==TokenStatus.OFF_BOARD){
            stringLocation=this.point.getX()+"-"+this.point.getY();
        }else if (this.getStatus()==TokenStatus.ON_BOARD){
            stringLocation=this.getBoardLocation().locationInfo();
        }else{
            stringLocation=this.point.getX()+"-"+this.point.getY();
        }
        return this.getStatus()+"-"+stringLocation;
    }
    public void loadToken(String data, Board board){
        switch (data.split("-")[0]) {
            case "OFF_BOARD" -> this.status = TokenStatus.OFF_BOARD;
            case "ON_BOARD" -> this.status = TokenStatus.ON_BOARD;
            case "DEAD" -> this.status = TokenStatus.DEAD;
            default -> System.out.println("error when load token status");
        }

        int xtemp=(int)Double.parseDouble(data.split("-")[1].trim());
        int ytemp=(int)Double.parseDouble(data.split("-")[2].trim());
        if(this.status==TokenStatus.ON_BOARD){
            Location desti= board.getLocation(xtemp,ytemp);
            if(desti.hasToken()){
                desti.removeToken();
            }
            this.setBoardLocation(board.getLocation(xtemp,ytemp));
            this.setPoint(new Point(xtemp,ytemp));
            desti.setToken(this);
        } else  {
            if(this.getBoardLocation()!=null){
                this.getBoardLocation().removeToken();
            }
            this.setBoardLocation(null);
            this.setPoint(new Point(xtemp,ytemp));
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        // Get the coordinates of the top-left of the token
        int cornerX = (int) this.point.getX() - DIAMETER / 2 + Location.DIAMETER / 2;
        int cornerY = (int) this.point.getY() - DIAMETER / 2 + Location.DIAMETER / 2;

        // Draw the token body
        graphics2D.setColor(this.mainColor);
        graphics2D.fillOval(cornerX, cornerY, DIAMETER, DIAMETER);

        // Draw a black outline so the white tokens are more visible
        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawOval(cornerX, cornerY, DIAMETER, DIAMETER);

        if (this.status == TokenStatus.DEAD) {
            // Paint a cross above dead tokens
            graphics2D.setColor(Color.RED);
            graphics2D.drawLine(cornerX, cornerY, cornerX + DIAMETER, cornerY + DIAMETER);
            graphics2D.drawLine(cornerX, cornerY + DIAMETER, cornerX + DIAMETER, cornerY);
        } else if (this.selected) {
            // Draw an outline if the token is selected
            graphics2D.setStroke(new BasicStroke(4));
            graphics2D.setColor(this.outlineColor);
            graphics2D.drawOval(cornerX, cornerY, DIAMETER, DIAMETER);
        }
    }
}

