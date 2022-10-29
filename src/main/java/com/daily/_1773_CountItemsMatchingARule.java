package com.daily;

import java.util.List;

/**
 * @author wangwei
 * @date 2022/10/29 10:21
 * @description: _1773_CountItemsMatchingARule
 *
 * 1773. 统计匹配检索规则的物品数量
 * 给你一个数组 items ，其中 items[i] = [typei, colori, namei] ，描述第 i 件物品的类型、颜色以及名称。
 *
 * 另给你一条由两个字符串 ruleKey 和 ruleValue 表示的检索规则。
 *
 * 如果第 i 件物品能满足下述条件之一，则认为该物品与给定的检索规则 匹配 ：
 *
 * ruleKey == "type" 且 ruleValue == typei 。
 * ruleKey == "color" 且 ruleValue == colori 。
 * ruleKey == "name" 且 ruleValue == namei 。
 * 统计并返回 匹配检索规则的物品数量 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：items = [["phone","blue","pixel"],["computer","silver","lenovo"],["phone","gold","iphone"]], ruleKey = "color", ruleValue = "silver"
 * 输出：1
 * 解释：只有一件物品匹配检索规则，这件物品是 ["computer","silver","lenovo"] 。
 * 示例 2：
 *
 * 输入：items = [["phone","blue","pixel"],["computer","silver","phone"],["phone","gold","iphone"]], ruleKey = "type", ruleValue = "phone"
 * 输出：2
 * 解释：只有两件物品匹配检索规则，这两件物品分别是 ["phone","blue","pixel"] 和 ["phone","gold","iphone"] 。注意，["computer","silver","phone"] 未匹配检索规则。
 *
 *
 * 提示：
 *
 * 1 <= items.length <= 104
 * 1 <= typei.length, colori.length, namei.length, ruleValue.length <= 10
 * ruleKey 等于 "type"、"color" 或 "name"
 * 所有字符串仅由小写字母组成
 */
public class _1773_CountItemsMatchingARule {


    /**
     * 简单模拟
     *
     * 题目意思：统计items中符合规则的item个数，每个item是[type,color,name]三部分，规则为：
     *
     * 当 ruleKey == "type" 时， 判断 每个item的 type  部分(idx=0)是否和 ruleValue 相等
     * 当 ruleKey == "color" 时，判断 每个item的 color 部分(idx=0)是否和 ruleValue 相等
     * 当 ruleKey == "name" 时， 判断 每个item的 name  部分(idx=0)是否和 ruleValue 相等
     * @param items
     * @param ruleKey
     * @param ruleValue
     * @return
     */
    public int countMatches(List<List<String>> items, String ruleKey, String ruleValue) {
        // 先判断要用item的哪一个部分去和ruleValue比较
        int idx = ruleKey.charAt(0) == 't' ? 0 : (ruleKey.charAt(0) == 'c' ? 1 : 2);
        // 再统计items中指定部分和ruleValue相等的item数量
        return (int) items.stream().filter(item -> item.get(idx).equals(ruleValue)).count();
    }
}
