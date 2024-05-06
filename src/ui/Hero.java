package ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Zengc
 * Date: 2020/11/12
 * Time: 20:23
 **/
public class Hero extends FlyObject{
    int hp;                       //英雄机的血量
    public Hero(){
        img=App.getImg("/img/hero.png");
        //英雄机的坐标（x，y），此处是英雄机图片的左上角的坐标
        x=200;
        y=500;
        hp=5;
        //确定所绘制的英雄机的宽、高（此处分别等于图片的宽和高）
        w=img.getWidth();
        h=img.getHeight();
    }
    // 让英雄机移动到“鼠标位置“
    public void moveToMouse(int mx,int my){
        x=mx-w/2;               //让英雄机的中心点（而不是左上角）位于鼠标位置
        y=my-w/2;
    }

    /**
     * 键盘控制时所调用的函数
     * 其中的if条件判断是为了确保英雄机始终处于屏幕内
     */
    public void moveUp(){
        y-=20;
        if(y<0)
            y=0;
    }

    public void moveDown(){
        y+=20;
        if(y>768)
            y=768;
    }

    public void moveLeft(){
        x-=20;
        if(x<0)
            x=0;
    }

    public void moveRight(){
        x+=20;
        if(x>512)
            x=512;
    }
}
