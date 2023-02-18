package com.daily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/2/18 14:17
 * @description: _1237_FindPositiveIntegerSolutionForAGivenEquation
 *
 * 1237. 找出给定方程的正整数解
 * 给你一个函数  f(x, y) 和一个目标结果 z，函数公式未知，请你计算方程 f(x,y) == z 所有可能的正整数 数对 x 和 y。满足条件的结果数对可以按任意顺序返回。
 *
 * 尽管函数的具体式子未知，但它是单调递增函数，也就是说：
 *
 * f(x, y) < f(x + 1, y)
 * f(x, y) < f(x, y + 1)
 * 函数接口定义如下：
 *
 * interface CustomFunction {
 * public:
 *   // Returns some positive integer f(x, y) for two positive integers x and y based on a formula.
 *   int f(int x, int y);
 * };
 * 你的解决方案将按如下规则进行评判：
 *
 * 判题程序有一个由 CustomFunction 的 9 种实现组成的列表，以及一种为特定的 z 生成所有有效数对的答案的方法。
 * 判题程序接受两个输入：function_id（决定使用哪种实现测试你的代码）以及目标结果 z 。
 * 判题程序将会调用你实现的 findSolution 并将你的结果与答案进行比较。
 * 如果你的结果与答案相符，那么解决方案将被视作正确答案，即 Accepted 。
 *
 *
 * 示例 1：
 *
 * 输入：function_id = 1, z = 5
 * 输出：[[1,4],[2,3],[3,2],[4,1]]
 * 解释：function_id = 1 暗含的函数式子为 f(x, y) = x + y
 * 以下 x 和 y 满足 f(x, y) 等于 5：
 * x=1, y=4 -> f(1, 4) = 1 + 4 = 5
 * x=2, y=3 -> f(2, 3) = 2 + 3 = 5
 * x=3, y=2 -> f(3, 2) = 3 + 2 = 5
 * x=4, y=1 -> f(4, 1) = 4 + 1 = 5
 * 示例 2：
 *
 * 输入：function_id = 2, z = 5
 * 输出：[[1,5],[5,1]]
 * 解释：function_id = 2 暗含的函数式子为 f(x, y) = x * y
 * 以下 x 和 y 满足 f(x, y) 等于 5：
 * x=1, y=5 -> f(1, 5) = 1 * 5 = 5
 * x=5, y=1 -> f(5, 1) = 5 * 1 = 5
 *
 *
 * 提示：
 *
 * 1 <= function_id <= 9
 * 1 <= z <= 100
 * 题目保证 f(x, y) == z 的解处于 1 <= x, y <= 1000 的范围内。
 * 在 1 <= x, y <= 1000 的前提下，题目保证 f(x, y) 是一个 32 位有符号整数。
 * 通过次数12,556提交次数17,451
 */
public class _1237_FindPositiveIntegerSolutionForAGivenEquation {

    /**
     * 方法：枚举 + 双指针
     *
     * 由于 x、y 取值范围在 1 - 1000，且只考虑正整数，因此可以通过枚举解决
     * for (int x = 1; x <= 1000;  x++) {
     *     for (int y = 1; y <= 1000; ++y) {
     *         if (f(x, y) == z) {
     *             ans.add((x, y))
     *         }
     *     }
     * }
     *
     * 在此基础上进行优化：
     * f(x, y) < f(x + 1, y)
     * f(x, y) < f(x, y + 1)
     * 即，x确定时，f(x,y)关于y单调递增；y确定时，f(x,y)关于x单调递增
     *
     * 那么对于一个指定的 x，要找到与之配对的y，可以通过 1-1000 内的二分搜索取代内层for循环；
     *
     * 进一步优化：
     *
     * 对于某个 x 找到的配对的 y，对于 x+1 来说，由于单调性，与它配对的 y1 必然 < y
     * 即，我们在枚举x从1到1000的增大变化时，与之匹配的y是一直减小的状态，
     *
     * 因此我们采用双指针，初始时让x=1(自增)，y=1000（自减），此时 t =  f(x,y)
     * 如果 t > z，那么与x配对的y需要减少，y--
     * 如果 t < z，那么此时需要增加x或增加y，由于y是从大到小变过来的，因此此时 x++
     * 如果 t = z，ans.add((x,y))， 然后 x++，对x+1来说，与之配对的 y必然要更小，所以 y--
     * 循环进行上述过程，直到 x > 1000 或 y < 1 结束
     *
     * @param customfunction
     * @param z
     * @return
     */
    public List<List<Integer>> findSolution(CustomFunction customfunction, int z) {
        // 双指针， x递增，为每一个x寻找配对的y，y从大到小搜索，f单调递增
        int x = 1, y = 1000;
        List<List<Integer>> list = new ArrayList<>();
        // 数据范围
        while (x <= 1000 && y >= 1) {
            // 当前结果
            int f = customfunction.f(x, y);
            //  匹配成功
            if (f == z) {
                // 加入x,y，并搜索下一个x，与之配对的y需要更小
                list.add(Arrays.asList(x++, y--));
            // 小于目标值
            } else if (f < z) {
                // 需要增大x或y，由于y递减搜索，更大的y已经尝试过，因此这里增大x
                x++;
            // 大于目标值
            } else {
                // 需要减小x或y，由于x递增搜索，更大的x已经尝试过，因此这里减小y
                y--;
            }
        }
        // 返回
        return list;
    }
}

interface CustomFunction {
    public int f(int x, int y);
}
