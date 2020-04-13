package com.atguigu.interview.juc;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author
 */
public class LockDemo {
    public static void main(String[] args){
        // 默认非公平锁
        // 可重入锁 避免死锁
        ReentrantLock lock = new ReentrantLock();

        // synchronized 可重入锁
        Phone phone = new Phone();
        new Thread(()->{phone.sendMsg();},"t1").start();
        new Thread(()->{phone.sendMsg();},"t2").start();

        // ReentrantLock 可重入锁
        new Thread(phone).start();
        new Thread(phone).start();

        // 自旋锁 spinlock CAS核心原理
        // 尝试获取锁的线程不会阻塞，而是采用循环的方式尝试获取锁。

    }
}

class Phone implements Runnable{
    public synchronized void  sendMsg() {
        System.out.println(Thread.currentThread().getName()+"\t invoked sendMsg()");
        sendEmail();
    }
    public synchronized void  sendEmail() {
        System.out.println(Thread.currentThread().getName()+"\t invoked sendEmail()");
    }

    Lock lock = new ReentrantLock();
    @Override
    public void run() {
        get();
    }
    public void get(){
        // 可多次加锁，只要和unlock匹配即可
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"\t invoked get()");
            set();
        }finally {
            lock.unlock();
        }
    }
    public void set(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"\t invoked set()");
        }finally {
            lock.unlock();
        }
    }
}
