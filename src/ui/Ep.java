package ui;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Zengc
 * Date: 2020/11/12
 * Time: 21:54
 **/
public class Ep extends FlyObject{
    int speed;      //敌机的移动速度
    int hp=6;       //敌机的血量
    int type;       //敌机的类型（用于确定Boss机，奖品等）
    public Ep(){

        //获取随机数
        Random rd=new Random();

        //将随机数取整（rd.nextInt()范围是[0,15)），所以index范围是[1,16)即[1,15]
        int index=rd.nextInt(15)+1;

        //确定路径，如果index在1~9，则在index前加0，否则不加
        String path="/img/ep"+(index<10?"0":"")+index+".png";

        //通过工具类App 获得图片
        img=App.getImg(path);

        //绘制大小和图片的大小一致
        w=img.getWidth();
        h=img.getHeight();

        //位置横坐标范围为[0,512-w)
        //纵坐标为-h ，让其开始位置在屏幕外
        x=rd.nextInt(512-w);
        y=-h;

        /**
         * 图片的编号与大小正相关
         * 此处想让大飞机速度慢
         *         小飞机速度块
         * 因此，让敌机速度与编号成反相关
         */
        speed=17-index;

        //记录飞机型号
        type=index;
    }

    //敌飞向下移动
    public void move() {
        y+=speed;
    }

    //判断敌机是否被击中
    public boolean shootBy(Fire fire) {
        //画图即可判断清楚条件
        boolean shoot = x<=fire.x+fire.w &&
                     x>=fire.x-w       &&
                     y<=fire.y+fire.h  &&
                     y>=fire.y-h;
        return  shoot;
    }

    public boolean hitBy(Hero hero) {
        //与ShootBy(Fire fire)类似
        boolean hit = x<=hero.x+hero.w &&
                x>=hero.x-w &&
                y<=hero.y+hero.h &&
                y>=hero.y-h;
        return hit;
    }
}
