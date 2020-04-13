package com.atguigu.interview.juc;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author
 */
public class ABADemo {
    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100,1);
    public static void main(String[] args) {
        System.out.println("----------------ABA Demo------------------");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            try {
                // 保证t1线程完成一次ABA
                Thread.sleep(1000L);
                System.out.println(atomicReference.compareAndSet(100, 102) +
                        "\t" + atomicReference.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2").start();

        System.out.println("----------------ABA Solution------------------");
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号" + stamp);
            try {Thread.sleep(1000L);} catch (Exception e) {e.printStackTrace();}
            atomicStampedReference.compareAndSet(100, 101,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t第2次版本号" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t第3次版本号" + atomicStampedReference.getStamp());

        }, "t3").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号" + stamp);
            try {Thread.sleep(3000L);} catch (Exception e) {e.printStackTrace();}
            boolean b = atomicStampedReference.compareAndSet(100, 102,
                    stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName()+"\t 修改成功："+b);
        }, "t4").start();

    }
}
