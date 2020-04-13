package com.atguigu.interview.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyData{
    /**
     * 可见性验证，可注释后体验效果
     */
    volatile int number = 0;
    AtomicInteger atomic = new AtomicInteger(0);
    public void add(){
        this.number = 10;
    }
    public void plus(){
        this.number++;
    }
    public void atomicPlus(){
        atomic.getAndIncrement();
    }
}
class ReSortSeqDemo{
    int a = 0;
    boolean flag = false;

    public void method(){
        a = 1;
        flag = true;
    }
    /**
     * 多线程环境中线程交替执行，由于编译器优化指令重排序的存在
     * 两个线程中使用的变量能够保证一致性无法确定
     */
    public void method2(){
        if(flag){
            a = a + 5;
            System.out.println("a="+a);
        }
    }
}

/**
 * @author
 */
public class VolatileDemo {
    public static void main(String[] args) {
        noAtomicVolatile();
    }

    /**
     * 不保证原子性验证
     */
    private static void noAtomicVolatile() {
        MyData myData = new MyData();
        for (int i = 0; i < 20 ; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    myData.plus();
                    myData.atomicPlus();
                }
            },String.valueOf(i)).start();
        }
        while(Thread.activeCount() >2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " finally number is :"+myData.number);
        System.out.println(Thread.currentThread().getName() + " finally atomic is :"+myData.atomic);
    }

    /**
     * 可见性验证
     */
    private static void seeOkByVolatile() {
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
