package com.greed;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * 2021/10/17 9:38
 *
 * 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
 *
 * 若可行，输出任意可行的结果。若不可行，返回空字符串。
 *
 * 示例 1:
 *
 * 输入: S = "aab"
 * 输出: "aba"
 * 示例 2:
 *
 * 输入: S = "aaab"
 * 输出: ""
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reorganize-string
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _767_ReorganizeString {

    /**
     * 这道题是典型的使用贪心思想的题。重构字符串时，需要根据每个字母在字符串中出现的次数处理每个字母放置的位置。如果出现次数最多的字母可以在重新排布之后不相邻，则可以重新排布字母使得相邻的字母都不相同。如果出现次数最多的字母过多，则无法重新排布字母使得相邻的字母都不相同。
     *
     * 假设字符串的长度为 nn，如果可以重新排布成相邻的字母都不相同的字符串，每个字母最多出现多少次？
     *
     * 当 nn 是偶数时，有 n/2n/2 个偶数下标和 n/2n/2 个奇数下标，因此每个字母的出现次数都不能超过 n/2n/2 次，否则出现次数最多的字母一定会出现相邻。
     *
     * 当 nn 是奇数时，由于共有 (n+1)/2(n+1)/2 个偶数下标，因此每个字母的出现次数都不能超过 (n+1)/2(n+1)/2 次，否则出现次数最多的字母一定会出现相邻。
     *
     * 由于当 nn 是偶数时，在整数除法下满足 n/2n/2 和 (n+1)/2(n+1)/2 相等，因此可以合并 nn 是偶数与 nn 是奇数的情况：如果可以重新排布成相邻的字母都不相同的字符串，每个字母最多出现 (n+1)/2(n+1)/2 次。
     *
     * 因此首先遍历字符串并统计每个字母的出现次数，如果存在一个字母的出现次数大于 (n+1)/2(n+1)/2，则无法重新排布字母使得相邻的字母都不相同，返回空字符串。如果所有字母的出现次数都不超过 (n+1)/2(n+1)/2，则考虑如何重新排布字母。
     *
     * 以下提供两种使用贪心的方法，分别基于最大堆和计数。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */


    /**
     * 方法一：基于最大堆的贪心
     * 维护最大堆存储字母，堆顶元素为出现次数最多的字母。首先统计每个字母的出现次数，然后将出现次数大于 0的字母加入最大堆。
     *
     * 当最大堆的元素个数大于 1 时，每次从最大堆取出两个字母，拼接到重构的字符串，然后将两个字母的出现次数分别减 1，并将剩余出现次数大于 00 的字母重新加入最大堆。由于最大堆中的元素都是不同的，因此取出的两个字母一定也是不同的，将两个不同的字母拼接到重构的字符串，可以确保相邻的字母都不相同。
     *
     * 如果最大堆变成空，则已经完成字符串的重构。如果最大堆剩下 1 个元素，则取出最后一个字母，拼接到重构的字符串。
     *
     * 对于长度为 n 的字符串，共有 n/2次每次从最大堆取出两个字母的操作，当 n 是奇数时，还有一次从最大堆取出一个字母的操作，因此重构的字符串的长度一定是 n。
     *
     * 不可能出现第一次取出ab，第二次取出bc，因为如果a比b多，那么第一次取出ab后，二者次数都减一，a还是比b多，第二次取出的第一个不会是b
     *
     * 当 n 是奇数时，是否可能出现重构的字符串的最后两个字母相同的情况？如果最后一个字母在整个字符串中至少出现了 2 次，则在最后一次从最大堆取出两个字母时，该字母会先被选出，因此不会成为重构的字符串的倒数第二个字母，也不可能出现重构的字符串最后两个字母相同的情况。
     *
     * 因此，在重构字符串可行的情况下，基于最大堆的贪心可以确保得到正确答案。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public String reorganizeString(String s) {
        if (s.length() < 2) {
            return s;
        }
        int len  = s.length(), maxCount = 0;
        int[] counts = new int[26];
        for (int i = 0; i < len; i++) {
            counts[s.charAt(i) - 'a'] += 1;
            maxCount = Math.max(maxCount, counts[s.charAt(i) - 'a']);
        }
        // 某个字符出现次数 比其他字符次数总和还多
        if (maxCount > (len + 1) / 2) {
            return "";
        }
        // 把这些字符按照出现次数从多到少入队
        PriorityQueue<Character> queue = new PriorityQueue<>((c1, c2) -> counts[c2 - 'a'] - counts[c1 - 'a']);
        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) {
                queue.offer((char)('a' + i));
            }
        }
        StringBuilder builder = new StringBuilder();
        // 每次取出前两个字符，所以确保队列元素数量大于1
        while (queue.size() > 1) {
            char c1 = queue.poll();
            char c2 = queue.poll();
            builder.append(c1).append(c2);
            // 这两字符的出现次数减一
            counts[c1 - 'a']--;
            counts[c2 - 'a']--;
            // 重新入队
            if (counts[c1 - 'a'] > 0) {
                queue.offer(c1);
            }
            if (counts[c2 - 'a'] > 0) {
                queue.offer(c2);
            }
        }
        // 如果总字符数量是奇数，那还会剩下一个
        if (queue.size() > 0) {
            builder.append(queue.poll());
        }
        return builder.toString();
    }

    /**
     * 方法二：基于计数的贪心
     * 首先统计每个字母的出现次数，然后根据每个字母的出现次数重构字符串。
     *
     * 当 n 是奇数且出现最多的字母的出现次数是 (n+1)/2 时，出现次数最多的字母必须全部放置在偶数下标，否则一定会出现相邻的字母相同的情况。其余情况下，每个字母放置在偶数下标或者奇数下标都是可行的。
     *
     * 维护偶数下标 evenIndex 和奇数下标 oddIndex，初始值分别为 0 和 1。遍历每个字母，根据每个字母的出现次数判断字母应该放置在偶数下标还是奇数下标。
     *
     * 首先考虑是否可以放置在奇数下标。根据上述分析可知，只要字母的出现次数不超过字符串的长度的一半（即出现次数小于或等于 n/2），就可以放置在奇数下标，
     * 只有当字母的出现次数超过字符串的长度的一半时，才必须放置在偶数下标。字母的出现次数超过字符串的长度的一半只可能发生在 n 是奇数的情况下，且最多只有一个字母的出现次数会超过字符串的长度的一半。
     *
     * 因为题目要求返回一种不重复排列即可。所以为了简单起见：
     *     先将出现次数的字符全部放置在偶数位置，
     *     再处理其他字符，放置在奇数位置(如果偶数位置还有剩余，那么先安排在偶数位置，再从1开始安排奇数位置)
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public String reorganizeString2(String s) {
        if (s.length() < 2) {
            return s;
        }
        int len  = s.length(), maxCount = 0, maxIndex = -1;
        int[] counts = new int[26];
        for (int i = 0; i < len; i++) {
            counts[s.charAt(i) - 'a'] += 1;
            if (counts[s.charAt(i) - 'a'] > maxCount) {
                maxCount = counts[s.charAt(i) - 'a'];
                // 注意这里，字符的出现次数在变化，但是它的counts数组中的索引是唯一的
                maxIndex = s.charAt(i) - 'a';
            }
        }
        // 某个字符出现次数 比其他字符次数总和还多
        if (maxCount > (len + 1) / 2) {
            return "";
        }
        int index = 0;
        // 先将出现次数最多的字符全部放置在偶数位置
        char[] result = new char[len];
        while (counts[maxIndex]-- > 0) {
            result[index] = (char)(maxIndex + 'a');
            index += 2;
        }
        // 再把所有其他字符安排在奇数位置（如果偶数位置还有剩余，那么先安排在偶数位置，再从1开始安排奇数位置0
        for (int i = 0; i < 26; i++) {
            while (counts[i]-- > 0) {
                // 偶数位置用完了。开始奇数位置
                if (index >= len) {
                    index = 1;
                }
                result[index] = (char)(i + 'a');
                index += 2;
            }
        }
        return new String(result);
    }

    public static void main(String[] args) {
        new _767_ReorganizeString().reorganizeString2("aab");
    }
}
