package com.yxw.yxw_zklock.lock.impl;

import com.yxw.yxw_zklock.lock.Lock;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author:阿倪
 * @Date: 2019/4/16 21:23
 * @Description:
 * @return:
 * @throws:
 */
@Service
public abstract class ZkAbstractLock implements Lock {

    Logger logger = LoggerFactory.getLogger(ZkAbstractLock.class);

    @Value("{yxw.zookeeper.addrs}")
    private String addrs;
    protected ZkClient zkClient = new ZkClient(addrs);
    protected static final String PATH_LOCK = "/lock";


    @Override
    public void getLock() {
        if (tryLock()) {
            logger.info(zkClient.toString()+"获取到锁");
        } else {
            // 等待
            logger.info(zkClient.toString()+"等待锁");
            waitLock();

            // 重新获取锁资源
            getLock();
            logger.info(zkClient.toString()+"重新获取锁");
        }


    }

    @Override
    public void unLock() {
        if (zkClient != null) {
            try {
                zkClient.close();
                logger.info("zkClient关闭,锁释放...");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }

    /**
     * 锁等待
     */
    abstract void waitLock();

    /**
     * 锁获取
     */
    abstract Boolean tryLock();
}
