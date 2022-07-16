package com.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-06
 * <p>
 * 具有关注关系的账号构造（单向）
 */
public class AccountFanRelationBuildV2 {

    // 总共500w
    private static final long account_id_start = 1L;
    private static final long account_id_end = 5000000L;
    private static final long total_accounts = 5000000L;

    private static final Random random = new Random();

    private static final String[] intervalKeys = {"fans_0_0", "fans_1_2", "fans_3_10", "fans_11_20",
            "fans_21_50", "fans_51_100", "fans_101_200", "fans_201_1000", "fans_1001_4999",
            "fans_5000_5000"};

    // 每种账号数目占比
    private static final double[] percentage = {0.2, 0.2, 0.2, 0.1, 0.1, 0.05, 0.05, 0.05, 0.04, 0.01};

    private static final Map<String, Integer> percentIndexMap = new HashMap<>() {
        {
            put("fans_0_0", 0);
            put("fans_1_2", 1);
            put("fans_3_10", 2);
            put("fans_11_20", 3);
            put("fans_21_50", 4);
            put("fans_51_100", 5);
            put("fans_101_200", 6);
            put("fans_201_1000", 7);
            put("fans_1001_4999", 8);
            put("fans_5000_5000", 9);
        }
    };

    // 每种账号，将范围粉丝数数离散化时的步长
    private static final Map<String, Integer> fansAddStepMap = new HashMap<>() {
        {
            put("fans_0_0", 0);
            put("fans_1_2", 1);
            put("fans_3_10", 2);
            put("fans_11_20", 3);
            put("fans_21_50", 5);
            put("fans_51_100", 8);
            put("fans_101_200", 16);
            put("fans_201_1000", 120);
            put("fans_1001_4999", 500);
            put("fans_5000_5000", 0);
        }
    };

    private static final Map<String, long[]> idIntervalByFansCountMap = new HashMap<>();

    static {
        // 初始分配：根据账号占比分配区间范围
        long curIdStart = account_id_start, curIdEnd, curCount;
        for (int i = 0; i < intervalKeys.length; i++) {
            String intervalKey = intervalKeys[i];
            curCount = (long) (total_accounts * percentage[percentIndexMap.get(intervalKey)]);
            curIdEnd = curIdStart + curCount - 1;
            idIntervalByFansCountMap.put(intervalKey, new long[] {curIdStart, curIdEnd});
            curIdStart = curIdEnd + 1;
        }
    }

    // 粉丝数为k的账号对应区间为v
    // 每个id的粉丝就是id+1，。。。id+friends
    public static final Map<Integer, long[]> fansCount2Interval = new HashMap<>();


    /**
     * 生成 min_fans - max_fans 个 粉丝 的账号区间
     */
    public static void divideIdIntervalByFansCount(String intervalKey) {
        // 从 intervalKey 中解析出当前区间 代表的 粉丝数范围
        String[] info = intervalKey.split("_");
        if (info.length < 3) {
            throw new RuntimeException("invalid interval key: " + intervalKey);
        }
        // 最少粉丝数，最大粉丝数
        int minFans = Integer.parseInt(info[1]), maxFans = Integer.parseInt(info[2]);
        // 拿到初始分好的大区间
        long[] originInterval = idIntervalByFansCountMap.get(intervalKey);
        // 区间起始id和结束id
        long minId = originInterval[0], maxId = originInterval[1];
        // 将粉丝数区间离散成多个取值，比如[3-10]，变成3、5、7、9四种取值，粉丝数目划分增量
        // 离散化时，每一步增加几个粉丝，针对粉丝数分布区间大小不同，初始时设置了不同的步长，不可能每次增加只1个粉丝
        int[] fansOfPart = discritization(minFans, maxFans, fansAddStepMap.get(intervalKey));
        // 分成了几种离散值
        int parts = fansOfPart.length;
        // 将原来的id大区间划分为对应可能数目的小区间，让不同小区间内的账号有不同数目的粉丝
        long countOfIdsPerPart = (long) (total_accounts * percentage[percentIndexMap.get(intervalKey)]) / parts;
        // 为了让原区间内账号的粉丝数更符合分布，让每个子区间去选择随机的一个粉丝数，而不是连续，
        // 比如避免 区间 1-10 有1个粉丝，10-50 有 3 个粉丝这种，随机一下，比如 1-10 有 3 个粉丝，11-50 有 1 个粉丝
        // 离散化后的粉丝数是递增的，我们给它打乱
        shuffle(fansOfPart);
        // 为每个子区间分配粉丝
        long startId = minId, endId = 0L;
        // 每个子区间部分，选择一种 粉丝数x
        // 假设这个区间为[1-100]，每个人要有8个粉丝，那么只能保证[1-92]每个人8个粉丝，比如 1 的粉丝是 2-9，92 的粉丝是 93-100
        // 93的粉丝只能从93往后开始，会出现101超出区间，因此我们选择 后一个区间前移，也就是 让 93 成为拥有 9 个粉丝的下一个区间的开始
        // 也就是让下一个子区间顺序前移，这样最后，这些子区间仍然连续，并且最后一个子区间的末尾一定比初始时的区间末尾值更小。我们可以丢弃剩余部分，这些人没有粉丝，他们是前面那个人的粉丝
        for (int i = 0; i < parts; i++) {
            // fansOfPart[i] 代表当前子区间账号要具有的粉丝数目
            // 比如 [1-100] 只能 保证 [1-92]每个人有8个粉丝
            endId = startId + countOfIdsPerPart - 1;
            endId -= fansOfPart[i];
            // 记录满足这种粉丝数的账号所处的子区间
            fansCount2Interval.put(fansOfPart[i], new long[] {startId, endId});
            // 下一个区间前移，这里调整 end 的话，下一个子区间的start会顺序前移，相互之前仍然连续
            // 下一个子区间会顺序前移
            startId = endId + 1;
        }
        // 最后，由于丢弃了一部分，所以最后一个子区间的结尾 endId 一定 比开始时的 interval[1] 小，我们丢弃那部分区间
        // 注意这里的丢弃只是用户选择时看不到了，比如 [1-100] 变成了 [1-92]，不代表最后8个id不存在了，最后8个id是92号的粉丝，只是他们没有自己的粉丝，所以不给用户选择
        originInterval[1] = endId;
    }

    /**
     * 根据步长，将指定区间离散化
     * @param min
     * @param max
     * @param step
     * @return
     */
    private static int[] discritization(int min, int max, int step) {
        System.out.println("before discritization, min: " + min + ", max: " + max + ", step: " + step);
        int parts = step == 0 ? 1 : (max - min + 1) / step;
        int[] nums = new int[parts];
        for (int i = 0; i < parts; i++) {
            nums[i] = min + i * step;
        }
        System.out.println("after discritization: " + Arrays.toString(nums));
        return nums;
    }


    /**
     * 得到某个用户的所有粉丝，根据 id 和 他的 粉丝数目
     * 从他的id开始往后fansCount个id都是他的粉丝
     */
    private static Set<Long> getFansById(int fansCount, long id) {
        Set<Long> set = new HashSet<>();
        // 从它往后fans个人都是它的粉丝
        for (long i = 1; i <= fansCount; i++) {
            set.add(id + i);
        }
        return set;
    }


    /**
     * 数组洗牌：数组元素打乱
     */
    private static void shuffle(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // 从 i 到最后随机选一个元素
            int r = i + random.nextInt(n - i);
            swap(nums, i, r);
        }
    }

    private static void swap(int[] nums, int i, int j) {
        if (i == j) {
            return;
        }
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }

    public static void generate() {
        System.out.println("-----------------------------init divide interval--------------------------------");

        idIntervalByFansCountMap.entrySet().stream().sorted(Comparator.comparingLong(e -> e.getValue()[1])).forEach(
                entry -> System.out.println(
                        "intervalKey: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));

        // 根据每个区间要生成的账号的好友数目范围调整区间（只可能右边界缩小），保证能够实现每个人的粉丝数在要求范围内
        Arrays.stream(intervalKeys).forEach(AccountFanRelationBuildV2::divideIdIntervalByFansCount);

        System.out.println("-----------------------------adjust interval by fans count--------------------------------");

        idIntervalByFansCountMap.entrySet().stream().sorted(Comparator.comparingLong(e -> e.getValue()[1])).forEach(
                entry -> System.out.println(
                        "intervalKey: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));

        System.out.println("-----------------------------all intervals for every possible count of fans--------------------------------");
        fansCount2Interval.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(e -> System.out.println("fans count: " + e.getKey() + ", interval: " + Arrays.toString(e.getValue())));
    }


    public static void main(String[] args) {
        // generate();
        // ConcurrentHashMap；
        // FutureTask；

        // AtomicStampedReference stampedReference = new AtomicStampedReference<>(100, 1);
        // stampedReference.compareAndSet(100, 101, 1, 2);
        // System.out.println(stampedReference.getStamp());
        // stampedReference.compareAndSet(101, 100, 2, 1);
        // System.out.println(stampedReference.getStamp());
        // System.out.println(getFansById(3, 2500007L));

        System.out.println(Math.ceil(10.1));
        System.out.println(Math.floor(10.1));
    }

}
