package com.company.comanda.quagmire;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Closeables;


public class QuagmmireUIImpl implements QuagmireUI {

	private static final Logger log = LoggerFactory.getLogger(QuagmmireUIImpl.class);

	private static final String APPLICATION_ICON = "images/comanda_icon.png";
	public static final String RING_ICON = "images/ring.png";

	private boolean configured;

	@Inject
	public QuagmmireUIImpl(){

	}

	private void configure() throws IOException{
		String message = "You got a new notification message. Isn't it awesome to have such a notification message.";
		String header = "This is header of notification message";
		JFrame frame = new JFrame();
		frame.setSize(300,125);
		frame.setUndecorated(true);
		
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel headingLabel = new JLabel(header);
		//    	headingLabel.setIcon(getImage(APPLICATION_ICON)); // --- use image icon you want to be as heading image.
		headingLabel.setOpaque(false);
		frame.add(headingLabel, constraints);
		constraints.gridx++;
		constraints.weightx = 0f;
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTH;
		JButton cloesButton = new JButton("X");
		cloesButton.setMargin(new Insets(1, 4, 1, 4));
		cloesButton.setFocusable(false);
		frame.add(cloesButton, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel messageLabel = new JLabel("<HtMl>"+message);
		frame.add(messageLabel, constraints);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
		frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());

	}

	public void notifyPendingBills(int pendingBills) {
		if(!configured){
			try {
				configure();
				configured = true;
			} catch (IOException e) {
				log.error("Could not configure notifications",e);
			}
		}

	}

	protected RenderedImage getImage(String name) throws IOException {
		InputStream is = getClass().getResourceAsStream(name);
		try {
			return ImageIO.read(is);
		} finally {
			Closeables.closeQuietly(is);
		}
	}

}
