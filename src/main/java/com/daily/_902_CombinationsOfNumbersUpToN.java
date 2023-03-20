package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/10/18 9:58
 * @description: _902_CombinationsOfNumbersUpToN
 *
 * 902. 最大为 N 的数字组合
 * 给定一个按 非递减顺序 排列的数字数组 digits 。你可以用任意次数 digits[i] 来写的数字。例如，如果 digits = ['1','3','5']，我们可以写数字，如 '13', '551', 和 '1351315'。
 *
 * 返回 可以生成的小于或等于给定整数 n 的正整数的个数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：digits = ["1","3","5","7"], n = 100
 * 输出：20
 * 解释：
 * 可写出的 20 个数字是：
 * 1, 3, 5, 7, 11, 13, 15, 17, 31, 33, 35, 37, 51, 53, 55, 57, 71, 73, 75, 77.
 * 示例 2：
 *
 * 输入：digits = ["1","4","9"], n = 1000000000
 * 输出：29523
 * 解释：
 * 我们可以写 3 个一位数字，9 个两位数字，27 个三位数字，
 * 81 个四位数字，243 个五位数字，729 个六位数字，
 * 2187 个七位数字，6561 个八位数字和 19683 个九位数字。
 * 总共，可以使用D中的数字写出 29523 个整数。
 * 示例 3:
 *
 * 输入：digits = ["7"], n = 8
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= digits.length <= 9
 * digits[i].length == 1
 * digits[i] 是从 '1' 到 '9' 的数
 * digits 中的所有值都 不同
 * digits 按 非递减顺序 排列
 * 1 <= n <= 109
 * 通过次数9,278提交次数21,289
 */
public class _902_CombinationsOfNumbersUpToN {


    /**
     *
     * 数位 dp，看完去看 _1012_，对数位dp有了认识，理解模板了
     *
     * 假设 nums 数组长度是 m, 假设n是5位数，
     *
     * 先考虑位数比n少的情况
     *      考虑四位数时，每一位上的数字就可以在nums数组中随意选择，共 m ^ 4
     *      考虑三位数时，每一位上的数字就可以在nums数组中随意选择，共 m ^ 3
     *      考虑二位数时，每一位上的数字就可以在nums数组中随意选择，共 m ^ 2
     *      考虑一位数时，每一位上的数字就可以在nums数组中随意选择，共 m ^ 1
     *      累计以上结果即可；由于 nums 中不存在 0，所以不用排除0的情况
     * 再考虑和n位数一样的情况
     *      从高位往低位考虑，假设n从高到低位分别为 a1 a2 a3 a4 a5
     *      题目：nums递增，nums中元素各不相同
     *
     *      第一位数字必须小于等于a1，
     *          如果nums[0] > a1，nums中所有元素都大于a1，当前位置没有可选择的数字，结束
     *          如果nums[n-1] < a1, nums中所有元素都小于a1，当前位置可以随便选，当前位置选完后，剩余位置也可以随便选，共m*m^4种，结束
     *          假设nums中最后一个小于等于a1的数字在第i个位置，其值为x
     *              如果 x < a1, 说明当前位置，可以选择 x 及以前 共i个位置元素，有 i 种选择，
     *                          那么后边四位数字可以随便选，共 m^4 种
     *                          此时选法有 i * m^4，
     *                          直接结束
     *              如果 x = a1,
     *                          如果当前位置选择 x 以前 共i-1个位置元素，有 i-1 种选择，
     *                              此时后边四位数字可以随便选，共 m^4 种
     *                              此时选法有 (i-1) * m^4,
     *                              (这里正确的前提是nums中元素不重复且递增，否则x=a1并不能保证x前面的数字都小于a1，也就是需要过滤重复数字)
     *                          如果当前位置选择x，相当于1种选择
     *                              此时之后的数字如何选择，也就是继续流程，考虑a2，a3，a4，a5了
     *                              如果此时已经是最后一个位置了，那么无需后续，直接ans+1即可。
     *      第二位数字必须小于等于a2，
     *          ...
     *
     *      因为nums递增，考虑有边界二分搜索来找 小于等于x 的最右边界
     *
     *
     * 总结一下就是，来自【宫水三叶数位DP】：
     *      我们将组成 [1,x] 的合法数分成三类：
     *
     *      位数和 x 相同，且最高位比 x 最高位要小的，这部分统计为 res1；
     *      位数和 x 相同，且最高位与 x 最高位相同的，这部分统计为 res2；
     *      位数比 x 少，这部分统计为 res3。
     *
     *      其中 res1 和 res3 求解相对简单，重点落在如何求解 res2 上。
     *
     *      对 x 进行「从高到低」的处理（假定 x 数位为 n），
     *          对于我们要拼凑的数字的第 k 位而言（k不为最高位，最高位已经固定和x一致），
     *          假设在 x 中第 k 位为 cur，那么为了满足「大小限制」关系，我们只能在 [1,cur] 范围内取数(当取cur时，需要考虑后续取值)，
     *          同时为了满足「数字只能取自 nums」的限制，我们可以利用 nums 本身有序，对其进行二分，找到满足 nums[mid] <= cur 的最大下标 r ，
     *          根据 nums[r] 与 cur 的关系进行分情况讨论：
     *      nums[r] < cur: 此时算上 nums[r]，位置 k 共有 r+1 种选择，而后面的每个位置由于 nums[i] 可以使用多次，每个位置都有 m 种选择，共有 n-k 个位置，
     *                      因此该分支共有 (r+1)*m^(n-k) 种合法方案，由于后续位置的方案数（均满足小于关系）已经在这次被统计完成，累加后进行 break；
     *      nums[r] = cur：此时位置 k 也有 r+1 种选择
     *                     若k位置选择r前面的数字，肯定小于cur，此时后面的每个位置由于 nums[i] 可以使用多次，每个位置都有 m 种选择，共有 n-k 个位置，
     *                              因此该分支往后共有 r*m^(n-k) 种合法方案。
     *                     若k位置选择nums[r]，由于 nums[r]=cur，往后还有分支可决策（需要统计），因此需要继续处理，也就是不能break；
     *                              该分支有多少种方案，就是继续走后续流程对ans累加了多少呗
     *                              如果k=0，说明当前已经是最后一个位置了，那没有后续了，直接ans+1就行了
     *      nums[r] > cur: 说明nums所有元素都比cur大，没法选择，并且该分支往后不再满足「大小限制」要求，合法方案数为 0，直接 break。
     *
     * 扩展：
     *      本题相当于考虑 数值在 1-n 之间的取值情况
     *      dp(x): 取值在 [1,n] 的组合数
     *      如果求 取值在 [l,r] 范围的组合数 = dp(r) - dp(l - 1)
     *
     * @param digits
     * @param n
     * @return
     */
    public int atMostNGivenDigitSet(String[] digits, int n) {
        int ans = 0;
        // 得到n的位数，以及每个位置上的数字，倒序保存在list中
        List<Integer> list = new ArrayList<>();
        while (n != 0) {
            list.add(n % 10);
            n /= 10;
        }
        n = list.size();
        // 可选数组的元素个数
        int m = digits.length;
        int[] nums = new int[m];
        // 字符串数组转整型数组
        for (int i = 0; i < m; ++i) {
            nums[i] = Integer.parseInt(digits[i]);
        }
        // 第一部分：位数 小于 n 的部分
        for (int i = 1; i < n; ++i) {
            // 一位数，每次可以选择m个，选择1次
            // 二位数，每次可以选择m个，选择2次
            // n-1位数，每次可以选择m个，选择n-1次
            ans += permutations(m, i);
        }
        // 第二部分：位数 等于 n 的部分
        // i 执行原数字n中从高到低位数值的索引，j 代表当前要凑的数字的从高到低位置索引
        for(int i = n - 1, j = 1; i >= 0; --i, ++j) {
            // 原数字当前位置取值
            int cur = list.get(i);
            // 所有可选值都比 cur 大，没法选择，结束
            if (nums[0] > cur) {
                break;
            }
            // 所有可选值都比cur小，那么当前位置可以随意选择
            // 并且当前位置选完后，后续所有位置也可以随意选择
            if (nums[m - 1] < cur) {
                ans += m * permutations(m, n - j);
                // 注意这里相当于已经考虑完了全部后续，需要结束
                break;
            }
            // 在 nums 中寻找 小于等于 cur 的最大值位置
            // 由于上面两个if已经排除掉了特殊情况，所以这里l不可能会是-1
            int l = rightBoundBinarySearch(nums, cur);
            // 如果这个值小于cur，那么当前位置可以选择这个值及之前所有位置，也就是 l + 1 种选择
            // 并且当前位置选完后，后续所有位置也可以随意选择
            if (nums[l] < cur) {
                ans += (l + 1) * permutations(m, n - j);
                // 注意这里相当于已经考虑完了全部后续，需要结束
                break;
            }
            // 如果这个值等于cur，由于nums递增且不重复，那么这个值前面所有取值肯定都小于cur
            // 如果当前位置选择前cur种情况，那么后续位置可以随意选择
            ans += l * permutations(m, n - j);
            // 如果当前位置选择cur这个值，那么后续位置如何取值，相当于继续进行for循环，所以这里没有break
            // 如果已经是最后一个位置了，那直接ans+1把这种取值算进来即可
            if (i == 0) {
                ans++;
            }
        }
        return ans;
    }


    /**
     * 排列数：每次可以选择k个数字，选择n次，结果数
     * @param k
     * @param n
     * @return
     */
    private int permutations(int k, int n) {
        return (int) Math.pow(k, n);
    }


    /**
     * 右边界二分搜索，在 递增序列 nums 中寻找 <= target 的最后一个元素位置
     * 二分搜索的模板：https://labuladong.github.io/algo/1/11/
     * @param nums
     * @param target
     * @return
     */
    public static int rightBoundBinarySearch(int[] nums, int target) {
        int l = 0, r = nums.length;
        while (l < r) {
            int mid = l + ((r - l) >> 1);
            if (nums[mid] <= target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        // 注意while中，当 nums[mid]<=target 时，l 会自增，所以最终结果必然要对l-1
        // 当nums所有元素都比target小时，会返回最后一个位置
        // 当nums所有元素都比target大时，会返回-1
        return l - 1;
    }


    /**
     * 更简单的写法
     *
     * 作者：muse-77
     *     链接：https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/solution/zhua-wa-mou-si-tu-jie-leetcode-by-muse-7-cfpx/
     *     来源：力扣（LeetCode）
     *     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param digits
     * @param n
     * @return
     */
    public int atMostNGivenDigitSet2(String[] digits, int n) {
        char[] nc = String.valueOf(n).toCharArray();
        int result = 0, ncl = nc.length, dl = digits.length;
        // 先对【非最高位】的其他位，可组装的数字进行统计
        // 就是先统计位数小于n的所有情况
        for (int i = 1; i < ncl; i++) result += Math.pow(dl, i);
        // 再考虑位数和n一样，逐个位进行处理
        for (int i = 0; i < ncl; i++) {
            // 是否需要对比下一个数字
            boolean compareNext = false;
            // 当前位置可以选<=cur(n的当前位)的数字
            for (String digit : digits) {
                char dc = digit.charAt(0); // 将String转换为char
                // 比 cur 小，可以选，并且后续位置可以随意选择，相当于已经统计完了，需要退出
                if (dc < nc[i]) result += Math.pow(dl, ncl - i - 1);
                else {
                    // 和 cur 一样，那么就要考虑后续位置上的数字，也就是 不能退出 最外层的for，但内层for可以提前结束了
                    if (dc == nc[i]) compareNext = true; break;
                }
            }
            if (!compareNext) return result;
        }
        // 如果到最后1位依然满足compareNext，因为最后1位无法再向后对比了，所以最终结果+1
        return ++result;
    }

    public static void main(String[] args) {
        System.out.println(rightBoundBinarySearch(new int[]{1, 3, 5, 7, 9}, 0));
        System.out.println(rightBoundBinarySearch(new int[]{1, 3, 5, 7, 9}, 10));
        System.out.println(rightBoundBinarySearch(new int[]{1, 3, 5, 7, 9}, 8));
        System.out.println(rightBoundBinarySearch(new int[]{1, 3, 5, 7, 9}, 1));
    }
}
