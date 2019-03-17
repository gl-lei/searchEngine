package com.liujun.search.algorithm.boyerMoore.use;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author liujun
 * @version 0.0.1
 * @date 2019/03/17
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCharMatcherBMBadChars {

  /** 字符串的查找操作，验证坏字符的规则 */
  @Test
  public void test01BadCharMatcher() {
    String matchers = "abd";
    CharMatcherBMBadChars instance = CharMatcherBMBadChars.getInstance(matchers);

    String src = "abcacabdc";

    int startIndex = 0;

    int findIndex = instance.matcherIndex(src.toCharArray(), startIndex);
    Assert.assertEquals(5, findIndex);
  }

  /** 字符串的查找操作,使用坏字符规则 */
  @Test
  public void test02BadCharMatcher() {

    String src = "abcacabcbacabc";
    String matchers = "cbacabc";
    int index = 0;

    CharMatcherBMBadChars instance = CharMatcherBMBadChars.getInstance(matchers);

    int findIndex = instance.matcherIndex(src.toCharArray(), index);
    Assert.assertEquals(7, findIndex);
  }
}
