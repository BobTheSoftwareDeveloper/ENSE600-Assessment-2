
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

/**
 *
 * @author Bob Liou - 18013456
 */
public class ImageGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Image");
        JLabel imgLabel = new JLabel();
        BufferedImage img;
        try {
            img = ImageIO.read(new File("pawn_80.png"));
            ImageIcon icon = new ImageIcon(img);
            imgLabel.setIcon(icon);
            imgLabel.setPreferredSize(new Dimension(80, 80));
            frame.setLayout(new GridLayout());
            frame.add(imgLabel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ImageGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
