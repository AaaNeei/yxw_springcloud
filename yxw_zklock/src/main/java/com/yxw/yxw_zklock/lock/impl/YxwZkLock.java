package com.yxw.yxw_zklock.lock.impl;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

/**
 * @Author:阿倪
 * @Date: 2019/4/16 21:36
 * @Description:
 * @return:
 * @throws:
 */
public class YxwZkLock extends ZkAbstractLock {
    private CountDownLatch countDownLatch = null;


    @Override
    void waitLock() {

        IZkDataListener iZkDataListener = new IZkDataListener() {
            /**
             * 节点变化监听
             * @param s
             * @param o
             * @throws Exception
             */
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            /**
             * 节点删除监听
             * @param s
             * @throws Exception
             */
            @Override
            public void handleDataDeleted(String s) throws Exception {
                if (countDownLatch != null) {
                    //如果监听到节点删除,就可以释放等待,去获取锁
                    countDownLatch.countDown();
                }
            }
        };
        /**
         * 向zk注册监听
         */
        zkClient.subscribeDataChanges(PATH_LOCK, iZkDataListener);

        if (zkClient.exists(PATH_LOCK)) {
            //如果该节点存在
            countDownLatch = new CountDownLatch(1);
            //创建countDownLatch实现线程等待
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /**
         *等待结束删除监听
         */
        zkClient.unsubscribeDataChanges(PATH_LOCK, iZkDataListener);
    }

    @Override
    Boolean tryLock() {

        try {
            zkClient.createEphemeral(PATH_LOCK);
            logger.info(zkClient.toString() + "创建节点成功,获取到锁");
            return true;
        } catch (Exception e) {
            logger.info(zkClient.toString() + "节点存在,创建失败,获取到锁失败");
            return false;
        }
    }
}
