package com.atguigu.interview.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author
 * CAS 比较并交换
 */
    public class CASDemo {
    public static void main(String[] args){
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println(atomicInteger.compareAndSet(0,1)+" current data = "+atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(0,2)+" current data = "+atomicInteger.get());
        atomicInteger.getAndIncrement();
    }
}
