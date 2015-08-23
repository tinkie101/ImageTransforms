import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by tinkie101 on 2015/08/23.
 */
public class ImageUtilities {

    public static BufferedImage readImage(String file) throws Exception{
        BufferedImage img;
        try {
            img = ImageIO.read(new File(file));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }

        return img;
    }

    public static void drawImages(String title, int height, int width, BufferedImage... ImageList)
    {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        for(int i = 0; i < ImageList.length; i++) {
            JLabel image = new JLabel();
            image.setSize(width, height);
            java.awt.Image org = ImageList[i].getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            image.setIcon(new ImageIcon(org));

            frame.getContentPane().add(image);
        }

        frame.pack();
        frame.setVisible(true);
    }

}
