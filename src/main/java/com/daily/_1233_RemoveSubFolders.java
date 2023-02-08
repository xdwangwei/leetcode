package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/2/8 20:17
 * @description: _1233_RemoveSubFolders
 *
 * 1233. 删除子文件夹
 * 你是一位系统管理员，手里有一份文件夹列表 folder，你的任务是要删除该列表中的所有 子文件夹，并以 任意顺序 返回剩下的文件夹。
 *
 * 如果文件夹 folder[i] 位于另一个文件夹 folder[j] 下，那么 folder[i] 就是 folder[j] 的 子文件夹 。
 *
 * 文件夹的「路径」是由一个或多个按以下格式串联形成的字符串：'/' 后跟一个或者多个小写英文字母。
 *
 * 例如，"/leetcode" 和 "/leetcode/problems" 都是有效的路径，而空字符串和 "/" 不是。
 *
 *
 * 示例 1：
 *
 * 输入：folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
 * 输出：["/a","/c/d","/c/f"]
 * 解释："/a/b" 是 "/a" 的子文件夹，而 "/c/d/e" 是 "/c/d" 的子文件夹。
 * 示例 2：
 *
 * 输入：folder = ["/a","/a/b/c","/a/b/d"]
 * 输出：["/a"]
 * 解释：文件夹 "/a/b/c" 和 "/a/b/d" 都会被删除，因为它们都是 "/a" 的子文件夹。
 * 示例 3：
 *
 * 输入: folder = ["/a/b/c","/a/b/ca","/a/b/d"]
 * 输出: ["/a/b/c","/a/b/ca","/a/b/d"]
 *
 *
 * 提示：
 *
 * 1 <= folder.length <= 4 * 104
 * 2 <= folder[i].length <= 100
 * folder[i] 只包含小写字母和 '/'
 * folder[i] 总是以字符 '/' 起始
 * 每个文件夹名都是 唯一 的
 * 通过次数27,202提交次数45,389
 */
public class _1233_RemoveSubFolders {

    /**
     * 方法一：字典树
     *
     * 思路与算法
     *
     * 文件夹的拓扑结构正好是树形结构，即字典树上的每一个节点就是一个文件夹，以'/'分割的每一个部分，就是一条路径。
     *
     * 对于字典树中的每一个节点，我们需要字段标记其是否为端点节点，与此同时我们又想快速得到此节点对应的初始文件夹路径，
     * 因此，让每个节点存储一个变量 ref，
     * 如果 ref ≥ 0，说明该节点对应着folder[ref]，否则（ref=−1）说明该节点只是一个中间节点。
     *
     * 我们首先将每一个文件夹路径按照 “/” 进行分割，分割后的结果作为一条路径加入字典树中。
     *
     * 随后我们对字典树进行一次深度优先搜索，
     * 搜索的过程中，如果我们走到了一个 ref ≥ 0 的节点，就将其加入答案，
     *      此节点所在子树中所有端点节点均为其需要删除的“子文件夹”，不需要向下遍历，因此向上返回
     * 否则，遍历当前节点的全部子节点
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/remove-sub-folders-from-the-filesystem/solution/shan-chu-zi-wen-jian-jia-by-leetcode-sol-0x8d/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 字典树
    class Trie {
        // 为-1代表中间节点，>=0代表叶子节点，且对应原文件夹为 folders[i]
        int inf;
        // 子节点
        Map<String, Trie> children;

        Trie() {
            // 初始化为-1
            inf = -1;
            children = new HashMap<>();
        }
    }
    // 向字典树root中插入节点folder，指定其inf
    private void insert(String folder, int inf, Trie root) {
        // 分割‘/’得到路径，由于路径以'/'开始，因此从第一部分开始取
        String[] info = folder.split("/");
        Trie node = root;
        // 插入节点
        for (int i = 1; i < info.length; ++i) {
            String path = info[i];
            // 创建子节点
            node.children.putIfAbsent(path, new Trie());
            // 下移
            node = node.children.get(path);
        }
        // 叶子节点，指定inf
        node.inf = inf;
    }
    // 对字典树root进行dfs
    private void dfs(List<String> list, Trie node, String[] folders) {
        // 遇到端点节点，保留当前节点对应文件夹
        // 其子树中所有端点节点均为其需要删除的“子文件夹”，不需要向下遍历，因此向上返回
        if (node.inf > -1) {
            list.add(folders[node.inf]);
            return;
        }
        // 中间节点，遍历所有子节点
        for (Trie child : node.children.values()) {
            dfs(list, child, folders);
        }
    }

    /**
     * 字典树
     * @param folder
     * @return
     */
    public List<String> removeSubfolders(String[] folder) {
        Trie root = new Trie();
        // 把每个folder插入字典树中，指定对应inf（在folder数组中的索引）
        for (int i = 0; i < folder.length; i++) {
            insert(folder[i], i, root);
        }
        List<String> res = new ArrayList<>();
        // dfs字典树，找到所有父文件夹
        dfs(res, root, folder);
        // 返回
        return res;
    }

    /**
     * 方法二：排序
     * 思路与算法
     *
     * 我们可以将字符串数组 folder 按照字典序进行排序。
     * 当排序完毕后，相关的主目录和子目录就会被排列在一起。如：[/a,/a/b,/a/c, /b,/b/c,/b/d, /c,/c/d,/c/e]
     * 在遍历过程中，用变量p记录当前部分的父文件夹的索引，用变量i记录当前文件夹索引
     * 我们需要找出每一部分（主+多个子目录）的主目录，如[/a , /b , /c]。
     * 初始时 0号文件夹就是第一部分的父目录，因此 p=0，i从1开始，
     * 每一部分的主子目录之间必然满足
     *      folder[i].startsWith(folder[p]) && folder[i].charAt(folder[p].length)=='/'
     *      后半部分不可少，不然 /a, /ac 也会被判断为成立，实际上这两个都是主目录
     * 如果 条件不成立，说明遇到了下一部分的主目录，
     * 此时 folder[i]就是下一部分的父目录，保留 folder[i]，更新 p = i
     * 。。。。。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/remove-sub-folders-from-the-filesystem/solution/shan-chu-zi-wen-jian-jia-by-leetcode-sol-0x8d/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param folder
     * @return
     */
    public List<String> removeSubfolders2(String[] folder) {
        // 按字典序排序
        Arrays.sort(folder);
        // 当排序完毕后，相关的主目录和子目录就会被排列在一起。如：[/a,/a/b,/a/c, /b,/b/c,/b/d, /c,/c/d,/c/e]
        // 找出每一部分的主目录
        List<String> res = new ArrayList<>();
        // folder[0]就是第一部分的父目录
        res.add(folder[0]);
        // p记录当前部分的父文件夹的索引，从0开始，变量i记录当前文件夹索引，从1开始
        for (int i = 1, p = 0; i < folder.length; ++i) {
            // 遇到了下一部分的主目录（和前一部分的最后一个子目录，此条件不成立）
            if (!(folder[i].startsWith(folder[p]) && folder[i].charAt(folder[p].length())=='/')) {
                // 保留当前主目录
                res.add(folder[i]);
                // 更新p为i
                p = i;
            }
        }
        // 返回
        return res;
    }
}
