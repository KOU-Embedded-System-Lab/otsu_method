import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
/**
 * Created by asus/Irma Dibra on 20-Jul-17.
 * Algorithm of  the program
 *
 *separate the pixels into two clusters according to the threshold
 *find the mean of each cluster
 *square the difference between the means
 *multiply by the number of pixels in one cluster times the number in the other
 *
 */
public class otsu {

             /*******************************************************************************************************************************/
    public static int color2RGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
      //  System.out.println("color2rgb "+newPixel);
        return newPixel;

    }

                            /*************************Calculate the image  histogram for wb and wf ***************************/
    public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];//[0-255]

        for(int i=0; i<256; i++)
            histogram[i] = 0;
        //System.out.println("histogram "+histogram.length);
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }

        return histogram;

    }

    public static void main(String args[]) {
        int alpha, red, green, blue, height, width;
        int newPixel;
        float varBetween = 0 ;
        BufferedImage img = null;
        File f = null;
        float sumB = 0;
        int wB = 0;
        int wF = 0;
        float sum = 0 ;
        float varMax = 0;
        int threshold = 0;


        //read image
        try {
            f = new File("D:\\Image\\sunflower.jpg");
            // original image is inhere
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }


        BufferedImage lum = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {

                // Get pixels by  A, R, G, B
                alpha = new Color(img.getRGB(i, j)).getAlpha();
                red = new Color(img.getRGB(i, j)).getRed();
                green = new Color(img.getRGB(i, j)).getGreen();
                blue = new Color(img.getRGB(i, j)).getBlue();

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = 0;
                newPixel += alpha;
                newPixel = newPixel << 8;
                newPixel += red;
                newPixel = newPixel << 8;
                newPixel += green;
                newPixel = newPixel << 8;
                newPixel += blue;


                // Write pixels into image
                lum.setRGB(i, j, newPixel);


            }
        }
       try{
            f = new File("D:\\Image\\sunflower1.jpg");
            ImageIO.write(lum, "jpg", f);
        }catch(IOException e){
            System.out.println(e);
        }
        /* histogram */
        int[] histogram = imageHistogram(img);
        int total = img.getHeight() * img.getWidth();
        System.out.println(total);



        for (int i = 0  ; i <256 ; i++)
            sum += i *histogram[i];



        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
            // between class variance method calculation is done here



            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;

            }
        }
        System.out.println( "threshold = "+threshold);
        System.out.println("varMax "+varMax);
        System.out.println("var between"+varBetween);

        int newPixel1;

        int threshold1 = threshold;

        BufferedImage binarized = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for(int i=0; i<img.getWidth(); i++) {
            for(int j=0; j<img.getHeight(); j++) {

                // Get pixels
                red = new Color(img.getRGB(i, j)).getRed();

                alpha = new Color(img.getRGB(i, j)).getAlpha();
                if(red > threshold1) {
                    newPixel1 = 255;
                }
                else {
                    newPixel1 = 0;
                }
                newPixel1 = color2RGB(alpha, newPixel1, newPixel1, newPixel1);

                binarized.setRGB(i, j, newPixel1);

            }
            try{
                f = new File("D:\\Image\\yes_done.jpg");
                ImageIO.write(binarized, "jpg", f);
            }catch(IOException e){
                System.out.println(e);
            }


    }
}
}
