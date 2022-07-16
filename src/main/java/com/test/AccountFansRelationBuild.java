package com.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-06
 * 
 * 具有关注关系的账号构造（单向）
 */
public class AccountFansRelationBuild {

    // 总共500w
    private static final long account_id_start = 1L;
    private static final long account_id_end = 5000000L;
    private static final long total_accounts = 5000000L;

    private static final Random random = new Random();

    private static final double percentage[] = {0.2, 0.2, 0.2, 0.1, 0.1, 0.05, 0.05, 0.05, 0.04, 0.01};

    private static final Map<String, Integer> percentIndexMap = new HashMap<>(){
        {
            put("fans_0", 0);
            put("fans_1_2", 1);
            put("fans_3_10", 2);
            put("fans_11_20", 3);
            put("fans_21_50", 4);
            put("fans_51_100", 5);
            put("fans_101_200", 6);
            put("fans_201_1000", 7);
            put("fans_1001_4999", 8);
            put("fans_5000", 9);
        }
    };

    private static final Map<String, Integer> fansAddMap = new HashMap<>(){
        {
            put("fans_0", 0);
            put("fans_1_2", 1);
            put("fans_3_10", 2);
            put("fans_11_20", 3);
            put("fans_21_50", 5);
            put("fans_51_100", 8);
            put("fans_101_200", 16);
            put("fans_201_1000", 120);
            put("fans_1001_4999", 500);
            put("fans_5000", 0);
        }
    };

    private static final long id_fans_0_start;
    private static final long id_fans_0_end;
    private static final long id_fans_1_2_start;
    private static final long id_fans_1_2_end;
    private static final long id_fans_3_10_start;
    private static final long id_fans_3_10_end;
    private static final long id_fans_11_20_start;
    private static final long id_fans_11_20_end;
    private static final long id_fans_21_50_start;
    private static final long id_fans_21_50_end;
    private static final long id_fans_51_100_start;
    private static final long id_fans_51_100_end;
    private static final long id_fans_101_200_start;
    private static final long id_fans_101_200_end;
    private static final long id_fans_201_1000_start;
    private static final long id_fans_201_1000_end;
    private static final long id_fans_1001_4999_start;
    private static final long id_fans_1001_4999_end;
    private static final long id_fans_5000_start;
    private static final long id_fans_5000_end;

    static {
        id_fans_0_start = account_id_start;
        id_fans_0_end = id_fans_0_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_0")]) - 1;
        id_fans_1_2_start = id_fans_0_end + 1;
        id_fans_1_2_end = id_fans_1_2_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_1_2")]) - 1;
        id_fans_3_10_start = id_fans_1_2_end + 1;
        id_fans_3_10_end = id_fans_3_10_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_3_10")]) - 1;
        id_fans_11_20_start = id_fans_3_10_end + 1;
        id_fans_11_20_end = id_fans_11_20_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_11_20")]) - 1;
        id_fans_21_50_start = id_fans_11_20_end + 1;
        id_fans_21_50_end = id_fans_21_50_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_21_50")]) - 1;
        id_fans_51_100_start = id_fans_21_50_end + 1;
        id_fans_51_100_end = id_fans_51_100_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_51_100")]) - 1;
        id_fans_101_200_start = id_fans_51_100_end + 1;
        id_fans_101_200_end = id_fans_101_200_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_101_200")]) - 1;
        id_fans_201_1000_start = id_fans_101_200_end + 1;
        id_fans_201_1000_end = id_fans_201_1000_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_201_1000")]) - 1;
        id_fans_1001_4999_start = id_fans_201_1000_end + 1;
        id_fans_1001_4999_end = id_fans_1001_4999_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_1001_4999")]) - 1;
        id_fans_5000_start = id_fans_1001_4999_end + 1;
        id_fans_5000_end = id_fans_5000_start + (long)(total_accounts * percentage[percentIndexMap.get("fans_5000")]) - 1;
    }

    // 粉丝数为k的账号对应区间为v
    // 每个id的粉丝就是id+1，。。。id+friends
    public static final Map<Integer, long[]> fansCount2Interval = new HashMap<>();


    /**
     * 生成 minfans_maxfans 个 粉丝 的账号区间
     */
    public static void divideIdIntervalByfansCount(String intervalKey, long minId, long maxId, int minfans, int maxfans) {
        System.out.println("-------------------generate ids that have " + minfans + "_" + maxfans + " fans:");
        // 粉丝数目划分增量
        int fansDiff = fansAddMap.get(intervalKey);
        // 一共有parts次增量，那么 粉丝数目 就有 parts 个可能取值，对应的 全部区间也应该 分开为 parts 份
        int parts = fansDiff == 0 ? 1 : (maxfans - minfans + 1) / fansDiff;
        int max = minfans + (parts - 1) * fansDiff;
        System.out.println("minfans: " + minfans + ", maxfans: " + max + ", fans add per part: " + fansDiff + ", parts: " + parts);
        // 每种粉丝数目下，需要生成 ？ 个账号
        long countPerPart = (long)(total_accounts * percentage[percentIndexMap.get(intervalKey)]) / parts;
        System.out.println("count of ids per part: "+ countPerPart);
        long startTime = System.currentTimeMillis();
        // 为了让全部账号区间随机某部分去对应粉丝数，而不是连续，比如 1-10 有1个粉丝，10-50有2个粉丝这种，随机一下，比如 1-10有3个粉丝，11-50 有2个粉丝
        int[] partIndex = new int[parts];
        for (int i = 0; i < parts; i++) {
            partIndex[i] = i;
        }
        shuffle(partIndex);
        // 统计 为 每种粉丝数 生成了多少 id，最后全部id数应该等于 maxId - minId + 1
        long total = 0;
        // 粉丝数取值分别为 minfans + ？ * diff
        for (int i = 0; i < parts; i++) {
            int fansNum = minfans + i * fansDiff;
            long startId = minId + partIndex[i] * countPerPart;
            long endId = startId + countPerPart - 1;
            if (partIndex[i] == parts - 1 && endId < maxId) {
                endId = maxId;
            }
            fansCount2Interval.put(fansNum, new long[]{startId, endId});
            total += endId - startId + 1;
        }
        System.out.println("count of ids that have " + minfans + "_" + maxfans + " fans: " + total);
        System.out.println("--------------time cost: " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 分配粉丝
     * 将大区间，划分为 能够恰好凑出指定数目好友关系的小区间
     * @param fansCount
     */
    private static void generateRelationForIdIntervalByFansCount(int fansCount) {
        if (fansCount == 0) {
            return;
        }
        // 初始分配的粉丝数为friendCount的id区间
        System.out.println("before generate fans, id interval: " + Arrays.toString(fansCount2Interval.get(fansCount)));

        // 实际分配粉丝
        // 比如这个区间是1-10，每个人需要3个粉丝
        // 那么1需要234，2需要345，。。。，7需要8910，所以 最后只能保证 1-7每个人有3个粉丝，但是由于 8910原来就预留在此区间内，可能保证这几个人一定不会被别的区间用到
        long[] interval = fansCount2Interval.get(fansCount);
        // 所以这里把1-10改为1-7没有问题
        interval[1] -= fansCount;

        // 分配完后，最后几个人只能给前面的人当粉丝。。。。
        System.out.println("after generate fans, id interval: " + Arrays.toString(fansCount2Interval.get(fansCount)));
    }

    /**
     * 得到某个用户的所有粉丝，根据 id 和 他的 粉丝数目
     * 根据粉丝数得到满足有这么多朋友的全部id区间
     * 再在这个区间对应的全部小区间内找到这个人的全部粉丝，及它的id+1。。。到id+fansCount
     * @param id
     * @return
     */
    private static Set<Long> getfansById(long id, int fansCount) {
        long[] interval = fansCount2Interval.get(fansCount);
        // 此id必然属于这个区间
        assert id >= interval[0] && id <= interval[1];
        // System.out.println(Arrays.toString(floor));
        Set<Long> set = new HashSet<>();
        // 最后 fans 个人，只是前面人的粉丝
        if (id > interval[1] - fansCount) {
            return set;
        }
        // 从它往后fans个人都是它的粉丝
        for (long i = 1; i <= fansCount; i++) {
            set.add(id + i);
        }
        return set;
    }


    /**
     * 数组洗牌：数组元素打乱
     * @param nums
     */
    private static void shuffle(int[] nums) {
        int n = nums.length;
        for (int i = 0 ; i < n; i++) {
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


    public static void main(String[] args) {
        // System.out.println(fans_0_end);
        // System.out.println(fans_1_2_end);
        // System.out.println(fans_3_10_end);
        // System.out.println(fans_11_20_end);
        // System.out.println(fans_21_50_end);
        // System.out.println(fans_51_100_end);
        // System.out.println(fans_101_200_end);
        // System.out.println(fans_201_1000_end);
        // System.out.println(fans_1001_4999_end);
        // System.out.println(fans_5000_end);

        divideIdIntervalByfansCount("fans_0", id_fans_0_start, id_fans_0_end, 0, 0);
        divideIdIntervalByfansCount("fans_1_2", id_fans_1_2_start, id_fans_1_2_end, 1, 2);
        divideIdIntervalByfansCount("fans_3_10", id_fans_3_10_start, id_fans_3_10_end, 3, 10);
        divideIdIntervalByfansCount("fans_11_20", id_fans_11_20_start, id_fans_11_20_end, 11, 20);
        divideIdIntervalByfansCount("fans_21_50", id_fans_21_50_start, id_fans_21_50_end, 21, 50);
        divideIdIntervalByfansCount("fans_51_100", id_fans_51_100_start, id_fans_51_100_end, 51, 100);
        divideIdIntervalByfansCount("fans_101_200", id_fans_101_200_start, id_fans_101_200_end, 101, 200);
        divideIdIntervalByfansCount("fans_201_1000", id_fans_201_1000_start, id_fans_201_1000_end, 201, 1000);
        divideIdIntervalByfansCount("fans_1001_4999", id_fans_1001_4999_start, id_fans_1001_4999_end, 1001, 4999);
        divideIdIntervalByfansCount("fans_5000", id_fans_5000_start, id_fans_5000_end, 5000, 5000);

        fansCount2Interval.entrySet().stream().sorted(Comparator.comparingInt(Entry::getKey)).forEach(entry -> System.out.println("fans count: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));

        System.out.println("*".repeat(100));

        generateRelationForIdIntervalByFansCount(3);
        // before generate fans, id interval: [2000001, 2250000]
        // after generate fans, id interval: [2000001, 2249997]
        // generateRelationForIdIntervalByFriendCount(117);
        // 因为区间划分会随机划分，所以每次运行结果会不一样
        System.out.println(getfansById(2750017, 3));
    }

}
