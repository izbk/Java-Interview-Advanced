package com.atguigu.interview.juc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author
 * CAS->Unsafe->CAS底层思想->ABA->原子引用更新->如何规避ABA问题
 */
public class AtomicReferenceDemo {
    public static void main(String[] args){
        User z3 = new User("z3",22);
        User l4 = new User("l4",25);
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(z3);
        System.out.println(atomicReference.compareAndSet(z3,l4)+"\t"+atomicReference.get());
        System.out.println(atomicReference.compareAndSet(z3,l4)+"\t"+atomicReference.get());

    }
}

@Data
@ToString
@AllArgsConstructor
class User{
    String userName;
    int age;
}
