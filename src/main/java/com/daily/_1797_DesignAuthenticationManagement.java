package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/2/9 13:33
 * @description: _1797_DesignAuthenticationManagement
 *
 * 1797. 设计一个验证系统
 * 你需要设计一个包含验证码的验证系统。每一次验证中，用户会收到一个新的验证码，这个验证码在 currentTime 时刻之后 timeToLive 秒过期。如果验证码被更新了，那么它会在 currentTime （可能与之前的 currentTime 不同）时刻延长 timeToLive 秒。
 *
 * 请你实现 AuthenticationManager 类：
 *
 * AuthenticationManager(int timeToLive) 构造 AuthenticationManager 并设置 timeToLive 参数。
 * generate(string tokenId, int currentTime) 给定 tokenId ，在当前时间 currentTime 生成一个新的验证码。
 * renew(string tokenId, int currentTime) 将给定 tokenId 且 未过期 的验证码在 currentTime 时刻更新。如果给定 tokenId 对应的验证码不存在或已过期，请你忽略该操作，不会有任何更新操作发生。
 * countUnexpiredTokens(int currentTime) 请返回在给定 currentTime 时刻，未过期 的验证码数目。
 * 如果一个验证码在时刻 t 过期，且另一个操作恰好在时刻 t 发生（renew 或者 countUnexpiredTokens 操作），过期事件 优先于 其他操作。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：
 * ["AuthenticationManager", "renew", "generate", "countUnexpiredTokens", "generate", "renew", "renew", "countUnexpiredTokens"]
 * [[5], ["aaa", 1], ["aaa", 2], [6], ["bbb", 7], ["aaa", 8], ["bbb", 10], [15]]
 * 输出：
 * [null, null, null, 1, null, null, null, 0]
 *
 * 解释：
 * AuthenticationManager authenticationManager = new AuthenticationManager(5); // 构造 AuthenticationManager ，设置 timeToLive = 5 秒。
 * authenticationManager.renew("aaa", 1); // 时刻 1 时，没有验证码的 tokenId 为 "aaa" ，没有验证码被更新。
 * authenticationManager.generate("aaa", 2); // 时刻 2 时，生成一个 tokenId 为 "aaa" 的新验证码。
 * authenticationManager.countUnexpiredTokens(6); // 时刻 6 时，只有 tokenId 为 "aaa" 的验证码未过期，所以返回 1 。
 * authenticationManager.generate("bbb", 7); // 时刻 7 时，生成一个 tokenId 为 "bbb" 的新验证码。
 * authenticationManager.renew("aaa", 8); // tokenId 为 "aaa" 的验证码在时刻 7 过期，且 8 >= 7 ，所以时刻 8 的renew 操作被忽略，没有验证码被更新。
 * authenticationManager.renew("bbb", 10); // tokenId 为 "bbb" 的验证码在时刻 10 没有过期，所以 renew 操作会执行，该 token 将在时刻 15 过期。
 * authenticationManager.countUnexpiredTokens(15); // tokenId 为 "bbb" 的验证码在时刻 15 过期，tokenId 为 "aaa" 的验证码在时刻 7 过期，所有验证码均已过期，所以返回 0 。
 *
 *
 * 提示：
 *
 * 1 <= timeToLive <= 108
 * 1 <= currentTime <= 108
 * 1 <= tokenId.length <= 5
 * tokenId 只包含小写英文字母。
 * 所有 generate 函数的调用都会包含独一无二的 tokenId 值。
 * 所有函数调用中，currentTime 的值 严格递增 。
 * 所有函数的调用次数总共不超过 2000 次。
 * 通过次数20,706提交次数32,716
 */
public class _1797_DesignAuthenticationManagement {

    /**
     * 方法一：哈希表
     * 思路
     *
     * 按照题意，用一个哈希表 map 保存验证码和过期时间。
     * 调用 generate 时，将验证码-过期时间对直接插入 map。
     * 调用 renew 时，如果验证码存在并且未过期，则更新过期时间。
     * 调用 countUnexpiredTokens 时，遍历整个 map，统计未过期的验证码的数量。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/design-authentication-manager/solution/she-ji-yi-ge-yan-zheng-xi-tong-by-leetco-kqqb/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class AuthenticationManager {

        // 记录 验证码-过期时间
        Map<String, Integer> map;
        int timeToLive;

        public AuthenticationManager(int timeToLive) {
            map = new HashMap<>();
            this.timeToLive = timeToLive;
        }

        public void generate(String tokenId, int currentTime) {
            // 插入，验证码-过期时间
            map.put(tokenId, currentTime + timeToLive);
        }

        public void renew(String tokenId, int currentTime) {
            // 对于有效的验证码
            if (map.containsKey(tokenId) && map.get(tokenId) > currentTime) {
                // 更新其过期时间
                map.put(tokenId, currentTime + timeToLive);
            } else {
                // 移除无效key
                map.remove(tokenId);
            }
        }

        public int countUnexpiredTokens(int currentTime) {
            int ans = 0;
            // 遍历所有验证码的过期时间，统计还未过期的数量
            for (String key : map.keySet()) {
                if (map.get(key) > currentTime) {
                    ans++;
                }
            }
            return ans;
        }
    }

    /**
     * 方法二：哈希表 + 双向链表
     * 思路
     *
     * 用一个双向链表保存验证码和过期时间的顺序。
     * 链表的节点保存验证码和过期时间信息，并且在一条链表上，节点保存的过期时间是递增的。
     * 因为时间是增长的，所以每次新的验证码节点插入链表末尾，形成到期时间递增的双向链表。
     * 额外用一个哈希表 map 来保存验证码-节点对，提供根据验证码来快速访问节点的方法。
     * 调用 generate 时，新建节点，将节点插入链表末尾，并插入 map。
     * 调用 renew 时，如果验证码存在并且未过期，根据 map 访问到节点，更新过期时间，并将节点从原来位置移动到链表末尾。
     * 调用 countUnexpiredTokens 时，从链表头部开始，删除过期的节点（已经无效了），并从 map 删除。
     * 最后 map 的长度就是未过期的验证码的数量。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/design-authentication-manager/solution/she-ji-yi-ge-yan-zheng-xi-tong-by-leetco-kqqb/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 双向链表节点
    class Node {
        // 验证码
        String key;
        // 到期时间
        int expire;
        // 后、前
        Node prev;
        Node next;

        public Node(String key, int expire) {
            this(key, expire, null, null);
        }

        public Node(String key, int expire, Node prev, Node next) {
            this.key = key;
            this.expire = expire;
            this.prev = prev;
            this.next = next;
        }
    }
    class AuthenticationManager2 {
        // 有效时间
        int timeToLive;
        // 链表首尾dummy节点
        Node head;
        Node tail;
        // 验证码-链表节点
        Map<String, Node> map;

        public AuthenticationManager2(int timeToLive) {
            this.timeToLive = timeToLive;
            map = new HashMap<>();
            // 初始化首尾两个dummy
            head = new Node("head", -1);
            tail = new Node("tail", -1);
            // 链接
            head.next = tail;
            tail.prev = head;
        }

        public void generate(String tokenId, int currentTime) {
            // 生成新节点
            Node node = new Node(tokenId, currentTime + timeToLive);
            // 保存对应关系到map
            map.put(tokenId, node);
            // 插入尾节点，最后一个有效节点是 tail.prev
            Node last = tail.prev;
            // 和前面节点的双向链接
            last.next = node;
            node.prev = last;
            // 和后面节点的双向链接
            node.next = tail;
            tail.prev = node;
        }

        public void renew(String tokenId, int currentTime) {
            // 只处理还在有效期的验证码节点
            if (map.containsKey(tokenId) && map.get(tokenId).expire > currentTime) {
                Node node = map.get(tokenId);
                // 断开前后链接
                node.prev.next = node.next;
                node.next.prev = node.prev;
                // 更新到期时间
                node.expire = currentTime + timeToLive;
                // 重新插入链表末尾
                Node last = tail.prev;
                last.next = node;
                node.prev = last;
                node.next = tail;
                tail.prev = node;
            }
        }

        public int countUnexpiredTokens(int currentTime) {
            Node node = head.next;
            // 遍历有效节点，节点的到期时间按顺序递增，在遍历过程中移除已经到期的节点
            while (node != tail && node.expire <= currentTime) {
                // 断开链接
                node.prev.next = node.next;
                node.next.prev = node.prev;
                // 从map中移除
                map.remove(node.key);
                // 下一个
                node = node.next;
            }
            // 最后map中全为有效验证码
            return map.size();
        }
    }
}
