package com.window;

/**
 * @author wangwei
 * 2020/7/27 12:18
 */
public class _00_SlidingWindowDemo {

    /**
     * 滑动窗口本质上还是双指针问题，该类问题比较明显的特征是
     *      会让你维护一个固定大小的区间。连续区间
     * 当然有些问题可能比这复杂或者简单一些，没有这个特征，但做的多了还是能看出来应该使用这种方法
     *
     * 一般都是左闭又开 [i, j)
     */

    /*
    int left = 0, right = 0;

    while (right < s.size()) {
        // 增大窗口
        window.add(s[right]);
        right++;

        while (window needs shrink) {
            // 缩小窗口
            window.remove(s[left]);
            left++;
        }
    }*/

    /**
     * 这个算法技巧的时间复杂度是 O(N)
     *
     * 如何向窗口中添加新元素，
     * 什么时候如何停止扩大窗口
     * 什么时候需要缩小窗口，
     * 每次扩大或缩小窗口时需要更新哪些数据
     * 在窗口滑动的哪个阶段更新结果集
     *
     */

    /**
     * 滑动窗口算法的思路是这样：
     *
     * 1、我们在字符串 S 中使用双指针中的左右指针技巧，初始化 left = right = 0，把索引左闭右开区间 [left, right) 称为一个「窗口」。
     *
     * 2、我们先不断地增加 right 指针扩大窗口 [left, right)，直到窗口中的字符串符合要求（包含了 T 中的所有字符）。
     *
     * 3、此时，我们停止增加 right，转而不断增加 left 指针缩小窗口 [left, right)，直到窗口中的字符串不再符合要求（不包含 T 中的所有字符了）。同时，每次增加 left，我们都要更新一轮结果。
     *
     * 4、重复第 2 和第 3 步，直到 right 到达字符串 S 的尽头。
     *
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/find-all-anagrams-in-a-string/solution/hua-dong-chuang-kou-tong-yong-si-xiang-jie-jue-zi-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    /**
     * 给你一个字符串S、一个字符串T,请在字符串S里面找出:包含T所有字母的最小子串。
     *
     * 很明显，所有符合要求的子串长度都应该等于T的长度，
     * 并且子串都是连续部分，所以就相当于一个固定大小的窗口再滑动
     *
     * need记录需要凑齐的字符; window 维护窗口中的已有字符
     * valid 变量表示窗口中满足 need 条件的字符个数，如果 valid 和 need.size 的大小相同，则说明窗口已满足条件，已经完全覆盖了串 T
     *
     * 现在开始套模板，只需要思考以下四个问题：
     *
     * 1、当移动 right 扩大窗口，即加入字符时，应该更新哪些数据？
     *
     * 2、什么条件下，窗口应该暂停扩大，开始移动 left 缩小窗口？
     *
     * 3、当移动 left 缩小窗口，即移出字符时，应该更新哪些数据？
     *
     * 4、我们要的结果应该在扩大窗口时还是缩小窗口时进行更新？
     *
     *
     * unordered_map 就是哈希表（字典），它的一个方法 count(key) 相当于 Java 的 containsKey(key) 可以判断键 key 是否存在。
     *
     * 可以使用方括号访问键对应的值 map[key]。需要注意的是，如果该 key 不存在，C++ 会自动创建这个 key，并把 map[key] 赋值为 0。
     *
     * 所以代码中多次出现的 map[key]++ 相当于 Java 的 map.put(key, map.getOrDefault(key, 0) + 1)
     */

    /* 滑动窗口算法框架
    void slidingWindow(string s, string t) {
        // window就是要维护的子串，need就是window中必须包含的部分
        unordered_map<char, int> need, window;
        // 统计T中每个字符出现次数
        for (char c : t) need[c]++;

        int left = 0, right = 0;
        int valid = 0;
        while (right < s.size()) {
            // c 是将移入窗口的字符
            char c = s[right];
            // 右移窗口
            right++;
            // 进行窗口内数据的一系列更新
            ...

            // debug 输出的位置
            printf("window: [%d, %d)\n", left, right);
            //********************

            // 判断左侧窗口是否要收缩
            while (window needs shrink) {
                // d 是将移出窗口的字符
                char d = s[left];
                // 左移窗口
                left++;
                // 进行窗口内数据的一系列更新
                ...
            }
        }
    }
    */

}
