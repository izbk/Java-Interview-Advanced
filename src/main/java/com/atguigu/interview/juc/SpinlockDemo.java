package com.atguigu.interview.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author
 */
public class SpinlockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName()+"\t invoked lock");
        while(!atomicReference.compareAndSet(null,thread)){}
    }
    public void unlock(){
        Thread thread = Thread.currentThread();
        while(atomicReference.compareAndSet(thread,null)){}
        System.out.println(thread.getName()+"\t invoked unlock");
    }

    public static void main(String[] args){
        SpinlockDemo lock = new SpinlockDemo();
        new Thread(()->{
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch(Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"AA").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch(Exception e) {
            e.printStackTrace();
        }

        new Thread(()->{
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch(Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"BB").start();
    }

}
