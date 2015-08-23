package FourierTransform;

/* Library found at: http://commons.apache.org/proper/commons-math/download_math.cgi */
import org.apache.commons.math3.complex.Complex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by tinkie101 on 2015/08/16.
 */
public class FourierTransform {

    class newPixel
    {
        int uPos, vPos;
        double value;

        newPixel(int uPos, int vPos, double value){
            this.uPos = uPos;
            this.vPos = vPos;
            this.value = value;
        }
    }

    class CalculateFourierPixel implements Callable
    {
        private final BufferedImage image;
        private final int u, v, N;

        CalculateFourierPixel(final BufferedImage image, final int u, final int v, final int N)
        {
            this.image = image;
            this.u = u;
            this.v = v;
            this.N = N;
        }

        @Override
        public newPixel call()
        {
            Complex sum = new Complex(0.0d, 0.0d);

            for(int x = 0; x < image.getWidth(); x++){
                for(int y = 0; y < image.getHeight(); y++){
                    Color colour = new Color(image.getRGB(x, y));

                    int averageGray = (colour.getRed() + colour.getGreen() + colour.getBlue())/3;
                    double phase = ((-2.0d * Math.PI)/((double)N)) * (double)(u*x + v*y);

                    Complex complex = new Complex(Math.cos(phase), Math.sin(phase));
                    complex = complex.multiply((double)averageGray);
                    sum = sum.add(complex);
                }
            }

            double scaleVal = 255.0d / (Math.log(1 + 7.9e6)) * Math.log(1 + ((1.0d / N) * sum.abs()));

//            double scaleVal = (1.0d/N)*sum.abs();
//
//            if(scaleVal > 255)
//                scaleVal = 255;
//            else if(scaleVal < 0)
//                scaleVal = 0;

            return new newPixel(u, v, scaleVal);
        }
    }

    class CalculateReverseFourierPixel implements Callable
    {
        private final BufferedImage image;
        private final int x, y, N;

        CalculateReverseFourierPixel(final BufferedImage image, final int x, final int y, final int N)
        {
            this.image = image;
            this.x = x;
            this.y = y;
            this.N = N;
        }

        @Override
        public newPixel call()
        {
            Complex sum = new Complex(0.0d, 0.0d);

            for(int u = 0; u < image.getWidth(); u++){
                for(int v = 0; v < image.getHeight(); v++){
                    Color colour = new Color(image.getRGB(u, v));

                    int averageGray = (colour.getRed() + colour.getGreen() + colour.getBlue())/3;

                    double phase = ((2.0d * Math.PI)/((double)N)) * (double)(u*x + v*y);

                    Complex complex = new Complex(Math.cos(phase), Math.sin(phase));
                    complex = complex.multiply((double) averageGray);
                    sum = sum.add(complex);
                }
            }

            double scaleVal = 255.0d / (Math.log(1 + 7.9e6)) * Math.log(1 + (sum.abs()));

            return new newPixel(x, y, scaleVal);
        }
    }


    public BufferedImage generateFourierImage(BufferedImage image) throws Exception{
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int N = image.getHeight();

        for(int u = 0; u < result.getWidth(); u++){

            ExecutorService threadPool = Executors.newFixedThreadPool(result.getWidth());
            Set<Future<newPixel>> set = new HashSet<>();

            for(int v = 0; v < result.getHeight(); v++){

                Callable<newPixel> callable =  new CalculateFourierPixel(image, u, v, N);
                Future<newPixel> future = threadPool.submit(callable);
                set.add(future);
            }

            for (Future<newPixel> future : set) {
                newPixel tempPixel = future.get();

                int pixelValue = (int)tempPixel.value;

                result.setRGB(tempPixel.uPos, tempPixel.vPos, new Color(pixelValue, pixelValue, pixelValue).getRGB());
            }

            threadPool.shutdown();

            System.out.print("\r" + u + ":" + N);
        }

        return result;
    }

    public BufferedImage getReverseFourierImage(BufferedImage image) throws Exception{
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int N = image.getHeight();

        for(int x = 0; x < result.getWidth(); x++){

            ExecutorService threadPool = Executors.newFixedThreadPool(result.getWidth());
            Set<Future<newPixel>> set = new HashSet<>();

            for(int y = 0; y < result.getHeight(); y++){

                Callable<newPixel> callable =  new CalculateReverseFourierPixel(image, x, y, N);
                Future<newPixel> future = threadPool.submit(callable);
                set.add(future);
            }

            for (Future<newPixel> future : set) {
                newPixel tempPixel = future.get();

                int pixelValue = (int)tempPixel.value;

                result.setRGB(tempPixel.uPos, tempPixel.vPos, new Color(pixelValue, pixelValue, pixelValue).getRGB());
            }

            threadPool.shutdown();

            System.out.print("\r" + x + ":" + N);
        }

        return result;
    }

    public BufferedImage rotateImage(BufferedImage image){
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                Color temp = new Color(image.getRGB(x,y));
                int averageColour = (temp.getRed() + temp.getGreen() + temp.getBlue())/3;

                int newColour = averageColour * (int)Math.pow(-1, x+y);

                result.setRGB(x,y, newColour);
            }
        }
        return result;
    }
}
