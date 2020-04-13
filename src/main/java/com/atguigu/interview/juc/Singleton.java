package com.atguigu.interview.juc;

/**
 * @author
 *
 */
public class Singleton {
    private static volatile Singleton instance = null;
    private Singleton(){
        System.out.println(Thread.currentThread().getName()+" 创建单例");
    }

    /**
     * 懒汉模式
     * 存在多线程问题
     * @return
     */
    public static Singleton getInstance() {
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * DCL（double check lock）+ volatile
     * @return
     */
    public static Singleton getDCLInstance() {
        if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args){
        // 单线程环境
//        System.out.println("单线程环境:");
//        System.out.println(Singleton.getInstance() == Singleton.getInstance());

        // 多线程并发环境
        System.out.println("多线程并发环境:");
        for (int i = 0; i < 10 ; i++) {
            new Thread(()->{
                Singleton.getDCLInstance();
            }).start();
        }
    }
}
