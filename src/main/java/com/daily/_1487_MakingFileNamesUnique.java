package com.daily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/3/3 11:54
 * @description: _1487_MakingFileNamesUnique
 *
 * 1487. 保证文件名唯一
 * 给你一个长度为 n 的字符串数组 names 。你将会在文件系统中创建 n 个文件夹：在第 i 分钟，新建名为 names[i] 的文件夹。
 *
 * 由于两个文件 不能 共享相同的文件名，因此如果新建文件夹使用的文件名已经被占用，系统会以 (k) 的形式为新文件夹的文件名添加后缀，其中 k 是能保证文件名唯一的 最小正整数 。
 *
 * 返回长度为 n 的字符串数组，其中 ans[i] 是创建第 i 个文件夹时系统分配给该文件夹的实际名称。
 *
 *
 *
 * 示例 1：
 *
 * 输入：names = ["pes","fifa","gta","pes(2019)"]
 * 输出：["pes","fifa","gta","pes(2019)"]
 * 解释：文件系统将会这样创建文件名：
 * "pes" --> 之前未分配，仍为 "pes"
 * "fifa" --> 之前未分配，仍为 "fifa"
 * "gta" --> 之前未分配，仍为 "gta"
 * "pes(2019)" --> 之前未分配，仍为 "pes(2019)"
 * 示例 2：
 *
 * 输入：names = ["gta","gta(1)","gta","avalon"]
 * 输出：["gta","gta(1)","gta(2)","avalon"]
 * 解释：文件系统将会这样创建文件名：
 * "gta" --> 之前未分配，仍为 "gta"
 * "gta(1)" --> 之前未分配，仍为 "gta(1)"
 * "gta" --> 文件名被占用，系统为该名称添加后缀 (k)，由于 "gta(1)" 也被占用，所以 k = 2 。实际创建的文件名为 "gta(2)" 。
 * "avalon" --> 之前未分配，仍为 "avalon"
 * 示例 3：
 *
 * 输入：names = ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"]
 * 输出：["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece(4)"]
 * 解释：当创建最后一个文件夹时，最小的正有效 k 为 4 ，文件名变为 "onepiece(4)"。
 * 示例 4：
 *
 * 输入：names = ["wano","wano","wano","wano"]
 * 输出：["wano","wano(1)","wano(2)","wano(3)"]
 * 解释：每次创建文件夹 "wano" 时，只需增加后缀中 k 的值即可。
 * 示例 5：
 *
 * 输入：names = ["kaido","kaido(1)","kaido","kaido(1)"]
 * 输出：["kaido","kaido(1)","kaido(2)","kaido(1)(1)"]
 * 解释：注意，如果含后缀文件名被占用，那么系统也会按规则在名称后添加新的后缀 (k) 。
 *
 *
 * 提示：
 *
 * 1 <= names.length <= 5 * 10^4
 * 1 <= names[i].length <= 20
 * names[i] 由小写英文字母、数字和/或圆括号组成。
 * 通过次数10,892提交次数32,869
 */
public class _1487_MakingFileNamesUnique {


    /**
     * 方法：哈希表
     *
     * 由于每个文件夹的文件名必须各不相同，因此在创建文件夹时，需要判断原始文件名是否被占用，
     * 如果被占用，需要找到最小的正整数 k 并以后缀的形式添加到文件名后面，使得实际名称不被占用。
     *
     * 为了判断原始文件名是否被占用和找到保证文件名唯一的最小正整数 k，需要使用哈希表记录每个原始文件名的使用次数。
     * 如果原始文件名不在哈希表中，则原始文件名没有被占用，不需要添加后缀，此时在哈希表中记录此name的出现次数是0；
     * 如果原始文件名在哈希表中且使用次数是 k，则需要在原始文件名后面添加后缀 (k+1)，
     * 然后在哈希表中将原始文件名的使用次数加 1。
     *
     * 上述思路并不能保证文件名唯一，因为添加后缀 (k+1) 之后得到的实际名称仍可能已经被占用。
     *
     * 示例 2  ["gta","gta(1)","gta","avalon"] 就存在这样的情况，
     * 当遍历到 names[2]=“gta" 时，虽然哈希表中 “gta" 的使用次数是 0，但是 “gta(1)" 已经被占用，
     * 因此 后缀 k+1 并不能保证文件名唯一，需要将 k 的值增加到 2 才能保证文件名唯一。
     *
     * 示例 3 ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"] 也存在这样的情况，
     * 当遍历到 names[4]=“onepiece" 时，虽然哈希表中 “onepiece" 的使用次数是 1，但是 “onepiece(2)" 已经被占用，
     * “onepiece(3)" 也已经被占用，因此 k=1,2,3 都不能保证文件名唯一，需要将 k 的值增加到 4 才能保证文件名唯一。
     *
     * 因此，为了保证文件名唯一，当使用次数是 k 时，需要判断添加后缀 (k+1) 之后的实际名称是否被占用，
     * 如果实际名称仍被占用则需要增加 k 的值，直到添加后缀 (k) 之后的实际名称不被占用。再更新哈希表中此文件名的出现次数
     *
     * 还需要注意的一点是，如果一个文件夹的文件名添加了后缀，则添加后缀之后的实际名称也会影响到后续创建的文件夹的命名，
     * 因此也需要将实际名称添加到哈希表中，由于添加后缀之后的实际名称第一次出现，因此实际名称在哈希表中的使用次数是 1。
     *
     * 考虑以下例子：names=[“name",“name",“name(1)"]。为了保证文件名唯一，
     * names[1] 需要变成 “name(1)"，当遍历到 names[2]=“name(1)" 时，该名称已经被 names[1] 的实际名称占用，
     * 因此 names[2] 需要变成 “name(1)(1)"。
     *
     *
     * 【总结】
     * 根据上述分析，遍历每个文件名时，保证文件名唯一的做法如下：
     *
     * 如果原始文件名不在哈希表中，则原始文件名没有被占用，因此实际名称即为原始文件名，并将原始文件名和使用次数 0 存入哈希表；
     *
     * 如果原始文件名已经在哈希表中，则原始文件名已经被占用，
     *      从哈希表中得到原始文件名的使用次数 k，并判断添加后缀 (k+1) 之后的实际名称是否被占用，
     *      如果被占用则增加 k 的值，直到添加后缀 (k+1) 之后的实际名称不被占用，
     *      然后在哈希表中将原始名称的使用次数更新为 k+1，
     *      并将新的名称和使用次数 0 存入哈希表。
     *
     * 作者：stormsunshine
     * 链接：https://leetcode.cn/problems/making-file-names-unique/solution/by-stormsunshine-lwig/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param names
     * @return
     */
    public String[] getFolderNames(String[] names) {
        // 保存名字和出现次数
        List<String> ans = new ArrayList<>();
        // 保存结果
        Map<String, Integer> map = new HashMap<>();
        // 顺序遍历
        for (String name : names) {
            // 未被占用过
            if (!map.containsKey(name)) {
                // 直接采用
                ans.add(name);
                // 记录出现次数，刚开始相当于后缀(0)
                map.put(name, 0);
            } else {
                // 已经存在
                // 根据出现次数 得到 新的后缀
                int idx = map.get(name) + 1;
                // 得到新的名字
                String nName = name + "(" + idx + ")";
                // 如果被占用，就继续增加后缀编号
                while (map.containsKey(nName)) {
                    nName = name + "(" + ++idx + ")";
                }
                // 找到最小的未出现的后缀
                // 使用这个新名字
                ans.add(nName);
                // 更新原名字的出现次数
                map.put(name, idx);
                // 记录新名字的出现次数
                map.put(nName, 0);
            }
        }
        // list 转为 array
        return ans.toArray(new String[0]);
    }
}
