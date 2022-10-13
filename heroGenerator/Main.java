import java.util.HashMap;


public class Main {
    private static final String DIR_PATH = "./img/downImgs/";
    private static final String PIC_NAME = "test";
    private static final String SUFFIX = "png";
//    private static final String MERGED_PIC_PATH = "/Users/kirillzhukov/Downloads/Ahana/test/";
    private static final String MERGED_PIC_PATH = "/Users/kirillzhukov/Downloads/OreChain/test";

    public static void main(String[] args) throws Exception {
        HashMap<String, String> myHashMap = new HashMap<>();
//        myHashMap.put("background", "Background.PNG");
//        myHashMap.put("clothes", "Clothes.PNG");
//        myHashMap.put("earring", "Earrings.PNG");
//        myHashMap.put("mouth", "Mouth .PNG");
//        myHashMap.put("hair", "Hair_shadow .PNG");
//        myHashMap.put("headband", "Eyeglasses.PNG");
//        myHashMap.put("eyes", "Eyes.PNG");
//        myHashMap.put("necklace", "Necklace.PNG");
//        myHashMap.put("body", "Body_head.PNG");

        myHashMap.put("background", "Background/BG_0001.png");
        myHashMap.put("clothes", "Clothes/C_F_0001.png");
        myHashMap.put("hair", "Hair/H_0003.png");
        myHashMap.put("eyes", "Faces/F_0002.png");
//        myHashMap.put("headband", "Hats/Hat_0001.png");
        myHashMap.put("headband", "Faces/Eyes.PNG");
        myHashMap.put("body", "BodyHeadEars/BHE_0001.png");
        HeroGenerator generator = new HeroGenerator(myHashMap, MERGED_PIC_PATH);
        generator.generate(myHashMap);
    }
}
