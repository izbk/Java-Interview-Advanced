package com.atguigu.interview.juc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author
 */
public class ReadWriteLockDemo {
    public static void main(String[] args){
        MyCache cache = new MyCache();
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(()->{
                cache.put(String.valueOf(temp),temp);
            },String.valueOf(i)).start();
        }
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(()->{
                cache.get(String.valueOf(temp));
            },String.valueOf(i)).start();
        }
    }
}

class MyCache{
    private volatile Map<String,Object> map = new ConcurrentHashMap<>();
    /**
     * 尝试注释锁，体验效果
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(String key,Object value){
        System.out.println(Thread.currentThread().getName()+"\t 正在写入");
        lock.writeLock().lock();
        try {
            TimeUnit.MILLISECONDS.sleep(300L);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName()+"\t  写入完成");
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void get(String key){
        System.out.println(Thread.currentThread().getName()+"\t 正在读取");
        lock.readLock().lock();
        try {
            TimeUnit.MILLISECONDS.sleep(100L);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName()+"\t  读取完成" + o);
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            lock.readLock().unlock();
        }
    }

}
