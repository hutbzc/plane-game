package ui;

/**
 * Created by IntelliJ IDEA.
 * User: Zengc
 * Date: 2020/11/13
 * Time: 11:25
 **/
public class Fire extends FlyObject{

    //英雄机的横纵坐标(hx，hy)
    public Fire(int hx,int hy){

        //通过工具类App 获得子弹的图片
        img=App.getImg("/img/fire.png");

        //绘制的大小为图片大小的1/4
        w=img.getWidth()/4;
        h=img.getHeight()/4;

        //子弹的起始位置在英雄机的位置
        x=hx;
        y=hy;
    }

    public void move() {
        y-=10;
    }
}
