package com.jvm;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-16
 */
public class ObjectHead {

    static class Consumer {
        private String name;
        private int age;
    }

    // 对象头
    //  32 bits:
    //  --------
    //             hash:25 ------------>| age:4    biased_lock:1 lock:2 (normal object)
    //             JavaThread*:23 epoch:2 age:4    biased_lock:1 lock:2 (biased object)
    //             size:32 ------------------------------------------>| (CMS free block)
    //             PromotedObject*:29 ---------->| promo_bits:3 ----->| (CMS promoted object)
    //
    //  64 bits:
    //  --------
    //  unused:25 hash:31 -->| unused:1   age:4    biased_lock:1 lock:2 (normal object)
    //  JavaThread*:54 epoch:2 unused:1   age:4    biased_lock:1 lock:2 (biased object)
    //  PromotedObject*:61 --------------------->| promo_bits:3 ----->| (CMS promoted object)
    //  size:64 ----------------------------------------------------->| (CMS free block)
    //
    //  unused:25 hash:31 -->| cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && normal object)
    //  JavaThread*:54 epoch:2 cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && biased object)
    //  narrowOop:32 unused:24 cms_free:1 unused:4 promo_bits:3 ----->| (COOPs && CMS promoted object)
    //  unused:21 size:35 -->| cms_free:1 unused:7 ------------------>| (COOPs && CMS free block)

    // https://wiki.openjdk.org/display/lilliput/Main 对象头
    // https://wiki.openjdk.org/display/HotSpot/Synchronization synchronized锁

    public static void main(String[] args) {

        // 对象：对象头 + 实例域 + 对齐填充
        //      对象头 = MarkWord(8字节) + Klass(8字节) + 数组长度(4字节，仅针对数组，才有这部分)
        // VM默认参数 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops


        // 当前VM，对象头大小 12字节：8字节（64位）MarkWord，4字节（32位）klass
        // 默认vm参数启用了类型地址压缩，所以 klass 大小变为4字节，本来对象头应该是16字节，
        // 通过设置参数是 -XX:-UseCompressedClassPointers 关闭类型压缩
        System.out.println(VM.current().objectHeaderSize());
        // 对象内存分配单位：8字节： 对象地址起始必须是8的整数倍（以字节为单位），64位虚拟机嘛，所以 64bit = 8 Byte
        System.out.println(VM.current().objectAlignment());


        // Object 类型对象 在堆中分配内存大小
        // 对象头：12字节（8字节MarkWord+压缩后的4字节Klass+无数组长度）
        // 没有实例域，0字节
        // 填充4字节，对齐为 16字节
        // 大小：16字节
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        // Consumer 类型对象 在堆中分配内存大小
        // 对象头：12字节（8字节MarkWord+压缩后的4字节Klass+无数组长度）
        // 实例域，8字节，string name(4字节) + int age（4字节）
        // 填充4字节，对齐为 24字节
        // 大小：24字节
        System.out.println(ClassLayout.parseInstance(new Consumer()).toPrintable());

        // int[4] 类型对象 在堆中分配内存大小
        // 对象头：16字节（8字节MarkWord+压缩后的4字节Klass+4字节数组长度）
        // 实例域，4个int 4 * 4 = 16 字节
        // 不需要填充对齐
        // 大小：32字节
        System.out.println(ClassLayout.parseInstance(new int[4]).toPrintable());
    }
}
