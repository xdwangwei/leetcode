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
 */
public class AccountRelationBuild {

    // 总共500w
    private static final long start = 1L;
    private static final long end = 5000000L;
    private static final long total = 5000000L;

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

    private static final long friends_0_start;
    private static final long friends_0_end;
    private static final long friends_1_2_start;
    private static final long friends_1_2_end;
    private static final long friends_3_10_start;
    private static final long friends_3_10_end;
    private static final long friends_11_20_start;
    private static final long friends_11_20_end;
    private static final long friends_21_50_start;
    private static final long friends_21_50_end;
    private static final long friends_51_100_start;
    private static final long friends_51_100_end;
    private static final long friends_101_200_start;
    private static final long friends_101_200_end;
    private static final long friends_201_1000_start;
    private static final long friends_201_1000_end;
    private static final long friends_1001_4999_start;
    private static final long friends_1001_4999_end;
    private static final long friends_5000_start;
    private static final long friends_5000_end;

    static {
        friends_0_start = start;
        friends_0_end = friends_0_start + (long)(total * percentage[percentIndexMap.get("friends_0")]) - 1;
        friends_1_2_start = friends_0_end + 1;
        friends_1_2_end = friends_1_2_start + (long)(total * percentage[percentIndexMap.get("friends_1_2")]) - 1;
        friends_3_10_start = friends_1_2_end + 1;
        friends_3_10_end = friends_3_10_start + (long)(total * percentage[percentIndexMap.get("friends_3_10")]) - 1;
        friends_11_20_start = friends_3_10_end + 1;
        friends_11_20_end = friends_11_20_start + (long)(total * percentage[percentIndexMap.get("friends_11_20")]) - 1;
        friends_21_50_start = friends_11_20_end + 1;
        friends_21_50_end = friends_21_50_start + (long)(total * percentage[percentIndexMap.get("friends_21_50")]) - 1;
        friends_51_100_start = friends_21_50_end + 1;
        friends_51_100_end = friends_51_100_start + (long)(total * percentage[percentIndexMap.get("friends_51_100")]) - 1;
        friends_101_200_start = friends_51_100_end + 1;
        friends_101_200_end = friends_101_200_start + (long)(total * percentage[percentIndexMap.get("friends_101_200")]) - 1;
        friends_201_1000_start = friends_101_200_end + 1;
        friends_201_1000_end = friends_201_1000_start + (long)(total * percentage[percentIndexMap.get("friends_201_1000")]) - 1;
        friends_1001_4999_start = friends_201_1000_end + 1;
        friends_1001_4999_end = friends_1001_4999_start + (long)(total * percentage[percentIndexMap.get("friends_1001_4999")]) - 1;
        friends_5000_start = friends_1001_4999_end + 1;
        friends_5000_end = friends_5000_start + (long)(total * percentage[percentIndexMap.get("friends_5000")]) - 1;
    }

    // 0个好友 -- 20% -- 100w
    private static final Set<Long> setsOfFriends_0 = new HashSet<>();
    // 1-2个好友 -- 20% -- 100w
    private static final Set<Long> setsOfFriends_1_2 = new HashSet<>();
    // 3-10个好友 -- 20% -- 100w
    private static final Set<Long> setsOfFriends_3_10 = new HashSet<>();
    // 11-20个好友 -- 10% -- 50w
    private static final Set<Long> setsOfFriends_11_20 = new HashSet<>();
    // 21-50个好友 -- 10% -- 50w
    private static final Set<Long> setsOfFriends_21_50 = new HashSet<>();
    // 51-100个好友 -- 5% -- 25w
    private static final Set<Long> setsOfFriends_51_100 = new HashSet<>();
    // 101-200个好友 -- 5% -- 25w
    private static final Set<Long> setsOfFriends_101_200 = new HashSet<>();
    // 201-1000个好友 -- 5% -- 25w
    private static final Set<Long> setsOfFriends_201_1000 = new HashSet<>();
    // 1001-4999个好友 -- % -- 20w
    private static final Set<Long> setsOfFriends_1001_4999 = new HashSet<>();
    // 5000个好友 -- % -- 5w
    private static final Set<Long> setsOfFriends_5000 = new HashSet<>();

    // 账号id及其朋友
    public static final Map<Long, Set<Long>> friends = new HashMap<>();
    // 朋友数为k的账号对应区间为v
    public static final Map<Integer, long[]> friendsCount2Interval = new HashMap<>();

    /**
     * 生成具有 minFriends_maxFriends 个 好友 的账号集合
     */
    public static void generateIdsWithFriends(Set<Long> idSet, String intervalKey, Long idStart, Long idEnd, int minFriends, int maxFriends) {
        System.out.println("-----------------generate ids that have " + minFriends + "_" + maxFriends + " friends:");
        // 朋友数目划分增量
        int friendsPerPart = friendsAddMap.get(intervalKey);
        // 朋友数目 一共可能有 总朋友数 / 增量 种
        int parts = (maxFriends - minFriends + 1) / friendsPerPart;
        // 每种朋友数目下，需要生成 ？ 个账号
        long countPerPart = (long)(total * percentage[percentIndexMap.get(intervalKey)]) / parts;
        System.out.println("count per part: " + countPerPart);
        long startTime = System.currentTimeMillis();
        // 需要 num 个朋友，num 分别取 1 和 2
        for (int num = minFriends; num <= maxFriends; num += friendsPerPart) {
            // 生成 countPerPart 个 具有 num 个朋友的账号
            for (int j = 0; j < countPerPart; j++) {
                // 生成账号
                long id = nextLong(idStart, idEnd);
                while (idSet.contains(id)) {
                    id = nextLong(idStart, idEnd);
                }
                idSet.add(id);
                // 生成 num 个朋友
                friends.putIfAbsent(id, new HashSet<>(num));
                Set<Long> friendsSet = friends.get(id);
                while (friendsSet.size() < num) {
                    long friendId = nextLong(idStart, idEnd);
                    while (friendId == id || friendsSet.contains(friendId) || friends.getOrDefault(friendId, new HashSet<>()).size() >= num || idSet.contains(friendId)) {
                        friendId = nextLong(idStart, idEnd);
                    }
                    // 加入这个朋友
                    friendsSet.add(friendId);
                    // 相互关系
                    // setsOfFriends_1_2.add(friendId);
                    friends.putIfAbsent(friendId, new HashSet<>());
                    friends.get(friendId).add(id);
                }
            }
        }
        System.out.println("--------------time cost: " + (System.currentTimeMillis() - startTime));
        for (int num = minFriends; num <= maxFriends; num++) {
            int finalNum = num;
            System.out.println("count of ids that has " + num + " friends: " + idSet.stream().filter(id -> friends.get(id).size() == finalNum).count());
        }
    }


    /**
     * 生成 minFriends_maxFriends 个 好友 的账号集合
     */
    public static void generateIdsWithFriends2(Set<Long> idSet, String intervalKey, long idStart, long idEnd, int minFriends, int maxFriends) {
        System.out.println("-----------------generate ids that have " + minFriends + "_" + maxFriends + " friends:");
        // 朋友数目划分增量
        int friendsDiff = friendsAddMap.get(intervalKey);
        System.out.println("friendsDiff：" + friendsDiff);
        // 一共有parts次增量
        int parts = (maxFriends - minFriends + 1) / friendsDiff;
        System.out.println("parts: " + parts);
        // 每种朋友数目下，需要生成 ？ 个账号
        long countPerPart = (long)(total * percentage[percentIndexMap.get(intervalKey)]) / parts;
        System.out.println("count per part: "+ countPerPart);
        long startTime = System.currentTimeMillis();
        // 需要 num 个朋友，num 分别取 1 和 2
        for (int num = minFriends; num <= maxFriends; num += friendsDiff) {
            // 生成 countPerPart 个 具有 num 个朋友的账号
            for (int j = 0; j < countPerPart; j++) {
                // 生成账号
                long id = idStart++;
                idSet.add(id);
                // 生成 num 个朋友
                friends.putIfAbsent(id, new HashSet<>(num));
                Set<Long> friendsSet = friends.get(id);
                while (friendsSet.size() < num) {
                    long friendId = nextLong(id + 1, idEnd);
                    while (friendsSet.contains(friendId) || friends.getOrDefault(friendId, new HashSet<>()).size() >= num) {
                        friendId = nextLong(id + 1, idEnd);
                    }
                    // 加入这个朋友
                    friendsSet.add(friendId);
                    // 相互关系
                    friends.putIfAbsent(friendId, new HashSet<>());
                    friends.get(friendId).add(id);
                }
            }
        }
        System.out.println("--------------time cost: " + (System.currentTimeMillis() - startTime));
        for (int num = minFriends; num <= maxFriends; num++) {
            int finalNum = num;
            System.out.println("count of ids that has " + num + " friends: " + idSet.stream().filter(id -> friends.get(id).size() == finalNum).count());
        }
    }

    /**
     * random generate id in [start, end]
     * @param start
     * @param end
     * @return
     */
    private static long nextLong(long start, long end) {
        return start + (long)(random.nextDouble() * (end - start + 1));
    }

    /**
     * 生成 minFriends_maxFriends 个 好友 的账号集合
     */
    public static void generateIdsWithFriends3(Set<Long> idSet, String intervalKey, long minId, long maxId, int minFriends, int maxFriends) {
        System.out.println("-----------------generate ids that have " + minFriends + "_" + maxFriends + " friends:");
        // 朋友数目划分增量
        int friendsDiff = friendsAddMap.get(intervalKey);
        // 一共有parts次增量，那么 朋友数目 就有 parts 个可能取值，对应的 全部区间也应该 分开为 parts 份
        int parts = friendsDiff == 0 ? 1 : (maxFriends - minFriends + 1) / friendsDiff;
        int max = minFriends + (parts - 1) * friendsDiff;
        System.out.println("minFriends: " + minFriends + ", maxFriends: " + max + ", friends add per part: " + friendsDiff + ", parts: " + parts);
        // 每种朋友数目下，需要生成 ？ 个账号
        long countPerPart = (long)(total * percentage[percentIndexMap.get(intervalKey)]) / parts;
        System.out.println("count of ids per part: "+ countPerPart);
        long startTime = System.currentTimeMillis();
        // 为了让全部账号区间随机某部分去对应朋友数，而不是连续，比如 1-10 有1个朋友，10-50有2个朋友这种，随机一下，比如 1-10有3个朋友，11-50 有2个朋友
        int[] partIndex = new int[parts];
        for (int i = 0; i < parts; i++) {
            partIndex[i] = i;
        }
        shuffle(partIndex);
        long total = 0;
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

        // generateIdsWithFriends(setsOfFriends_0, "friends_0", friends_0_start, friends_0_end, 0, 0);
        // generateIdsWithFriends(setsOfFriends_1_2, "friends_1_2", friends_1_2_start, friends_1_2_end, 1, 2);
        // generateIdsWithFriends(setsOfFriends_3_10, "friends_3_10", friends_3_10_start, friends_3_10_end, 3, 10);
        // generateIdsWithFriends(setsOfFriends_21_50, "friends_21_50", friends_21_50_start, friends_21_50_end, 21, 50);
        // generateIdsWithFriends(setsOfFriends_51_100, "friends_51_100", friends_51_100_start, friends_51_100_end, 51, 100);
        // generateIdsWithFriends(setsOfFriends_101_200, "friends_101_200", friends_101_200_start, friends_101_200_end, 101, 200);
        // generateIdsWithFriends(setsOfFriends_201_1000, "friends_201_1000", friends_201_1000_start, friends_201_1000_end, 201, 1000);
        // generateIdsWithFriends(setsOfFriends_1001_4999, "friends_1001_4999", friends_1001_4999_start, friends_1001_4999_end, 201, 1000);
        // generateIdsWithFriends(setsOfFriends_5000, "friends_5000", friends_5000_start, friends_5000_end, 5000, 5000);

        // generateIdsWithFriends2(setsOfFriends_0, "friends_0", friends_0_start, friends_0_end, 0, 0);
        // generateIdsWithFriends2(setsOfFriends_1_2, "friends_1_2", friends_1_2_start, friends_1_2_end, 1, 2);
        // generateIdsWithFriends2(setsOfFriends_3_10, "friends_3_10", friends_3_10_start, friends_3_10_end, 3, 10);
        // generateIdsWithFriends2(setsOfFriends_21_50, "friends_21_50", friends_21_50_start, friends_21_50_end, 21, 50);
        // generateIdsWithFriends2(setsOfFriends_51_100, "friends_51_100", friends_51_100_start, friends_51_100_end, 51, 100);
        // generateIdsWithFriends2(setsOfFriends_101_200, "friends_101_200", friends_101_200_start, friends_101_200_end, 101, 200);
        // generateIdsWithFriends2(setsOfFriends_201_1000, "friends_201_1000", friends_201_1000_start, friends_201_1000_end, 201, 1000);
        // generateIdsWithFriends2(setsOfFriends_1001_4999, "friends_1001_4999", friends_1001_4999_start, friends_1001_4999_end, 201, 1000);
        // generateIdsWithFriends2(setsOfFriends_5000, "friends_5000", friends_5000_start, friends_5000_end, 5000, 5000);

        generateIdsWithFriends3(setsOfFriends_0, "friends_0", friends_0_start, friends_0_end, 0, 0);
        generateIdsWithFriends3(setsOfFriends_1_2, "friends_1_2", friends_1_2_start, friends_1_2_end, 1, 2);
        generateIdsWithFriends3(setsOfFriends_3_10, "friends_3_10", friends_3_10_start, friends_3_10_end, 3, 10);
        generateIdsWithFriends3(setsOfFriends_11_20, "friends_11_20", friends_11_20_start, friends_11_20_end, 11, 20);
        generateIdsWithFriends3(setsOfFriends_21_50, "friends_21_50", friends_21_50_start, friends_21_50_end, 21, 50);
        generateIdsWithFriends3(setsOfFriends_51_100, "friends_51_100", friends_51_100_start, friends_51_100_end, 51, 100);
        generateIdsWithFriends3(setsOfFriends_101_200, "friends_101_200", friends_101_200_start, friends_101_200_end, 101, 200);
        generateIdsWithFriends3(setsOfFriends_201_1000, "friends_201_1000", friends_201_1000_start, friends_201_1000_end, 201, 1000);
        generateIdsWithFriends3(setsOfFriends_1001_4999, "friends_1001_4999", friends_1001_4999_start, friends_1001_4999_end, 1001, 4999);
        generateIdsWithFriends3(setsOfFriends_5000, "friends_5000", friends_5000_start, friends_5000_end, 5000, 5000);

        friendsCount2Interval.entrySet().stream().sorted(Comparator.comparingInt(Entry::getKey)).forEach(entry -> System.out.println("friends count: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));
    }

}
