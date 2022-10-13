import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HeroGenerator {
//    private static String DIR_PATH = "/Users/kirillzhukov/Downloads/Ahana/";
    private static final String DIR_PATH = "/Users/kirillzhukov/Downloads/OreChain/";
    private static String PIC_NAME = "";
    private static String SUFFIX = "";
    private static String MERGED_PIC_PATH = "";
    Queue q = new Queue(15);

    HeroGenerator(HashMap<String, String> hashMap, String mergedPicturePath) {
//        DIR_PATH = dirPath;
//        PIC_NAME = pictureName;
        MERGED_PIC_PATH = mergedPicturePath;
//        q.add("background");
//        q.add("weapon 2");
//        q.add("body");
//        q.add("hair");
//        q.add("clothes");
//        q.add("eyes");
//        q.add("mouth");
//        q.add("facemark");
//        q.add("headband");
//        q.add("bandanna");
//        q.add("hat");
//        q.add("necklace");
//        q.add("earring");
//        q.add("mask");
//        q.add("weapon 1");

        q.add(hashMap.get("background"));
        q.add(hashMap.get("weapon 2"));
        q.add(hashMap.get("body"));
        q.add(hashMap.get("hair"));
        q.add(hashMap.get("clothes"));
        q.add(hashMap.get("eyes"));
        q.add(hashMap.get("mouth"));
        q.add(hashMap.get("facemark"));
        q.add(hashMap.get("headband"));
        q.add(hashMap.get("bandanna"));
        q.add(hashMap.get("hat"));
        q.add(hashMap.get("necklace"));
        q.add(hashMap.get("earring"));
        q.add(hashMap.get("mask"));
        q.add(hashMap.get("weapon 1"));
    }

    public void generate(HashMap<String, String> myHashMap) throws Exception {
//        URLDownloader downloader = new URLDownloader(myHashMap);
//        downloader.getImages(DIR_PATH);
        ArrayList<File> fileList = MergePic.getAllFiles(DIR_PATH, q, SUFFIX);
        ArrayList<BufferedImage> picList = new ArrayList<BufferedImage>();
        for (File file : fileList) {
            BufferedImage currentPic = MergePic.loadImageLocal(file.getAbsolutePath());
            picList.add(currentPic);
        }
        MergePic.mergeImageTogether(MERGED_PIC_PATH, picList, SUFFIX);
//        downloader.deleteFolderWithImages(DIR_PATH);
        //Combine multiple pictures together
    }

}
