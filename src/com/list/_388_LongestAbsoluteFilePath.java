package com.list;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * 2022/4/20 12:41
 *
 * 388. 文件的最长绝对路径
 * 假设有一个同时存储文件和目录的文件系统。下图展示了文件系统的一个示例：
 *
 *
 *
 * 这里将 dir 作为根目录中的唯一目录。dir 包含两个子目录 subdir1 和 subdir2 。subdir1 包含文件 file1.ext 和子目录 subsubdir1；subdir2 包含子目录 subsubdir2，该子目录下包含文件 file2.ext 。
 *
 * 在文本格式中，如下所示(⟶表示制表符)：
 *
 * dir
 * ⟶ subdir1
 * ⟶ ⟶ file1.ext
 * ⟶ ⟶ subsubdir1
 * ⟶ subdir2
 * ⟶ ⟶ subsubdir2
 * ⟶ ⟶ ⟶ file2.ext
 * 如果是代码表示，上面的文件系统可以写为 "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext" 。'\n' 和 '\t' 分别是换行符和制表符。
 *
 * 文件系统中的每个文件和文件夹都有一个唯一的 绝对路径 ，即必须打开才能到达文件/目录所在位置的目录顺序，所有路径用 '/' 连接。上面例子中，指向 file2.ext 的 绝对路径 是 "dir/subdir2/subsubdir2/file2.ext" 。每个目录名由字母、数字和/或空格组成，每个文件名遵循 name.extension 的格式，其中 name 和 extension由字母、数字和/或空格组成。
 *
 * 给定一个以上述格式表示文件系统的字符串 input ，返回文件系统中 指向 文件 的 最长绝对路径 的长度 。 如果系统中没有文件，返回 0。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：input = "dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"
 * 输出：20
 * 解释：只有一个文件，绝对路径为 "dir/subdir2/file.ext" ，路径长度 20
 * 示例 2：
 *
 *
 * 输入：input = "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext"
 * 输出：32
 * 解释：存在两个文件：
 * "dir/subdir1/file1.ext" ，路径长度 21
 * "dir/subdir2/subsubdir2/file2.ext" ，路径长度 32
 * 返回 32 ，因为这是最长的路径
 * 示例 3：
 *
 * 输入：input = "a"
 * 输出：0
 * 解释：不存在任何文件
 * 示例 4：
 *
 * 输入：input = "file1.txt\nfile2.txt\nlongfile.txt"
 * 输出：12
 * 解释：根目录下有 3 个文件。
 * 因为根目录中任何东西的绝对路径只是名称本身，所以答案是 "longfile.txt" ，路径长度为 12
 *
 *
 * 提示：
 *
 * 1 <= input.length <= 104
 * input 可能包含小写或大写的英文字母，一个换行符 '\n'，一个制表符 '\t'，一个点 '.'，一个空格 ' '，和数字。
 */
public class _388_LongestAbsoluteFilePath {

    /**
     *  dir
     *  ⟶ subdir1
     *  ⟶ ⟶ file1.ext
     *  ⟶ ⟶ subsubdir1
     *  ⟶ subdir2
     *  ⟶ ⟶ subsubdir2
     *  ⟶ ⟶ ⟶ file2.ext
     *
     * 可以看作一个树状结构
     * 对于 subdir1 和 subdir2 来说，前缀都是 dir
     * 对于 file1.ext和subsubdir1来说，前缀都是 dir/subdir1
     * 通过每行开始的 \t 的个数，就能知道这个文件或文件夹在第几层，
     * 那么对于同一层的文件来说，它的最终长度就是它的前缀(相同的上一层)长度+/+自己的路径长度
     * 所以，我们需要保存不同层的前缀长度，需要一个 (层数，前缀长) 的结构，所以使用 hashmap
     * 比如 dir，第0层，前缀长度就是上一层的长度，为0
     * 比如 subdir1，第1层，前缀长度就是第0层的长度，也就是 dir的长度3
     * 比如 file1.ext，第2层，前缀长度就是第1层的长度，也就是 subdir1前缀长度+它自身的长度
     * 那么 对于 第 count 层的文件，它的前缀长度就是 它的上一层的长度 + / + 它自身的长度
     *      最后组成路径时不包含 \t，所以去掉 \t 的长度
     *      并且，如果当前是一个文件，那么还要用它的绝对路径长度去更新res，绝对路径长度是包含\n\t这些符号的，所以应该是 上一层长度+本行长度
     *
     *  并且，每个文件或取的上一层的路径长度一定要是最近的上一层路径长度
     *  比如
     *  dir
     *  ⟶ dir1
     *  ⟶ ⟶ file1.ext
     *  ⟶ subdir2
     *  ⟶ ⟶ abc.txt
     *  对于abc.txt来说，它在第2层，它的上一层应该是 dir/subdir2, 长度是 11
     *  但是，第1层还有一个dir1，所以在计算abc.txt长度时，不能出现 dir/dir1/abc.txt
     *  幸运的是，对于上面这种输入格式，每个文件的上一行一定是它严格所属的上一级目录信息，这样，当处理上一行时，它会将之前记录的同层文件长度的值覆盖掉，保证abc.txt获取到的上一层值时最新值
     */

    /**
     * 通过 input.split("\\n")得到一共多少行，即多少个问价夹或文件信息
     * 对每一行，
     */
    public int lengthLongestPath(String input) {
        // 保存不同层当前目录长度，对于它的多个下一层文件或文件夹来说，其公共前缀相同
        Map<Integer, Integer> map = new HashMap<>();
        // 每一行是一个文件路径
        String[] split = input.split("\\n");
        int res = 0;
        for (String line: split) {
            // 当前行长度
            int lineLen = line.length();
            int count = 0;
            // 统计 count 的个数，当前文件处于哪一层
            for (int i = 0; i < lineLen && line.charAt(i) == '\t'; ++i) {
                count++;
            }
            // 它的上一层的路径长度
            int lastDepthLen = map.getOrDefault(count - 1, 0);
            // 拼接它之后，当层的路径长度，不包含 \t
            map.put(count, lastDepthLen + lineLen - count);
            // 如果是一个文件，那么用它的绝对路径长度来更新res
            if (line.contains(".")) {
                // 绝对路径包含 前面的\t
                // 本来是上一层\当前文件长，这里不用加 \ 的长度1是因为 lineLen里面包含了当前行最后一个换行符，相当于加上了前面的\
                res = Math.max(res, lastDepthLen + lineLen);
            }
        }
        return res;
    }
}
