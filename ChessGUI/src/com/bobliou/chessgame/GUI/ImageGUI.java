package com.bobliou.chessgame.GUI;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Bob Liou - 18013456
 */
public class ImageGUI {

    public static ImageIcon getImage(String name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images/" + name));
//            ImageIcon icon = new ImageIcon(img);
//            imgLabel.setIcon(icon);
//            imgLabel.setPreferredSize(new Dimension(80, 80));
        } catch (IOException ex) {
            Logger.getLogger(ImageGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIcon imageIcon = new ImageIcon(img);
        return imageIcon;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Image");

        // layered panel 
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(640, 640));
        layeredPane.setVisible(true);
        
        // game board
        JPanel boardPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        boardPanel.setOpaque(false);
        boardPanel.setPreferredSize(new Dimension(640, 640));
        boardPanel.setBounds(0, 0, 640, 640);
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JLabel label = new JLabel(getImage("blackSquare.png"));
                boardPanel.add(label);
            }
        }
        
        JPanel highlightPanel;
        highlightPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        highlightPanel.setOpaque(false);
        highlightPanel.setPreferredSize(new Dimension(640, 640));
        highlightPanel.setBounds(0, 0, 640, 640);

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                JLabel label = new JLabel(getImage("pawn_80.png"));
//                highlightArray[row][col] = label;
                highlightPanel.add(label);
            }
        }
        
        layeredPane.add(boardPanel, new Integer(0));
        layeredPane.add(highlightPanel, new Integer(1));

        frame.setLayout(new GridLayout());
        frame.add(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
