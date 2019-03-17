package com.liujun.search.engine.collect;

import com.liujun.search.common.constant.SymbolMsg;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件队列的单元测试
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/03/03
 */
// Junit测试顺序：@FixMethodOrder
// ** MethodSorters.DEFAULT **（默认）
// 默认顺序由方法名hashcode值来决定，如果hash值大小一致，则按名字的字典顺序确定。
// ** MethodSorters.NAME_ASCENDING （推荐） **
//   按方法名称的进行排序，由于是按字符的字典顺序，所以以这种方式指定执行顺序会始终保持一致；
// ** MethodSorters.JVM **
//    按JVM返回的方法名的顺序执行，此种方式下测试方法的执行顺序是不可预测的，即每次运行的顺序可能
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFileQueue {

  /** 仅操作一次 */
  @BeforeClass
  public static void testClean() {
    FileQueue.INSTANCE.clean();
    FileQueue.INSTANCE.openFileQueue();
  }

  /** 从队列中获取一个数据 */
  @Test
  public void test02QueueGet() {

    String lineData = "http://www.sohu.com/" + SymbolMsg.LINE;

    boolean result = FileQueue.INSTANCE.put(lineData);

    Assert.assertEquals(true, result);

    String getData = FileQueue.INSTANCE.get();

    Assert.assertEquals(lineData, getData);
  }

  @Test
  public void test03QueuePutList() {
    List<String> list = new ArrayList<>();

    String line1 = "http://www.sohu.com1/" + SymbolMsg.LINE;
    list.add(line1);
    String line2 = "http://www.sohu.com2/" + SymbolMsg.LINE;
    list.add(line2);
    String line3 = "http://www.sohu.com3/" + SymbolMsg.LINE;
    list.add(line3);
    String line4 = "http://www.sohu.com4/" + SymbolMsg.LINE;
    list.add(line4);

    boolean result = FileQueue.INSTANCE.put(list);
    Assert.assertEquals(true, result);

    Assert.assertEquals(line1, FileQueue.INSTANCE.get());
    Assert.assertEquals(line2, FileQueue.INSTANCE.get());
    Assert.assertEquals(line3, FileQueue.INSTANCE.get());
    Assert.assertEquals(line4, FileQueue.INSTANCE.get());
  }

  /** 测试队列的功能，超过一个buffer大小的情况时 */
  @Test
  public void test06FileQueue() {

    int maxFileQueue = 1000;
    List<String> result = new ArrayList<>(maxFileQueue);
    for (int i = 0; i < maxFileQueue; i++) {
      result.add("http://www.sohu.com/" + i + SymbolMsg.LINE);
    }

    // 将数据放入
    FileQueue.INSTANCE.put(result);

    List<String> getList = new ArrayList<>(maxFileQueue);

    String getBuffList = null;

    while ((getBuffList = FileQueue.INSTANCE.get()) != null) {
      getList.add(getBuffList);
    }

    Assert.assertEquals(result, getList);
  }

  /** 进行关闭后再次读取操作 */
  @Test
  public void test07FileQueue() {

    // 先执行下清理操作,再开户操作
    FileQueue.INSTANCE.closeAll();
    FileQueue.INSTANCE.clean();
    FileQueue.INSTANCE.openFileQueue();

    // 1,写入100
    int maxFileQueue = 500;
    List<String> result = new ArrayList<>(maxFileQueue);
    for (int i = 0; i < maxFileQueue; i++) {
      result.add("http://www.sohu.com/" + i + SymbolMsg.LINE);
    }

    // 将数据放入
    FileQueue.INSTANCE.put(result);

    List<String> compResult = new ArrayList<>(maxFileQueue);

    // 首次需要从0开始读取
    String data = FileQueue.INSTANCE.get();

    Assert.assertNotNull(data);

    // 读取10
    compResult.add(data);

    // 保存offset
    FileQueue.INSTANCE.writeOffset();
    // 再次读取offset
    FileQueue.INSTANCE.readOffset();

    // 再进行读取操作
    String getBuffList = null;

    while ((getBuffList = FileQueue.INSTANCE.get()) != null) {
      compResult.add(getBuffList);
    }

    // 再保存一次
    FileQueue.INSTANCE.writeOffset();

    Assert.assertEquals(result, compResult);
  }
}
