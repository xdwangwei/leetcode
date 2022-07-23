package com.jvm;

import java.util.concurrent.TimeUnit;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-16
 */
public class SynchronizedUpgrade {

    /**
     * 根据《Java虚拟机规范》的要求，在执行monitorenter指令时，首先要去尝试获取对象的锁。
     * 如果这个对象没被锁定，或者当前线程已经持有了那个对象的锁，就把锁的计数器的值增加一，
     * 而在执行monitorexit指令时会将锁计数器的值减一。一旦计数器的值为零，锁随即就被释放了。
     * 如果获取对象锁失败，那当前线程就应当被阻塞等待，直到请求锁定的对象被持有它的线程释放为止。
     *
     * 从功能上看，根据以上《Java虚拟机规范》对monitorenter和monitorexit的行为描述，我们可以得出两个关于synchronized的直接推论，这是使用它时需特别注意的：
     *      被synchronized修饰的同步块对同一条线程来说是可重入的。这意味着同一线程反复进入同步块也不会出现自己把自己锁死的情况。
     *      被synchronized修饰的同步块在持有锁的线程执行完毕并释放锁之前，会无条件地阻塞后面其他线程的进入。
     *      这意味着无法像处理某些数据库中的锁那样，强制已获取锁的线程释放锁；也无法强制正在等待锁的线程中断等待或超时退出。
     *
     * 从执行成本的角度看，持有锁是一个重量级（Heavy-Weight）的操作。
     * 在第10章中我们知道了在主流Java虚拟机实现中，Java的线程是映射到操作系统的原生内核线程之上的，如果要阻塞或唤醒一条线程，则需要操作系统来帮忙完成，
     * 这就不可避免地陷入用户态到核心态的转换中，进行这种状态转换需要耗费很多的处理器时间。尤其是对于代码特别简单的同步块（譬如被synchronized修饰的getter()
     * 或setter()方法），状态转换消耗的时间甚至会比用户代码本身执行的时间还要长。
     * 因此才说，synchronized是Java语言中一个重量级的操作，有经验的程序员都只会在确实必要的情况下才使用这种操作。
     * 而虚拟机本身也会进行一些优化，譬如在通知操作系统阻塞线程之前加入一段自旋等待过程，以避免频繁地切入核心态之中
     */


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

    /**
     * 无锁态 25位 unused + 31位 hashcode + 1位 unused + 4位 分代年龄 + 1位 锁偏向标志 + 2位 锁状态
     * 注意 hashcode 只有在此一次调用hashcode方法后才会生成并存储在对象头中，默认全0
     */
    public static void noLockState() {
        // object header: mark + klass + length 只关注 MarkWord
        // jdk11 0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
        // jdk8  0   8        (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
        // jdk11 默认偏向状态是 1，也就是说默认是偏向锁状态（在 edit configuration中修改本类的jdk）
        // 关注 jdk8，默认无锁状态，jvm启动4000ms后，新建对象会变为偏向锁状态
        // hashcode 全0，最后 4位 0001 ：偏向状态 0，锁状态 01 表示 无锁
        //                                             00 表示 轻量级锁
        //                                             10 表示 重量级锁
        //                                             11 表示 GC标记
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        Object o = new Object();
        System.out.println(o.hashCode());
        //  0   8        (object header: mark)     0x00000039a054a501 (hash: 0x39a054a5; age: 0)
        // 最后四位 0001，偏向状态0，锁状态01
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        try {
            TimeUnit.MILLISECONDS.sleep(4000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        // 4000ms后
        //   0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
        // 最后3位 101，偏向状态1，锁状态 01
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        // 4s延时已过，偏向锁已开启
        synchronized (o) {
            // 此时对象头MarkWord最后3为101，偏向状态1，锁状态01
            // jol已升级，可直接看出持有偏向锁的线程id(前54位)
            // 0   8        (object header: mark)     0x00007fbfdf808805 (biased: 0x0000001feff7e022; epoch: 0; age: 0)
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    // jdk11
    // ➜  ~ java -XX:+PrintFlagsInitial | grep 'BiasedLock'
    // intx BiasedLockingBulkRebiasThreshold         = 20                                        {product} {default}
    // intx BiasedLockingBulkRevokeThreshold         = 40                                        {product} {default}
    // intx BiasedLockingDecayTime                   = 25000                                     {product} {default}
    // intx BiasedLockingStartupDelay                = 0                                         {product} {default}
    // bool UseBiasedLocking                         = true

    // jdk8
    // ➜  ~ java -XX:+PrintFlagsInitial | grep 'BiasedLock'
    // intx BiasedLockingBulkRebiasThreshold          = 20                                  {product}
    // intx BiasedLockingBulkRevokeThreshold          = 40                                  {product}
    // intx BiasedLockingDecayTime                    = 25000                               {product}
    // intx BiasedLockingStartupDelay                 = 4000                                {product}
    // bool TraceBiasedLocking                        = false                               {product}
    // bool UseBiasedLocking                          = true

    // 二者都 UseBiasedLocking=true，但是 BiasedLockingStartupDelay 不同，jdk8是4s，jdk11是0s
    // 通过 -XX:BiasedLockingStartupDelay=0 关闭延时
    // 通过 -XX:-UseBiasedLocking           禁用偏向锁


    /**
     * 轻量级锁：
     * 1。jdk8，启动4s内由于偏向锁还未启动，所以在此期间使用synchronized(obj)发现对象obj的对象头MarkWord状态是 轻量级锁状态，也就是最后3位是 000
     * 2。线程B尝试获取锁lock，发现lock的MarkWord的线程id不是自己，线程B尝试cas将其改为自己
     * 修改成功则lock仍为偏向锁，MarkWord中线程ID指向线程B
     * 修改失败则lock变为轻量级锁，线程A仍然持有lock，线程B自旋。（轻量级锁MarkWord保存的是线程栈中锁记录的指针）
     *
     * 轻量级锁的工作过程：
     * 在代码即将进入同步块的时候，如果此同步对象没有被锁定（锁标志位为“01”状态），虚拟机首先将在当前线程的栈帧中建立一个名为锁记录（Lock Record）的空间，
     * 用于存储锁对象目前的Mark Word的拷贝（官方为这份拷贝加了一个Displaced前缀，即Displaced Mark Word）.
     * 然后，虚拟机将使用CAS操作尝试把对象的Mark Word更新为指向Lock Record的指针。
     * 如果这个更新动作成功了，即代表该线程拥有了这个对象的锁，并且对象Mark Word的锁标志位（Mark Word的最后两个比特）将转变为“00”，表示此对象处于轻量级锁定状态。
     * 如果这个更新操作失败了，那就意味着至少存在一条线程与当前线程竞争获取该对象的锁。
     *      虚拟机首先会检查对象的Mark Word是否指向当前线程的栈帧，
     *      如果是，说明当前线程已经拥有了这个对象的锁，那直接进入同步块继续执行就可以了，否则就说明这个锁对象已经被其他线程抢占了。
     *      如果出现两条以上的线程争用同一个锁的情况，那轻量级锁就不再有效，必须要膨胀为重量级锁，锁标志的状态值变为“10”，
     *      此时Mark Word中存储的就是指向重量级锁（互斥量）的指针，后面等待锁的线程也必须进入阻塞状态。
     *
     * 解锁过程也同样是通过CAS操作来进行的:
     * 持有锁的线程尝试释放锁，如果锁对象的Mark Word仍然指向线程的锁记录，那就用CAS操作把对象当前的Mark Word和线程栈帧中的DisplacedMark Word替换回来。
     * 假如能够成功替换，那整个同步过程就顺利完成了；
     * 如果替换失败，则说明有其他线程尝试过获取该锁，就要在释放锁的同时，唤醒被挂起的线程。
     *
     * 轻量级锁能提升程序同步性能的依据是“对于绝大部分的锁，在整个同步周期内都是不存在竞争的”这一经验法则。
     * 如果没有竞争，轻量级锁便通过CAS操作成功避免了使用互斥量的开销；但如果确实存在锁竞争，除了互斥量的本身开销外，还额外发生了CAS操作的开销。
     * 因此在有竞争的情况下，轻量级锁反而会比传统的重量级锁更慢。
     */
    public static void thinLockState() {
        Object obj = new Object();
        synchronized (obj) {
            // 对象头MarkWord最后3位000，偏向状态0，锁状态00表示轻量级锁
            // 前62位是线程栈中锁记录的指针
            // 栈帧（DisplacedMarkWord）
            // 若一个线程获取锁时发现是轻量级锁，会将锁对象的MarkWord复制都自己的DisplacedMarkWord，再通过CAS将锁对象的MarkWord替换为此条锁记录的指针。
            // 如果成功，当前线程获取到锁；如果失败，表示锁对象的MarkWord指向其他线程的栈帧锁记录，说明存在竞争，当前线程自旋（自适应）。
            // 上次自旋成功则放松下次自旋限制或增大允许自旋次数；上次自旋失败则缩减下次自旋次数甚至不自旋
            //   0   8        (object header: mark)     0x0000700002c0a940 (thin lock: 0x0000700002c0a940)
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
        // 因为锁没有降级过程，所以这里即便等待4s后再获取，obj仍然是轻量级锁状态
        try {
            TimeUnit.MILLISECONDS.sleep(4000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }

    /**
     * synchronized重量级锁，是基于进入和退出Monitor（管程）对象实现。
     * 对于同步代码块，synchronized(lock) {} 编译后会在 代码块开始位置插入 monitorEnter指令，在结束位置插入 monitorExit指令
     * 对于同步方法，synchronized method() {}，编译后会在方法标识flag部分添加ACC_SYNCHRONIZED标志
     * 线程执行到monitorEnter，尝试获取锁对象对应的Monitor对象的所有权，若获取成功，即获取到锁，会在Monitor对象的owner字段保存当前线程ID，标识处于锁定状态，
     * 除非退出同步代码块，执行monitorExit，否则其他线程无法获取都这个Monitor。
     * 操作系统的 mutex
     * MarkWord前62位指向堆中Monitor对象
     * <p>
     * 这个Monitor对应的是c/c++实现的 ObjectMonitor 对象
     * ObjectMonitor() {
     * _header       = NULL;
     * _count        = 0; //记录个数
     * _waiters      = 0,
     * _recursions   = 0;
     * _object       = NULL;
     * _owner        = NULL;
     * _WaitSet      = NULL; //处于wait状态的线程，会被加入到_WaitSet
     * _WaitSetLock  = 0 ;
     * _Responsible  = NULL ;
     * _succ         = NULL ;
     * _cxq          = NULL ;
     * FreeNext      = NULL ;
     * _EntryList    = NULL ; //处于等待锁block状态的线程，会被加入到该列表
     * _SpinFreq     = 0 ;
     * _SpinClock    = 0 ;
     * OwnerIsThread = 0 ;
     * }
     */
    public static void fatLockState() {

    }

    /**
     * 锁升级的过程中hashCode()去哪了
     * 锁升级为轻量级后,Mark Word中保存的分别是线程栈帧里的锁记录指针，指向Lock Record记录
     * 锁升级为重量级锁后，Mark Word中保存的分别是线程栈帧里的重量级锁指针，指向Object Monitor类
     * 已经没有位置再保存哈希码, GC年龄了,那么这些信息被移动到哪里去了呢?
     * <p>
     * 用书中的一段话来描述 锁和hashcode 之间的关系 ：在Java语言里面一个对象如果计算过哈希码,就应该一直保持该值不变(强烈推荐但不强制,因为用户可以重载hashCode()方法按自己的意愿返回哈希码),
     * 否则很多依赖对象哈希码的API都可能存在出错风险。
     * 作为绝大多数对象哈希码来源的Object::hashCode()方法,返回的是对象的一致性哈希码(Identity Hash Code),这个值是能强制保证不变的,
     * 它通过在对象头中存储计算结果来保证第一次计算之后,再次调用该方法取到的哈希码值永远不会再发生改变。
     * <p>
     * 总结：
     * <p>
     * 当一个对象已经计算过一致性哈希码后,它就再也无法进入偏向锁状态了，直接升级为轻量级锁;
     * 而当一个对象当前正处于偏向锁状态,又收到需要 计算其一致性哈希码请求【1】时,它的偏向状态会被立即撤销,并且锁会膨胀为重量级锁。
     * [1] 注意，这里说的计算请求应来自于对Object::hashCode()或者System::identityHashCode(Object)方法的调用，
     * 如果重写了对象的hashCode()方法，计算哈希码时并不会产生这里所说的请求。
     * <p>
     * 在无锁状态下，Mark Word中可以存储对象的identity hash code值。
     * 当对象的hashCode()方法第一次 被调用时，JVM会生成对应的identity hash code值并将该值存储到Mark Word中。
     * <p>
     * 对于偏向锁，在线程获取偏向锁时，会用Thread ID和epoch值覆盖identity hash code所在的位置。如果一个对象的hashCode()方法
     * 已经被调用过一次之后，这个对象不能被设置偏向锁。因为如果可以的话，那Mark Word中的identity hash code必然会被偏向线程
     * id给覆盖，这就会造成同一个对象前后两次调用hashCode()方法得到的结果不一致。
     * <p>
     * 升级为轻量级锁时，JVM会在当前线程的栈帧中创建一个锁记录(LockRecord)空间，用于存储锁对象的MarkWord拷贝，该拷贝中可
     * 以包含identity hash code,所以轻量级锁可以和identity hash code共存，哈希码和GC年龄自然保存在此，释放锁后会将这些信息写
     * 回到对象头。
     * <p>
     * 升级为重量级锁后，MarkWord保存的重量级锁ObjectMonitor对象指针，代表重量级锁的ObjectMonitor类里有字段可以记录非加锁 状态(标志为“01”)下的Mark Word,其中自然可以存储原来的哈希码。
     * 锁释放后也会将信息写回到对象头。
     */
    public static void whereIsHashcode() {

        Object o = new Object();
        System.out.println("启动时新建对象偏向状态是0，锁状态是01");
        // (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // 睡眠4秒，保证开启偏向锁
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        o = new Object();
        System.out.println("4s后新建对象偏向状态是1，锁状态是01，此时是偏向锁");
        // (object header: mark)     0x0000000000000005 (non-biasable; age: 0)
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        System.out.println("偏向锁状态下，获取锁对象前调用hashcode方法：" + o.hashCode());

        synchronized (o) {
            System.out.println("本来是偏向锁，但是由于计算过一致性哈希，获取锁对象时发现已升级为轻量级锁，偏向状态0，锁状态00");
            // (object header: mark)     0x00007000075de940 (【thin lock】: 0x00007000075de940)
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

        o = new Object();
        System.out.println("新对象o");
        // (object header: mark)     0x0000000000000005 (biasable; age: 0)
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            System.out.println("偏向锁状态时，获取锁对象后，调用hashcode方法，导致直接升级为重量级锁");
            o.hashCode();
            // (object header: mark)     0x00007f839e81813a (【fat lock】: 0x00007f839e81813a)
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    public static void main(String[] args) {
        // noLockState();
        // thinLockState();
        whereIsHashcode();
    }
}
