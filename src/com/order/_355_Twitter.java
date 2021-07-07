package com.order;

import java.util.*;

/**
 * @author wangwei
 * 2020/8/1 11:22
 *
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，能够看见关注人（包括自己）的最近十条推文。你的设计需要支持以下的几个功能：
 *
 * postTweet(userId, tweetId): 创建一条新的推文
 * getNewsFeed(userId): 检索最近的十条推文。每个推文都必须是由此用户关注的人或者是用户自己发出的。推文必须按照时间顺序由最近的开始排序。
 * follow(followerId, followeeId): 关注一个用户
 * unfollow(followerId, followeeId): 取消关注一个用户
 * 示例:
 *
 * Twitter twitter = new Twitter();
 *
 * // 用户1发送了一条新推文 (用户id = 1, 推文id = 5).
 * twitter.postTweet(1, 5);
 *
 * // 用户1的获取推文应当返回一个列表，其中包含一个id为5的推文.
 * twitter.getNewsFeed(1);
 *
 * // 用户1关注了用户2.
 * twitter.follow(1, 2);
 *
 * // 用户2发送了一个新推文 (推文id = 6).
 * twitter.postTweet(2, 6);
 *
 * // 用户1的获取推文应当返回一个列表，其中包含两个推文，id分别为 -> [6, 5].
 * // 推文id6应当在推文id5之前，因为它是在5之后发送的.
 * twitter.getNewsFeed(1);
 *
 * // 用户1取消关注了用户2.
 * twitter.unfollow(1, 2);
 *
 * // 用户1的获取推文应当返回一个列表，其中包含一个id为5的推文.
 * // 因为用户1已经不再关注用户2.
 * twitter.getNewsFeed(1);
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/design-twitter
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _355_Twitter {

    /**
     * 如果我们把每个用户各自的推文存储在链表里，每个链表节点存储文章 id 和一个时间戳 time（记录发帖时间以便比较），
     * 我们让这个链表是按 time 有序的，(头插即可)，那么如果某个用户关注了 k 个用户，
     * 我们就可以用合并 k 个有序链表的算法合并出有序的推文列表，正确地 getNewsFeed 了！
     *
     * 借助优先队列，从每个列表头拿一个，拿完拿下一个
     * 我们不需要把全部关注者的全部推文放入优先队列，再去拿出前10个
     * 我们可以维护一个固定大小的优先队列，比如10，也可以是所有用户的数目，最多把全部人都关注了
     *
     * 由于每个人的推文列表都是按照发表时间排好序的，
     * 我们只需要把每个人的第一篇推文加入优先队列，
     * 然后取出队列第一个，也就是最近的，
     * 然后把它的next也就是下一条推文放入优先队列，自动排序
     * 再取出队列第一个元素
     *
     * 需要一个 User 类，储存 user 信息，和tweet链表(头结点)，还需要一个 Tweet 类，储存推文信息，并且要作为链表的节点
     */
    // 发表推文的全局时间戳
    private static int timestap;
    // 需要根据userId拿到User对象
    private HashMap<Integer, User> userMap = new HashMap<>();
    // tweet类
    private static class Tweet {
        // 推文id
        private int id;
        // 发表时间
        private int time;
        // 下一个
        private Tweet next;
        /** 创建推文需要id和发表时间 **/
        public Tweet(final int id, final int time) {
            this.id = id;
            this.time = time;
            next = null;
        }
    }
    // user类
    private static class User {
        // 用户id
        private int id;
        // 关注了哪些人
        private Set<Integer> followed;
        // 推文列表的第一个推文
        private Tweet head;
        /** 构造方法  **/
        public User(final int id) {
            this.id = id;
            followed = new HashSet<>();
            this.head = null;
            // 关注自己
            follow(id);
        }
        /** 关注一个人  **/
        public void follow(int followeeId) {
            followed.add(followeeId);
        }
        /** 取消关注一个人  **/
        public void unfollow(int followeeId) {
            // 不能取关自己
            if (followeeId != id)
                followed.remove(followeeId);
        }
        /** 发表一篇推文  **/
        public void postTweet(int tweetId) {
            Tweet tweet = new Tweet(tweetId, timestap);
            // 全局时间戳
            timestap++;
            // 头插法，达到按时间排序
            tweet.next = head;
            head = tweet;
        }
    }

    /** user 发表一条 tweet 动态 */
    public void postTweet(int userId, int tweetId) {
        // 根据用户id拿到这个User对象
        User user = userMap.get(userId);
        if (user == null) {
            // 如果不存在就创建一个新的
            user = new User(userId);
            // 添加到userMap中
            userMap.put(userId, user);
        }
        // 执行这个对象的方法
        user.postTweet(tweetId);
    }

    /** follower 关注 followee，如果 Id 不存在则新建 */
    public void follow(int followerId, int followeeId) {
        // 根据用户id拿到这个User对象
        User follower = userMap.get(followerId);
        if (follower == null) {
            // 如果 关注者 不存在就创建一个新的
            follower = new User(followerId);
            // 添加到userMap中
            userMap.put(followerId, follower);
        }
        // 根据用户id拿到这个User对象
        User followee = userMap.get(followeeId);
        if (followee == null) {
            // 如果 被关注者 不存在就创建一个新的
            followee = new User(followeeId);
            // 添加到userMap中
            userMap.put(followeeId, followee);
        }
        // 执行这个对象的方法
        follower.follow(followeeId);
    }

    /** follower 取关 followee，如果 Id 不存在则什么都不做 */
    public void unfollow(int followerId, int followeeId) {
        // 根据用户id拿到这个User对象
        User user = userMap.get(followerId);
        // 不存在则直接返回
        if (user == null) {
            return;
        }
        // 执行这个对象的方法
        user.unfollow(followeeId);
    }

    /** 返回该 user 关注的人（包括他自己）最近发表的动态(推文id)，
     最多 10 条，而且这些动态必须按从新到旧的时间线顺序排列。*/
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> res = new ArrayList<>();
        // 根据用户id拿到这个User对象
        User user = userMap.get(userId);
        if (user == null) return res;
        // 拿到这个用户的关注列表
        Set<Integer> followed = user.followed;
        // 创建优先队列，按照推文的时间顺序排序
        PriorityQueue<Tweet> priorityQueue = new PriorityQueue<>(followed.size(), (o1, o2) -> o2.time - o1.time);
        // 把每个关注者的第一篇推文加到优先队列
        for (int followeeId: followed) {
            Tweet tweet = userMap.get(followeeId).head;
            if (tweet == null) continue;
            priorityQueue.add(tweet);
        }

        // 优先队列排好序后，逐个取出
        while (!priorityQueue.isEmpty()) {
            // 最多只需要10条最近推文
            if (res.size() == 10) break;
            // 拿出最近一条推文
            Tweet tweet = priorityQueue.poll();
            // 加入结果集
            res.add(tweet.id);
            // 把它的下一条推文加入优先队列
            if (tweet.next != null) priorityQueue.add(tweet.next);
        }
        return res;
    }
}
