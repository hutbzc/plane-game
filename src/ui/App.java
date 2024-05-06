package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Zengc
 * Date: 2020/11/12
 * Time: 18:56
 **/
public class App {
    /**
     * static 方法 特点
     * 所有对象都共用
     * 并且，不需要创建对象就可以直接调用
     */
    public static BufferedImage getImg(String path){
        try{
            /**
             * Java的IO流（输入输出流）
             * App.class获取App类的路径
             * getResource()方法获取资源
             */
            BufferedImage img = ImageIO.read(App.class.getResource(path));
            return img;
        } catch (IOException e){
            //找不到图片则输出将异常情况输出
            e.printStackTrace();
        }
        return null;
    }
}
