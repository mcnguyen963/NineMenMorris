package game.players;

import game.games.Game;

/**
 * Represents a human player.
 */
public class Person extends Player {

    /**
     * Creates a new person player with the specified ID.
     * @param id The ID of the person player.
     */
    public Person(PlayerID id) {
        super(id);
    }

    @Override
    public void generateAction(Game game) {
        // Empty implementation since a person player's actions are controlled by user input.
        // The generateAction method is overridden to fulfill the abstract method requirement.
    }
}
