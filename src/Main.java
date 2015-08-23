import FourierTransform.FourierTransform;

import java.awt.image.BufferedImage;

/**
 * Created by tinkie101 on 2015/08/23.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        BufferedImage image1 = ImageUtilities.readImage("images/drawing.png");
        BufferedImage image2 = ImageUtilities.readImage("images/film.png");
        BufferedImage lena = ImageUtilities.readImage("images/lena.png");
        BufferedImage image4 = ImageUtilities.readImage("images/microphone.png");
        BufferedImage image5 = ImageUtilities.readImage("images/screw.png");

        ImageUtilities.drawImages("Normal Images", image1.getWidth(), image1.getHeight(), image1, image2, lena, image4, image5);

        FourierTransform dft = new FourierTransform();

//        BufferedImage resultImage1 = FourierTransform.generateFourierImage(image1);
//        BufferedImage resultImage2 = FourierTransform.generateFourierImage(image2);
        lena = dft.rotateImage(lena);
        BufferedImage resultLena = dft.generateFourierImage(lena);
        ImageUtilities.drawImages("Discrete Fourier Transform Images", resultLena.getWidth(), resultLena.getHeight(), resultLena);
        BufferedImage result2Lena = dft.getReverseFourierImage(resultLena);
        result2Lena = dft.rotateImage(result2Lena);
//        BufferedImage resultImage4 = FourierTransform.generateFourierImage(image4);
//        BufferedImage resultImage5 = FourierTransform.generateFourierImage(image5);

        ImageUtilities.drawImages("Discrete Fourier Transform Images", resultLena.getWidth(), resultLena.getHeight(), result2Lena);
    }
}
