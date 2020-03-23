package com.atguigu.interview.juc;

import java.util.concurrent.TimeUnit;

class MyData{
    // 可见性验证，可注释后体验效果
    volatile int number = 0;
    public void add(){
        this.number = 10;
    }
}
public class VolatileDemo {
    public static void main(String[] args) {
        MyData myData = new MyData();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch(Exception e) {
                e.printStackTrace();
            }
            myData.add();
            System.out.println(Thread.currentThread().getName()+" updated number");
        },"thread0").start();

        while(myData.number == 0){
            // main 循环等待
        }
        System.out.println(Thread.currentThread().getName()+" task is over");
    }

}
