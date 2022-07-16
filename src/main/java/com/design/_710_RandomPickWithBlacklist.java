package com.design;

import java.util.*;

/**
 * @author wangwei
 * 2021/12/7 21:41
 *
 * 给定一个包含 [0，n) 中不重复整数的黑名单 blacklist ，写一个函数从 [0, n) 中返回一个不在 blacklist 中的随机整数。
 *
 * 对它进行优化使其尽量少调用系统方法 Math.random() 。
 *
 * 提示:
 *
 * 1 <= n <= 1000000000
 * 0 <= blacklist.length < min(100000, N)
 * [0, n) 不包含 n ，详细参见 interval notation 。
 * 示例 1：
 *
 * 输入：
 * ["Solution","pick","pick","pick"]
 * [[1,[]],[],[],[]]
 * 输出：[null,0,0,0]
 * 示例 2：
 *
 * 输入：
 * ["Solution","pick","pick","pick"]
 * [[2,[]],[],[],[]]
 * 输出：[null,1,1,1]
 * 示例 3：
 *
 * 输入：
 * ["Solution","pick","pick","pick"]
 * [[3,[1]],[],[],[]]
 * 输出：[null,0,0,2]
 * 示例 4：
 *
 * 输入：
 * ["Solution","pick","pick","pick"]
 * [[4,[2]],[],[],[]]
 * 输出：[null,1,3,1]
 * 输入语法说明：
 *
 * 输入是两个列表：调用成员函数名和调用的参数。Solution的构造函数有两个参数，n 和黑名单 blacklist。pick 没有参数，输入参数是一个列表，即使参数为空，也会输入一个 [] 空列表。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/random-pick-with-blacklist
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _710_RandomPickWithBlacklist {

    class Solution {

        HashMap<Integer, Integer> map = new HashMap<>();

        Random random = new Random();

        /**
         * 方法一：最后三个例子超时
         * 遇到黑名单里的数字就跳过
         * 否则，按照顺序记录其他数字
         * 比如 N = 5, blacklist=[2,3]
         * map {(0, 0), (1, 1), (2, 4), (3, 5)}
         * map.size 就是一共几个有效数字，在 [0, map.size)范围内取随机，再用map.get就可以实现
         * @param n
         * @param blacklist
         */
        // public Solution(int n, int[] blacklist) {
        //     Set<Integer> set = new HashSet<>();
        //     // 转成set用于快速判断当前数字是否在黑名单内
        //     for (int i : blacklist) {
        //         set.add(i);
        //     }
        //     for (int i = 0; i < n; ++i) {
        //         if (set.contains(i)) {
        //             continue;
        //         }
        //         map.put(map.size(), i);
        //     }
        // }

        /**
         * 题目要求少调用random，不然我可能写
         * res = random.nextInt(n)
         * while (res in blacklist) {
         *     res = random.nextInt(n);
         * }
         * return res;
         * @return
         */
        // public int pick() {
        //     return map.get(random.nextInt(map.size()));
        // }


        /**
         *
         * 方法二：黑名单映射
         * 对于 [0, N) 我们可以直接 randomInt(N)
         * B是黑名单，黑名单里面的数字也是 [0, N), 那么白名单中数的个数为 N - len(B)，可以直接在 [0, N - len(B)) 中随机生成整数。
         * 但是生成的随机数可能在 B 中
         * 所以我们要把所有小于 N - len(B) 但却在黑名单中的数字（会被随机到） 一一映射到 大于等于 N - len(B) （不会被随机到，但本应该被随机到）且应该在白名单中的数字。
         *
         * 本来应该是 0-----N-1， 然后黑名单里面有 len(B) 个数，那么实际有效的数字是 N - len(B) 个数字
         * 那么，假设只考虑 从 0 - N-len(B)-1
         *      这些数字里面，有一些数字出现在了黑名单里面，要讲他们映射到什么地方去呢？
         *      只考虑 [0, N-len(B)-1], 漏掉了 本来应该包含的 N-len(B) -- N-1  这些数字，当然，这些数字也有个别出现在了 黑名单中。
         * ！！！【所以】，实际上的映射：
         *      在黑名单中且 < N-len(B) 的这些数字     -->     在 N-len(B) - N-1 中，且不在黑名单中的数字
         *      这两部分的元素个数应该是【一致】的
         *
         * 这样一来，我们取随机数 x = randInt(N - len(B))
         * 如果 x 在黑名单中，那它肯定存在一个映射，我们就返回它唯一对应的那个应该在白名单中的数即可。
         *
         * 例如当 N = 6，B = [0, 2, 3] 时，我们在 [0, 3) 中随机生成整数，并将 0 映射到 4，2 映射到 5，
         * 这样随机生成的整数就是 [4, 1, 5] 中的一个。
         *
         * 我们将黑名单分成两部分，第一部分 X 的数都小于 N - len(B)，需要进行映射；第二部分 Y 的数都大于等于 N - len(B)，这些数不需要进行映射，因为并不会随机到它们。
         * W 表示大于等于 N - len(B) 且应该在白名单中的数，X 和 W 的长度一定是相等的。
         * sz = N -len(B), 假设 B 中有 x 个数字 小于 sz，
         * 那么 W 里面的数字包括 原来的(sz到N) - 黑名单里面(len(B)-x) = (N - 1 - sz + 1) - (len(B) - x) = N - sz -len(B) + x = x
         * 所以 W和X里面的数字个数一样，能够形成一一映射
         *
         * 随后遍历 X 和 W，构造一个映射表（HashMap）M，将 X 和 W 中的数构造一一映射。
         *
         * 在 [0, N - len(B)) 中随机生成整数 a 时，如果 a 出现在 M 中，则将它的映射返回，否则直接返回 a。
         *
         * 作者：LeetCode
         * 链接：https://leetcode-cn.com/problems/random-pick-with-blacklist/solution/hei-ming-dan-zhong-de-sui-ji-shu-by-leetcode-2/
         * 来源：力扣（LeetCode）
         * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
         *
         * 作者：LeetCode
         * 链接：https://leetcode-cn.com/problems/random-pick-with-blacklist/solution/hei-ming-dan-zhong-de-sui-ji-shu-by-leetcode-2/
         * 来源：力扣（LeetCode）
         * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
         * @param n
         * @param blacklist
         */
        int sz;
        public Solution(int n, int[] blacklist) {
            // sz 保存白名单中数字个数
            sz = n - blacklist.length;
            // set保存所有 >= sz 且应该在白名单中的数字
            // 0-sz也属于白名单，但他们是前sz个数，不用考虑
            Set<Integer> set = new HashSet<>();
            // 首先 如果没有黑名单，那么 sz-n 之间的数字都属于白名单
            for (int i = sz; i < n; ++i) {
                set.add(i);
            }
            // 白名单(sz-N)里面去除 出现在黑名单中的数字
            for (int j : blacklist) {
                // if (set.contains(j)) {
                //     set.remove(j);
                // }
                set.remove(j);
            }
            // 如果黑名单里面某个数字在[0, sz)内，那么它会被随机到，需要将它映射到sz之外的某个白名单的数字
            Iterator<Integer> iter = set.iterator();
            for (int j : blacklist) {
                if (j < sz) {
                    // 保存映射关系
                    map.put(j, iter.next());
                }
            }
        }

        public int pick() {
            // 对 [0, sz) 进行随机
            int idx = random.nextInt(sz);
            // 如果是黑名单的数字就返回它映射的白名单
            // 如果不是黑名单数字那就返回自己
            return map.getOrDefault(idx, idx);
        }
    }
}
