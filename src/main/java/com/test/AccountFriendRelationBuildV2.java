package com.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author wangwei37 <wangwei37@kuaishou.com>
 * Created on 2022-07-06
 * <p>
 * 具有好友关系的账号构造（双向）
 */
public class AccountFriendRelationBuildV2 {

    // 总共500w
    private static final long account_id_start = 1L;
    private static final long account_id_end = 5000000L;
    private static final long total_accounts = 5000000L;

    private static final Random random = new Random();

    private static final String[] intervalKeys = {"friends_0_0", "friends_1_2", "friends_3_10", "friends_11_20",
            "friends_21_50", "friends_51_100", "friends_101_200", "friends_201_1000", "friends_1001_4999",
            "friends_5000_5000"};

    // 每种账号数目占比
    private static final double[] percentage = {0.2, 0.2, 0.2, 0.1, 0.1, 0.05, 0.05, 0.05, 0.04, 0.01};

    private static final Map<String, Integer> percentIndexMap = new HashMap<>() {
        {
            put("friends_0_0", 0);
            put("friends_1_2", 1);
            put("friends_3_10", 2);
            put("friends_11_20", 3);
            put("friends_21_50", 4);
            put("friends_51_100", 5);
            put("friends_101_200", 6);
            put("friends_201_1000", 7);
            put("friends_1001_4999", 8);
            put("friends_5000_5000", 9);
        }
    };

    // 每种账号，将范围朋友数离散化时的步长
    private static final Map<String, Integer> friendsAddStepMap = new HashMap<>() {
        {
            put("friends_0_0", 0);
            put("friends_1_2", 1);
            put("friends_3_10", 2);
            put("friends_11_20", 3);
            put("friends_21_50", 5);
            put("friends_51_100", 8);
            put("friends_101_200", 16);
            put("friends_201_1000", 120);
            put("friends_1001_4999", 500);
            put("friends_5000_5000", 0);
        }
    };

    private static final Map<String, long[]> idIntervalByFriendsCountMap = new HashMap<>();

    static {
        // 初始分配：根据账号占比分配区间范围
        long curIdStart = account_id_start, curIdEnd, curCount;
        for (int i = 0; i < intervalKeys.length; i++) {
            String intervalKey = intervalKeys[i];
            curCount = (long) (total_accounts * percentage[percentIndexMap.get(intervalKey)]);
            curIdEnd = curIdStart + curCount - 1;
            idIntervalByFriendsCountMap.put(intervalKey, new long[] {curIdStart, curIdEnd});
            curIdStart = curIdEnd + 1;
        }
    }

    // 朋友数为k的账号对应区间为v
    public static final Map<Integer, long[]> friendsCount2Interval = new HashMap<>();

    // k 朋友数，v 此朋友数对应的 多个 账号区间，每个区间大小是 朋友数 + 1，比如 3 个朋友的账号对应区间有 1-4，5-8，这些区间内互相形成朋友关系
    public static final Map<Integer, TreeSet<long[]>> friendsCount2Relation = new HashMap<>();


    /**
     * 生成 minFriends_maxFriends 个 好友 的账号区间
     */
    public static void divideIdIntervalByFriendsCount(String intervalKey) {
        // 从 intervalKey 中解析出当前区间 代表的 朋友数范围
        String[] info = intervalKey.split("_");
        if (info.length < 3) {
            throw new RuntimeException("invalid interval key: " + intervalKey);
        }
        // 最少朋友数，最大朋友数
        int minFriends = Integer.parseInt(info[1]), maxFriends = Integer.parseInt(info[2]);
        // 拿到初始分好的大区间
        long[] originInterval = idIntervalByFriendsCountMap.get(intervalKey);
        // 区间起始id和结束id
        long minId = originInterval[0], maxId = originInterval[1];
        // 将朋友数区间离散成多个取值，比如[3-10]，变成3、5、7、9四种取值，朋友数目划分增量
        // 离散化时，每一步增加几个朋友，针对朋友数分布区间大小不同，初始时设置了不同的步长，不可能每次增加只1个朋友
        int[] friendsOfPart = discritization(minFriends, maxFriends, friendsAddStepMap.get(intervalKey));
        // 分成了几种离散值
        int parts = friendsOfPart.length;
        // 将原来的id大区间划分为对应可能数目的小区间，让不同小区间内的账号有不同数目的朋友
        long countOfIdsPerPart = (long) (total_accounts * percentage[percentIndexMap.get(intervalKey)]) / parts;
        // 为了让原区间内账号的朋友数更符合分布，让每个子区间去选择随机的一个朋友数，而不是连续，
        // 比如避免 区间 1-10 有1个朋友，10-50 有 3 个朋友这种，随机一下，比如 1-10 有 3 个朋友，11-50 有 1 个朋友
        // 离散化后的朋友数是递增的，我们给它打乱
        shuffle(friendsOfPart);
        // 为每个子区间分配朋友
        long startId = minId, endId = 0L;
        // 每个子区间部分，选择一种 朋友数x
        // 为了保证这个子区间内的所有账号都能有x个朋友，这个区间大小必须为 x+1 的整数倍，否则我们就丢弃最后不足x+1的部分，让下一个子区间顺序前移
        // 这样最后，这些子区间仍然连续，并且最后一个子区间的末尾一定比初始时的区间末尾值更小。
        for (int i = 0; i < parts; i++) {
            // friendsOfPart[i] 代表当前子区间账号要具有的朋友数木
            if (countOfIdsPerPart % (friendsOfPart[i] + 1) == 0) {
                endId = startId + countOfIdsPerPart - 1;
            } else {
                // 不能分成 x+1 的小组，就丢弃最后那部分；这里调整 end 的话，下一个子区间的start会顺序前移，相互之前仍然连续
                long idCountsOfThisPart = countOfIdsPerPart / (friendsOfPart[i] + 1) * (friendsOfPart[i] + 1);
                endId = startId + idCountsOfThisPart - 1;
            }
            // 记录满足这种朋友数的账号所处的子区间
            friendsCount2Interval.put(friendsOfPart[i], new long[] {startId, endId});
            // 在这个子区间内实际分配朋友，（分成 x+1 的小组）
            generateFriends(friendsOfPart[i]);
            // 下一个子区间会顺序前移
            startId = endId + 1;
        }
        // 最后，由于丢弃了一部分，所以最后一个子区间的结尾 endId 一定 比开始时的 interval[1] 小，我们丢弃那部分区间
        originInterval[1] = endId;
    }


    /**
     * 生成朋友，将区间划分为多个大小为（friendsCount + 1）的小组，每个小组内成员互相为好友。
     * 每个区间在初步划分时已根据朋友数进行调整，这里一定能整除
     * @param friendCount
     */
    public static void generateFriends(int friendCount) {
        if (friendCount == 0) {
            return;
        }
        long[] interval = friendsCount2Interval.get(friendCount);
        long startId = interval[0];
        long endId = interval[1];
        long parts = (endId - startId + 1) / (friendCount + 1);
        // 划分为一个个大小为x+1的小组并保存每个小组的id范围，用于快速确认某个id所在小组内其他成员（即他的朋友）
        friendsCount2Relation.put(friendCount, new TreeSet<>(Comparator.comparingLong(o -> o[0])));
        TreeSet<long[]> relations = friendsCount2Relation.get(friendCount);
        for (int i = 0; i < parts; i++) {
            long curStart = startId + i * (friendCount + 1);
            long curEnd = curStart + friendCount;
            relations.add(new long[] {curStart, curEnd});
        }
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
     * 得到某个用户的所有朋友，根据 id 和 他的 朋友数目
     * 根据朋友数得到满足有这么多朋友的全部id区间
     * 再在这个区间对应的全部小区间内找到这个人的全部朋友
     */
    private static Set<Long> getFriendsById(int friendCount, long id) {
        TreeSet<long[]> relations = friendsCount2Relation.get(friendCount);
        long[] floor = relations.floor(new long[] {id, id});
        // System.out.println(Arrays.toString(floor));
        Set<Long> set = new HashSet<>();
        assert floor != null;
        for (long i = floor[0]; i <= floor[1]; i++) {
            if (i != id) {
                set.add(i);
            }
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

        idIntervalByFriendsCountMap.entrySet().stream().sorted(Comparator.comparingLong(e -> e.getValue()[1])).forEach(
                entry -> System.out.println(
                        "intervalKey: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));

        // 根据每个区间要生成的账号的好友数目范围调整区间（只可能右边界缩小），保证能够实现每个人的朋友数在要求范围内
        Arrays.stream(intervalKeys).forEach(AccountFriendRelationBuildV2::divideIdIntervalByFriendsCount);

        System.out.println("-----------------------------adjust interval by friends count--------------------------------");

        idIntervalByFriendsCountMap.entrySet().stream().sorted(Comparator.comparingLong(e -> e.getValue()[1])).forEach(
                entry -> System.out.println(
                        "intervalKey: " + entry.getKey() + ", id interval: " + Arrays.toString(entry.getValue())));

        System.out.println("-----------------------------all intervals for every possible count of friends--------------------------------");
        friendsCount2Interval.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(e -> System.out.println("friends count: " + e.getKey() + ", interval: " + Arrays.toString(e.getValue())));
    }


    public static void main(String[] args) {
        generate();
        System.out.println(getFriendsById(3, 2500007L));
    }

}
