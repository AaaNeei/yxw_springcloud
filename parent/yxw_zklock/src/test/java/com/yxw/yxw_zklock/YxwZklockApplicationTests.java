package com.yxw.yxw_zklock;

import com.yxw.yxw_zklock.lock.Lock;
import com.yxw.yxw_zklock.lock.impl.YxwZkLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YxwZklockApplicationTests implements Runnable {
    public static int count = 0;

    private Lock lock = new YxwZkLock();


    @Test
    public void contextLoads() {
        for (int i = 0; i < 100; i++) {
            new Thread(new YxwZklockApplicationTests()).start();

        }
    }

    @Override
    public void run() {

        try {
            lock.getLock();
            count++;
            System.out.println("分布式锁控制" + count);
        } catch (Exception e) {
        } finally {
            lock.unLock();
        }

    }
}

