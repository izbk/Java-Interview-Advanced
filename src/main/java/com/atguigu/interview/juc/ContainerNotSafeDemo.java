package com.atguigu.interview.juc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args){
        mapNotSafe();
    }

    private static void mapNotSafe() {
        Map<String,String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

    private static void setNotSafe() {
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
        // HashSet 与 HashMap
        new HashSet<String>().add("a");
    }

    private static void listNotSafe() {
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }

        /**
         * 故障现场
         * java.util.ConcurrentModificationException
         * 导致原因
         *
         * 解决方案
         * 1.new Vector<>();
         * 2.Collections.synchronizedList(new ArrayList<>());
         * 3.CopyOnWriteArrayList();
         * CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，
         * 不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容
         * 器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们
         * 可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。
         * 所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
         * CopyOnWrite的缺点
         * 　　内存占用问题。因为CopyOnWrite的写时复制机制，所以在进行写操作的时候，内存
         * 里会同时驻扎两个对象的内存，旧的对象和新写入的对象（注意:在复制的时候只是复制容
         * 器里的引用，只是在写的时候会创建新对象添加到新容器里，而旧容器的对象还在使用，
         * 所以有两份对象内存）。如果这些对象占用的内存比较大，比如说200M左右，那么再
         * 写入100M数据进去，内存就会占用300M，那么这个时候很有可能造成频繁的Yong GC和Full GC。
         * 之前我们系统中使用了一个服务由于每晚使用CopyOnWrite机制更新大对象，造成了每晚15秒的
         * Full GC，应用响应时间也随之变长。
         * 　　针对内存占用问题，可以通过压缩容器中的元素的方法来减少大对象的内存消耗，比如，如果
         * 元素全是10进制的数字，可以考虑把它压缩成36进制或64进制。或者不使用CopyOnWrite容器，
         * 而使用其他的并发容器，如ConcurrentHashMap。
         *
         * 　　数据一致性问题。CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性。
         * 所以如果你希望写入的的数据，马上能读到，请不要使用CopyOnWrite容器。
         */}
}
