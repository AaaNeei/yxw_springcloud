package com.yxw.yxw_zklock.lock;

/**
 * @Author:阿倪
 * @Date: 2019/4/16 21:21
 * @Description:zookeeper实现分布式锁接口
 * @return:
 * @throws:
 */
public interface Lock {
    //获取到锁的资源
    public void getLock();

    // 释放锁
    public void unLock();

}
