//package org.jimmy.autosearch2019.test;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class MergePic {
    private static final AffineTransform at = AffineTransform.getScaleInstance(0.458, 0.458);
    private static final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
    public static ArrayList<File> getAllFiles(String path, Queue q, String suffix) {
        ArrayList<File> fileList = new ArrayList<File>();
        suffix = "png";
        int size = q.size();
        for (int i = 0; i < size; i++) {
//            String fileName = path + q.pop() + "." + suffix;
            String fileName = path + q.pop();
            File file = new File(fileName);
            if (file.exists()) {
                System.out.printf("File exists %s\n", file.getAbsolutePath());
                fileList.add(file);
            } else {
                System.out.printf("File not exists: %s\n", fileName);
            }
        }
        return fileList;
    }

    public static void mergeImageTogether(String path, ArrayList<BufferedImage> picList, String suffix) throws IOException {
        try {
            BufferedImage firstImage = picList.get(0);
            int w1 = firstImage.getWidth();
            int h1 = firstImage.getHeight();
            int width = w1;
            int height = h1;
            BufferedImage newImage = new BufferedImage(width, height, firstImage.getType());
            Graphics g = newImage.getGraphics();
            int x = 0;
            int y = 0;
            for (int i = 0; i < picList.size(); i++) {
                BufferedImage currentImage = picList.get(i);
                y = h1;
                g.drawImage(currentImage, 0, 0, w1, h1, null);
            }
            BufferedImage scaledImage = new BufferedImage((int) (newImage.getWidth()*0.458), (int) (newImage.getHeight()*0.458), BufferedImage.TYPE_INT_RGB);
            scaledImage = ato.filter(newImage, scaledImage);
            File outputfile = new File(path+"/"+UUID.randomUUID().toString() +".jpg");
            ImageIO.write(scaledImage, "jpg", outputfile);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}