package com.order;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/25 周一 16:04
 **/
public class _06_Zconvert {
    
    // 转换后的 Z 字符串，按行(再按列)输出，找出每行每个字符在原字符串中的位置
    // 每次间隔是 2 * rows - 2
    // 非第一行和最后一行需要把中间那个特殊的也输出来，可认位一次输出两个，当前位置满足规律，再输出后续那个
    public static String solution1(String s, int rows){
        if (rows == 1) return s;
        StringBuilder builder = new StringBuilder();
        int len = s.length();
        int step = 2 * rows -  2;
        for (int i = 0; i < rows; i++){ // 把 'Z' 按行输出
            // 每一行第一个元素对应原串的下标就是i(i+0)，距离下一个元素位置的差为j
            // 下一个适当得到元素下标要在len之内
            for (int j = 0; i + j < len; j += step ){
                // 每一行第一个元素是符合规律的直接添加
                builder.append(s.charAt(i + j));
                // 对于非第一行，可认为满足规律的元素后跟了一个‘不恰当的元素’
                // 它不满足 下标每次跨越 step，但是它的位置满足 j + step - i 这个规律
                // 可以通过分析示例得出
                // 适当元素之后那个不恰当的元素下标也要在len之内
                if (i != 0 && i != rows - 1 && j + step - i < len){
                    builder.append(s.charAt(j + step - i));
                }
            }
        }
        return builder.toString();
    }
    
    // 通过从左向右迭代字符串，我们可以轻松地确定原串字符位于 Z 字形图案中的哪一行
    // 不用在乎在哪一列，因为它是 从上到下 再到上，最后按行输出，我只需要把每行保存
    // 我们可以使用 min(rows, len(s)) 个列表来表示 Z 字形图案中的非空行
    // 从左到右迭代 s，将每个字符添加到合适的行。
    // 可以使用当前行和当前方向这两个变量对合适的行进行跟踪，以确定下个字符要去哪个位置(往上还是下)
    // 只有当我们向上移动到最上面的行或向下移动到最下面的行时，当前方向才会发生改变
    public static String solution2(String s, int rows){
        if (rows == 1) return s;
        StringBuilder res = new StringBuilder();
        List<StringBuilder> rowStringList = new ArrayList<>();
        for (int i = 0; i < Math.min(rows, s.length()); i++)
            rowStringList.add(new StringBuilder()); // 初始化用来保存每行字符的列表 
        int curRow = 0; //从第0行开始，然后向下到最后一行，再向上到第一行，再向下
        boolean goDown = false;
        for (int i = 0; i < s.length(); i++){
            rowStringList.get(curRow).append(s.charAt(i));//加入当前行
            if (curRow == 0 || curRow == rows - 1)
                goDown = !goDown; //到达顶部或底部就换方向
            curRow += goDown ? 1 : -1; // 根据goDown决定是去下一行还是上一行
        }
        for (StringBuilder rowStr : rowStringList) {
            res.append(rowStr); // 把每一行加进来
        }
        return res.toString();
    }
}
