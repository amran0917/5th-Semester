package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import consol.Game;
import consol.State;

public class RockPaperScissorsMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RockPaperScissorsMain window = new RockPaperScissorsMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public RockPaperScissorsMain() throws IOException {
		initialize();
	}
	
	private static void loadImage(ImagePanel ImagePanel, String filname)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("E:\\6th Semester\\CSE 604 Artificial Intelligence (1T+2L)\\rock paper scissors\\"+filname.toLowerCase()+".jfif"));
            ImagePanel.setImage(image);
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
	
	Game game = new Game();
	String humanResult,computerResult,tie;
	
	public State aiPlay(String humanState) {
		
		State humanChoice = State.valueOf(humanState.toUpperCase());
		State computerChoice = game.nextMove(game.previous);
		game.no_ofMove++;
		
		if(game.previous != null) {
			game.updateTransitionModel(game.previous, humanChoice);
			
		}
		game.previous = humanChoice;
		
		if(computerChoice.equals(humanChoice)) {
			tie = "TIE";
			game.result[2]++;
		}
		else if(computerChoice.losesTo(humanChoice)) {
			humanResult = "WIN";
			computerResult = "LOSE";
			game.result[0]++;
		}
		else {
			humanResult = "LOSE";
			computerResult = "WIN";
			game.result[1]++;
		}
		return computerChoice;
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.menu);
		frame.setBounds(100, 100, 697, 546);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(SystemColor.menu);
		controlPanel.setBounds(119, 381, 435, 100);
		frame.getContentPane().add(controlPanel);
		controlPanel.setLayout(null);
		
		JLabel aiWinCounter = new JLabel("0",SwingConstants.CENTER);
		aiWinCounter.setBounds(267, 44, 46, 14);
		controlPanel.add(aiWinCounter);
		
		JLabel humanWinCounter = new JLabel("0",SwingConstants.CENTER);
		humanWinCounter.setBounds(130, 44, 46, 14);
		controlPanel.add(humanWinCounter);
		
		JLabel aiState = new JLabel(" ",SwingConstants.CENTER);
		aiState.setBounds(329, 39, 65, 24);
		controlPanel.add(aiState);
		
		JLabel humanState = new JLabel(" ",SwingConstants.CENTER);
		humanState.setBounds(39, 39, 65, 24);
		controlPanel.add(humanState);
		
		JLabel tieLabel = new JLabel(" ",SwingConstants.CENTER);
		tieLabel.setBounds(200, 11, 46, 14);
		controlPanel.add(tieLabel);
		
		JLabel tieCounter = new JLabel("0",SwingConstants.CENTER);
		tieCounter.setBounds(200, 44, 46, 14);
		controlPanel.add(tieCounter);
		//humanImageLabel = new JLabel(rockImageIcon);
		
		JButton rockButton = new JButton("ROCK");
		rockButton.setBounds(22, 283, 89, 61);
		frame.getContentPane().add(rockButton);
		rockButton.setBackground(SystemColor.menu);
		rockButton.setBorder(null);
		
		JButton paperButton = new JButton("PAPER");
		paperButton.setBounds(131, 283, 89, 61);
		frame.getContentPane().add(paperButton);
		paperButton.setBackground(SystemColor.menu);
		paperButton.setBorder(null);
		
		JButton scissorsButton = new JButton("SCISSORS");
		scissorsButton.setBounds(237, 283, 89, 61);
		frame.getContentPane().add(scissorsButton);
		scissorsButton.setBackground(SystemColor.menu);
		scissorsButton.setBorder(null);
		
		ImagePanel humanPanel = new ImagePanel();
		humanPanel.setBounds(57, 77, 245, 177);
		frame.getContentPane().add(humanPanel);
		
		
		JLabel lblHuman = new JLabel("Human");
		lblHuman.setBounds(134, 36, 46, 14);
		frame.getContentPane().add(lblHuman);
		
		ImagePanel aiPanel = new ImagePanel();
		aiPanel.setBounds(371, 77, 245, 177);
		frame.getContentPane().add(aiPanel);
		
		JLabel lblComputer = new JLabel("Computer");
		lblComputer.setBounds(476, 36, 64, 14);
		frame.getContentPane().add(lblComputer);
		
		game.initialize();
		
		scissorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadImage(humanPanel,scissorsButton.getText());
				//System.out.println(scissorsButton.getText());
				
				String aichoice = aiPlay(scissorsButton.getText()).toString();
				loadImage(aiPanel, aichoice);
				aiState.setText(computerResult);
				humanState.setText(humanResult);
				
				aiWinCounter.setText(Integer.toString(game.result[1]));
				humanWinCounter.setText(Integer.toString(game.result[0]));
				tieLabel.setText(tie);
				tieCounter.setText(Integer.toString(game.result[2]));
				
			}
		});
		paperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImage(humanPanel,paperButton.getText());
				
				String aichoice = aiPlay(paperButton.getText()).toString();
				loadImage(aiPanel, aichoice);
				aiState.setText(computerResult);
				humanState.setText(humanResult);
				aiWinCounter.setText(Integer.toString(game.result[1]));
				humanWinCounter.setText(Integer.toString(game.result[0]));
				tieLabel.setText(tie);
				tieCounter.setText(Integer.toString(game.result[2]));
			}
		});
		rockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadImage(humanPanel,rockButton.getText());	
				
				String aichoice = aiPlay(rockButton.getText()).toString();
				loadImage(aiPanel, aichoice);
				aiState.setText(computerResult);
				humanState.setText(humanResult);
				aiWinCounter.setText(Integer.toString(game.result[1]));
				humanWinCounter.setText(Integer.toString(game.result[0]));
				tieLabel.setText(tie);
				tieCounter.setText(Integer.toString(game.result[2]));
			}
		});
	}
}
