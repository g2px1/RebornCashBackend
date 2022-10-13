import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class URLDownloader {
    private static final HashMap<String, String> myMap = new HashMap<String, String>();


    URLDownloader(HashMap<String, String> m) {
        myMap.putAll(m);
    }

    //    Set<String> hashSet;
    static void getImages(String path) {
        new File(path).mkdirs();
        Set<String> hashSet = myMap.keySet();
        for (String URL : hashSet) {
            java.net.URL url = null;
            try {
                url = new URL(myMap.get(URL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedImage img = null;
            try {
                assert url != null;
                img = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file = new File(path + URL + ".png");
            try {
                assert img != null;
                ImageIO.write(img, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFolderWithImages(String dirPath) throws Exception {
        File file = new File(dirPath);
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                deleteFolderWithImages(subFile.getAbsolutePath());
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }
}

