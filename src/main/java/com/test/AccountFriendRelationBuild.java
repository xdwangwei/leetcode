package com.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-06
 *
 * 具有好友关系的账号构造（双向）
 */
public class AccountFriendRelationBuild {

    // 总共500w
    private static final long account_id_start = 1L;
    private static final long account_id_end = 5000000L;
    private static final long total_accounts = 5000000L;

    private static final Random random = new Random();

    private static final double percentage[] = {0.2, 0.2, 0.2, 0.1, 0.1, 0.05, 0.05, 0.05, 0.04, 0.01};

    private static final Map<String, Integer> percentIndexMap = new HashMap<>(){
        {
            put("friends_0", 0);
            put("friends_1_2", 1);
            put("friends_3_10", 2);
            put("friends_11_20", 3);
            put("friends_21_50", 4);
            put("friends_51_100", 5);
            put("friends_101_200", 6);
            put("friends_201_1000", 7);
            put("friends_1001_4999", 8);
            put("friends_5000", 9);
        }
    };

    private static final Map<String, Integer> friendsAddMap = new HashMap<>(){
        {
            put("friends_0", 0);
            put("friends_1_2", 1);
            put("friends_3_10", 2);
            put("friends_11_20", 3);
            put("friends_21_50", 5);
            put("friends_51_100", 8);
            put("friends_101_200", 16);
            put("friends_201_1000", 120);
            put("friends_1001_4999", 500);
            put("friends_5000", 0);
        }
    };

    private static final long id_friends_0_start;
    private static final long id_friends_0_end;
    private static final long id_friends_1_2_start;
    private static final long id_friends_1_2_end;
    private static final long id_friends_3_10_start;
    private static final long id_friends_3_10_end;
    private static final long id_friends_11_20_start;
    private static final long id_friends_11_20_end;
    private static final long id_friends_21_50_start;
    private static final long id_friends_21_50_end;
    private static final long id_friends_51_100_start;
    private static final long id_friends_51_100_end;
    private static final long id_friends_101_200_start;
    private static final long id_friends_101_200_end;
    private static final long id_friends_201_1000_start;
    private static final long id_friends_201_1000_end;
    private static final long id_friends_1001_4999_start;
    private static final long id_friends_1001_4999_end;
    private static final long id_friends_5000_start;
    private static final long id_friends_5000_end;

    static {
        id_friends_0_start = account_id_start;
        id_friends_0_end = id_friends_0_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_0")]) - 1;
        id_friends_1_2_start = id_friends_0_end + 1;
        id_friends_1_2_end = id_friends_1_2_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_1_2")]) - 1;
        id_friends_3_10_start = id_friends_1_2_end + 1;
        id_friends_3_10_end = id_friends_3_10_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_3_10")]) - 1;
        id_friends_11_20_start = id_friends_3_10_end + 1;
        id_friends_11_20_end = id_friends_11_20_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_11_20")]) - 1;
        id_friends_21_50_start = id_friends_11_20_end + 1;
        id_friends_21_50_end = id_friends_21_50_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_21_50")]) - 1;
        id_friends_51_100_start = id_friends_21_50_end + 1;
        id_friends_51_100_end = id_friends_51_100_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_51_100")]) - 1;
        id_friends_101_200_start = id_friends_51_100_end + 1;
        id_friends_101_200_end = id_friends_101_200_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_101_200")]) - 1;
        id_friends_201_1000_start = id_friends_101_200_end + 1;
        id_friends_201_1000_end = id_friends_201_1000_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_201_1000")]) - 1;
        id_friends_1001_4999_start = id_friends_201_1000_end + 1;
        id_friends_1001_4999_end = id_friends_1001_4999_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_1001_4999")]) - 1;
        id_friends_5000_start = id_friends_1001_4999_end + 1;
        id_friends_5000_end = id_friends_5000_start + (long)(total_accounts * percentage[percentIndexMap.get("friends_5000")]) - 1;
    }

    // 朋友数为k的账号对应区间为v
    public static final Map<Integer, long[]> friendsCount2Interval = new HashMap<>();

    // k 朋友数，v 此朋友数对应的 多个 账号区间，每个区间大小是 朋友数 + 1，比如 3 个朋友的账号对应区间有 1-4，5-8，这些区间内互相形成朋友关系
    public static final Map<Integer, TreeSet<long[]>> friendsCount2Relation = new HashMap<>();


    /**
     * 生成 minFriends_maxFriends 个 好友 的账号区间
     */
    public static void divideIdIntervalByFriendsCount(String intervalKey, long minId, long maxId, int minFriends, int maxFriends) {
        System.out.println("-------------------generate ids that have " + minFriends + "_" + maxFriends + " friends:");
        // 朋友数目划分增量
        int friendsDiff = friendsAddMap.get(intervalKey);
        // 一共有parts次增量，那么 朋友数目 就有 parts 个可能取值，对应的 全部区间也应该 分开为 parts 份
        int parts = friendsDiff == 0 ? 1 : (maxFriends - minFriends + 1) / friendsDiff;
        int max = minFriends + (parts - 1) * friendsDiff;
        System.out.println("minFriends: " + minFriends + ", maxFriends: " + max + ", friends add per part: " + friendsDiff + ", parts: " + parts);
        // 每种朋友数目下，需要生成 ？ 个账号
        long countPerPart = (long)(total_accounts * percentage[percentIndexMap.get(intervalKey)]) / parts;
        System.out.println("count of ids per part: "+ countPerPart);
        long startTime = System.currentTimeMillis();
        // 为了让全部账号区间随机某部分去对应朋友数，而不是连续，比如 1-10 有1个朋友，10-50有2个朋友这种，随机一下，比如 1-10有3个朋友，11-50 有2个朋友
        int[] partIndex = new int[parts];
        for (int i = 0; i < parts; i++) {
            partIndex[i] = i;
        }
        shuffle(partIndex);
        // 统计 为 每种朋友数 生成了多少 id，最后全部id数应该等于 maxId - minId + 1
        long total = 0;
        // 朋友数取值分别为 minFriends + ？ * diff
        for (int i = 0; i < parts; i++) {
            int friendsNum = minFriends + i * friendsDiff;
            long startId = minId + partIndex[i] * countPerPart;
            long endId = startId + countPerPart - 1;
            if (partIndex[i] == parts - 1 && endId < maxId) {
                endId = maxId;
            }
            friendsCount2Interval.put(friendsNum, new long[]{startId, endId});
            total += endId - startId + 1;
        }
        System.out.println("count of ids that have " + minFriends + "_" + maxFriends + " friends: " + total);
        System.out.println("--------------time cost: " + (System.currentTimeMillis() - startTime));
    }


    public static void generateFriends(int friendCount) {
        if (friendCount == 0) {
            return;
        }
        long[] interval = friendsCount2Interval.get(friendCount);
        long startId = interval[0];
        long endId = interval[1];
        long parts = (endId - startId + 1) / (friendCount + 1);
        friendsCount2Relation.put(friendCount, new TreeSet<>(Comparator.comparingLong(o -> o[0])));
        TreeSet<long[]> relations = friendsCount2Relation.get(friendCount);
        for (int i = 0; i < parts; i++) {
            long curStart = startId + i * (friendCount + 1);
            long curEnd = curStart + friendCount;
            relations.add(new long[]{curStart, curEnd});
            if (i == parts - 1 && curEnd < endId) {
                // 最后几个id不够friend+1个，没办法相互之间凑出friend
                interval[1] = curEnd;
            }
        }
    }


    /**
     * 将大区间，划分为 能够恰好凑出指定数目好友关系的小区间
     * @param friendCount
     */
    private static void generateRelationForIdIntervalByFriendCount(int friendCount) {
        // 朋友数为friendCount的id区间
        System.out.println("before generate friends, id interval: " + Arrays.toString(friendsCount2Interval.get(friendCount)));
        // 实际分配好友
        generateFriends(friendCount);
        // 如果不能恰好分配，会丢弃最后几个id
        System.out.println("after generate friends, id interval: " + Arrays.toString(friendsCount2Interval.get(friendCount)));
        // 打印分好的区间
        // TreeSet<long[]> relations = friendsCount2Relation.get(friendCount);
        // int i = 0;
        // for (long[] relation : relations) {
        //     System.out.println(String.format("每%d个人1组，形成好友关系，每个人%d个好友，id区间[%d, %d]：", friendCount + 1, friendCount, relation[0], relation[1]));
        //     i++;
        //     if (i == 50) {
        //         System.out.println("stop print.....");
        //         break;
        //     }
        // }
    }

    /**
     * 得到某个用户的所有朋友，根据 id 和 他的 朋友数目
     * 根据朋友数得到满足有这么多朋友的全部id区间
     * 再在这个区间对应的全部小区间内找到这个人的全部朋友
     * @param id
     * @return
     */
    private static Set<Long> getFriendsById(long id, int friendCount) {
        TreeSet<long[]> relations = friendsCount2Relation.get(friendCount);
        long[] floor = relations.floor(new long[] {id, id});
        // System.out.println(Arrays.toString(floor));
        Set<Long> set = new HashSet<>();
        assert floor != null;
        // 原来被丢弃的部分id，不存在好友
        if (id > floor[1]) {
            return set;
        }
        for (long i = floor[0]; i <= floor[1]; i++) {
            if (i != id) {
                set.add(i);
            }
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
        // System.out.println(friends_0_end);
        // System.out.println(friends_1_2_end);
        // System.out.println(friends_3_10_end);
        // System.out.println(friends_11_20_end);
        // System.out.println(friends_21_50_end);
        // System.out.println(friends_51_100_end);
        // System.out.println(friends_101_200_end);
        // System.out.println(friends_201_1000_end);
        // System.out.println(friends_1001_4999_end);
        // System.out.println(friends_5000_end);

        divideIdIntervalByFriendsCount("friends_0", id_friends_0_start, id_friends_0_end, 0, 0);
        divideIdIntervalByFriendsCount("friends_1_2", id_friends_1_2_start, id_friends_1_2_end, 1, 2);
        divideIdIntervalByFriendsCount("friends_3_10", id_friends_3_10_start, id_friends_3_10_end, 3, 10);
        divideIdIntervalByFriendsCount("friends_11_20", id_friends_11_20_start, id_friends_11_20_end, 11, 20);
        divideIdIntervalByFriendsCount("friends_21_50", id_friends_21_50_start, id_friends_21_50_end, 21, 50);
        divideIdIntervalByFriendsCount("friends_51_100", id_friends_51_100_start, id_friends_51_100_end, 51, 100);
        divideIdIntervalByFriendsCount("friends_101_200", id_friends_101_200_start, id_friends_101_200_end, 101, 200);
        divideIdIntervalByFriendsCount("friends_201_1000", id_friends_201_1000_start, id_friends_201_1000_end, 201, 1000);
        divideIdIntervalByFriendsCount("friends_1001_4999", id_friends_1001_4999_start, id_friends_1001_4999_end, 1001, 4999);
        divideIdIntervalByFriendsCount("friends_5000", id_friends_5000_start, id_friends_5000_end, 5000, 5000);

        friendsCount2Interval.entrySet().stream().sorted(Comparator.comparingInt(Entry::getKey)).forEach(entry -> System.out.println("friends count: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));

        System.out.println("*".repeat(100));

        generateRelationForIdIntervalByFriendCount(3);
        generateRelationForIdIntervalByFriendCount(5);
        generateRelationForIdIntervalByFriendCount(7);
        generateRelationForIdIntervalByFriendCount(9);
        // generateRelationForIdIntervalByFriendCount(117);
        // 因为区间划分会随机划分，所以每次运行结果会不一样
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750001, 2750004]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750005, 2750008]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750009, 2750012]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750013, 2750016]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750017, 2750020]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750021, 2750024]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750025, 2750028]：
        // 每4个人1组，形成好友关系，每个人3个好友，id区间[2750029, 2750032]：
        // System.out.println(getFriendsById(2750017, 3));
    }


}
