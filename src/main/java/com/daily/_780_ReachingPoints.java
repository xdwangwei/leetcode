package com.daily;

/**
 * @author wangwei
 * 2022/4/9 9:46
 *
 * 780. 到达终点
 * 给定四个整数 sx , sy ，tx 和 ty，如果通过一系列的转换可以从起点 (sx, sy) 到达终点 (tx, ty)，则返回 true，否则返回 false。
 *
 * 从点 (x, y) 可以转换到 (x, x+y)  或者 (x+y, y)。
 *
 *
 *
 * 示例 1:
 *
 * 输入: sx = 1, sy = 1, tx = 3, ty = 5
 * 输出: true
 * 解释:
 * 可以通过以下一系列转换从起点转换到终点：
 * (1, 1) -> (1, 2)
 * (1, 2) -> (3, 2)
 * (3, 2) -> (3, 5)
 * 示例 2:
 *
 * 输入: sx = 1, sy = 1, tx = 2, ty = 2
 * 输出: false
 * 示例 3:
 *
 * 输入: sx = 1, sy = 1, tx = 1, ty = 1
 * 输出: true
 *
 *
 * 提示:
 *
 * 1 <= sx, sy, tx, ty <= 109
 */
public class _780_ReachingPoints {

    /**
     * 辗转相除法。反向计算
     *
     * 如果从 (sx,sy) 开始正向计算，则可能的情况非常多，会超出时间限制 或者 堆栈溢出
     * 注意到 sx,sy,tx,ty 都是正整数，因此对于给定的状态 (tx,ty)，只有当 tx != ty 时才存在上一个状态，且上一个状态唯一，可能的情况如下：
     *
     *      如果 tx=ty，除非状态 (tx,ty) 即为起点状态，否则上一个状态肯定有个坐标是0，不符合题目数据取值范围
     *
     *      如果 tx>ty，则上一个状态一定是 (tx−ty,ty)；因为 (tx, ty-tx) 会出现负数
     *
     *      如果 tx<ty，则上一个状态是 (tx,ty−tx)；因为 (tx−ty,ty) 会出现负数
     *
     * 链接：https://leetcode-cn.com/problems/reaching-points/solution/dao-da-zhong-dian-by-leetcode-solution-77fo/
     *
     * 因此：如果一个对是 (a,b)，那么它之前必然是 (a,b−a) 或者 (a−b,b)。
     * 且由于整个过程没有出现负数，所以必然是 a,b 中较大者减去较小者，这表明之前两种情况只有一种是正确的。
     * 我们可以一直沿着这两种情况中的一种走，直到新的 a,b 的不等关系交换。
     *
     * 而什么时候这种关系会被打破，只有当新的 a' 变成 a mod b（或者新的 b' 变成 b mod a）这种不等关系才会交换，我们可以利用这一点加速程序。
     * 剩下的就是简单的边界条件的判断。
     *
     * 时间复杂度：O(logmax{tx,ty})。
     *
     * 即：
     * 由于每一步反向操作一定是将 tx 和 ty 中的较大的值减小，因此当 tx>ty 时可以直接将 tx 的值更新为 tx mod ty，
     * 当 tx<ty 时可以直接将 ty 的值更新为 ty mod tx。
     *
     * 当反向操作的条件不成立时，根据 tx 和 ty 的不同情况分别判断是否可以从起点转换到终点。
     *
     * 如果 tx=sx 且 ty=sy，则已经到达起点状态，因此可以从起点转换到终点。
     *
     * 如果 tx=sx 且 ty!=sy，则 tx 不能继续减小(tx再减小会出现tx<sx,肯定不能到达)，只能减小 ty，因此必须 ty>sy 并且 (ty−sy)mod tx=0 时可以从起点转换到终点。
     *                                                                      因为在这个过程中tx不能减小，所以正向过程是tx一直不变，y坐标一直累加tx，那么这个差值必须能整除tx
     *
     * 如果 ty=sy 且 tx!=sx，则 ty 不能继续减小(ty不能小于sy，否则肯定不能到达)，只能减小 tx，并且只有当 tx>sx 且 ((tx−sx)mod ty=0 时可以从起点转换到终点。
     *                                                                                       原因与上边类似
     *
     * 如果 tx!=sx 且 ty!=sy，则不可以从起点转换到终点。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/reaching-points/solution/dao-da-zhong-dian-by-leetcode-solution-77fo/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：zqy1018
     * 链接：https://leetcode-cn.com/problems/reaching-points/solution/c-zhan-zhuan-xiang-chu-fa-by-zqy1018/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param sx
     * @param sy
     * @param tx
     * @param ty
     * @return
     * 官方题解，迭代写法
     */
    public boolean reachingPoints(int sx, int sy, int tx, int ty) {
        // 保证 tx 和 ty 有效的前提下，一直沿着(每次只能选择大的减去小的)这一条路径倒退
        while (tx > sx && ty > sy && tx != ty) {
            if (tx > ty) {
                tx %= ty;
            } else {
                ty %= tx;
            }
        }
        // 倒退到起点
        if (tx == sx && ty == sy) {
            return true;
        // 此时不能减小tx，那么要能够走到终点就必须保证 tx不变，ty 累加 n 个tx 一直到 sy
        } else if (tx == sx) {
            return ty > sy && (ty - sy) % tx == 0;
        // 此时不能减小ty，那么要能够走到终点就必须保证 ty不变，tx 累加 n 个ty 一直到 sx
        } else if (ty == sy) {
            return tx > sx && (tx - sx) % ty == 0;
        // 否则无法到达终点
        } else {
            return false;
        }
    }

    /**
     * 递归写法
     * 每次倒退一步，倒退原则是 大坐标减去小坐标，小坐标不变
     * @param sx 起点
     * @param sy
     * @param tx 当前倒退到的位置
     * @param ty 当前倒退到的位置
     * @return
     */
    public boolean reachingPoints2(int sx, int sy, int tx, int ty) {
        // 起点就是终点 或者说 倒退
        if (sx == tx && sy == ty) {
            return true;
        }
        // 否则 tx != ty,不然上一步会出现0坐标
        // 起点位置不能比终点还大
        if (tx == ty || sx > tx || sy > ty) {
            return false;
        }
        // 倒退选择，每次选大的减去小的得到上一步
        if (tx > ty) {
            tx = tx % ty;
        } else {
            ty = ty % tx;
        }
        // 某个坐标和起点一致了，就不能再减小了，且不应该再变化，接下来只能通过 另一个坐标 累加n次tx得到终点
        if (tx == sx) {
            return (ty - sy) % tx == 0;
        } else if (ty == sy) {
            return (tx - sx) % ty == 0;
        }
        // 一次只倒退了一步，因此这里不能直接return false，应该继续递归，tx和ty已倒退，这里不用再分情况改
        return reachingPoints(sx, sy, tx, ty);
    }
}
