package ui;

import game.players.PlayerID;
import ui.menus.PopUp;

import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A dialog to show when the game ends.
 */
public class GameEndDialog extends JDialog implements PopUp {
	/**
	 * Creates the game end dialog with the given owner.
	 * @param owner The frame which owns this dialog.
	 */
	public GameEndDialog(Frame owner) {
		super(owner, "Game Over");
		Rectangle ownerBounds = owner.getBounds();

		int newWidth = 300;
		int newHeight = 200;
		this.setBounds(
				ownerBounds.x + (ownerBounds.width - newWidth) / 2,
				ownerBounds.y + (ownerBounds.height - newHeight) / 2,
				newWidth,
				newHeight
		);

		JButton button = new JButton("Okay");

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				System.exit(0);
			}
		});

		this.add(button);
	}

	/**
	 * Sets the winner of the game.
	 * @param winner The ID of the winning player.
	 */
	public void setWinner(PlayerID winner) {
		this.setTitle("Game Over: " + winner + " won");
	}

	@Override
	public void showCenteredToParent(Component parent) {
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
