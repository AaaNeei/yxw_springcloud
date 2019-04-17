package com.yxw.yxw_zklock.load;

import com.yxw.yxw_zklock.lock.Lock;
import com.yxw.yxw_zklock.lock.impl.YxwZkLock;
import com.yxw.yxw_zklock.lock.impl.ZkAbstractLock;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:阿倪
 * @Date: 2019/4/17 9:58
 * @Description: 游学网负载均衡服务接口实现
 * @return:
 * @throws:
 */
@Service
public class YxwLoadLevelServer {
    Logger logger = LoggerFactory.getLogger(YxwLoadLevelServer.class);
    private static final String PARENT_PATH_LOAD = "/yxw_load";

    public static List<String> listServer = new ArrayList<String>();

    private Lock lock = new YxwZkLock();
    // 请求次数
    private static int count = 1;
    /**
     * 向ZooKeeper注册当前服务器
     */
    protected ZkClient zkClient = null;


    /**
     * 注册服务创建节点
     */
    public void regServer(String path, String ip, String port) {
        zkClient = new ZkClient(ip, 60000, 1000);
        String realPath = PARENT_PATH_LOAD + "/" + path + port;

        if (!zkClient.exists(PARENT_PATH_LOAD)) {
            logger.info("父类节点:" + PARENT_PATH_LOAD + "不存在..准备创建");
            //如果父节点不存在创建父节点
            createParentNode();
        }
        if (zkClient.exists(realPath)) {
            zkClient.delete(realPath);
        }
        zkClient.createEphemeral(realPath, ip + port);
    }

    /**
     * 读取对应ip,port节点
     */
    public void initServer(String path, String ip, String port) {
        zkClient = new ZkClient(ip, 60000, 1000);
        //获取父类下面的所有节点信息
        List<String> childrens = zkClient.getChildren(PARENT_PATH_LOAD);
        try {
            lock.getLock();
            listServer.clear();
            for (String children : childrens) {
                listServer.add((String) zkClient.readData(PARENT_PATH_LOAD + "/" + children));
            }
            zkClient.subscribeChildChanges(PARENT_PATH_LOAD, new IZkChildListener() {
                /**
                 * 监听节点变化信息
                 * @param s
                 * @param
                 * @throws Exception
                 */
                @Override
                public void handleChildChange(String s, List<String> childrens) throws Exception {
                    //如果变化清除原集合
                    listServer.clear();
                    for (String children : childrens) {
                        //重新存入节点信息
                        listServer.add((String) zkClient.readData(PARENT_PATH_LOAD + "/" + children));
                    }

                }
            });
        } catch (Exception e) {
        } finally {
            lock.unLock();
        }

    }


    /**
     * 获取当前server节点信息
     *
     * @return
     */
    public String getServer() {
        String serverName = "";
        try {
            lock.getLock();
            //实现负载均衡
            serverName = listServer.get(count % listServer.size());
            ++count;

        } catch (Exception e) {

        } finally {
            lock.unLock();
        }
        return serverName;
    }


    /**
     * 创建父节点
     */
    private void createParentNode() {
        //创建父类永久节点
        zkClient.createPersistent(PARENT_PATH_LOAD);
        logger.info("创建父类节点:" + PARENT_PATH_LOAD + "..成功");
    }


}
