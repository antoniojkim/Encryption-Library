package Encryption_Library.Tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Antonio on 2017-07-12.
 */
public class IO {

    public static BufferedImage loadImage(String path){
        try {
            return(ImageIO.read(new FileInputStream(path)));
        } catch (IOException e) {
        }
        return null;
    }
    public static void saveImage(BufferedImage bufImage, String path){
        try {
            File file = new File(path);
            //            if (!file.exists()){
            //                file.createNewFile();
            //            }
            ImageIO.write(bufImage, "png", file);
        } catch (IOException e) { }
    }


    public static BufferedReader filereader(String path){
        try {
            return new BufferedReader (new FileReader(path));
        } catch (FileNotFoundException ex) {        }
        return null;
    }
    public static PrintWriter printwriter(String path){
        try {
            return new PrintWriter (new FileWriter (path));
        } catch (IOException ex) {        }
        return null;
    }

}
