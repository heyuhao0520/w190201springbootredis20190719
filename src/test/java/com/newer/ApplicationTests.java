package com.newer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 操作String
     */
    @Test
    public void opsForValue() {
        ValueOperations<String, Object> value = redisTemplate.opsForValue();
        // 存储数据
        value.set("clazz", "W190201J");
        value.set("sessionid", "sessind232242442", 10, TimeUnit.SECONDS);
        // 取数据
        logger.info("clazz:{}", value.get("clazz"));
        logger.info("sessionid:{}", value.get("sessionid"));
        logger.info("value.size{}", value.size("clazz"));
        // 每次递增1
        value.set("count", 1);
        value.increment("count");
        value.increment("count");
        value.increment("count");
        value.increment("count");
        value.increment("count");
        value.increment("count");
        logger.info("count:{}", value.get("count"));

        // 每次按1递减
        value.decrement("count");
        logger.info("count:{}", value.get("count"));

        value.append("clazz", "W20");
        value.append("clazz", "W0E");
        logger.info("clazz:{}", value.get("clazz", 0, 20));
    }

    /**
     * 操作Set
     */
    @Test
    public void opsForSet() {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add("favx", "唱", "跳", "RAP", "篮球");

        logger.info("size:{}", set.size("favx"));

        // 得到所有的成员 遍历set
        logger.info("all:{}", set.members("favx"));

        // 遍历成员
        Cursor<Object> cusor = set.scan("favx", ScanOptions.NONE);
        while (cusor.hasNext()) {
            logger.info("{}", cusor.next());
        }

    }

    /**
     * 操作List
     */
    @Test
    public void opsForList(){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        // 放缓存
        list.leftPush("lastname","胡毛");
        list.leftPush("lastname","胡汉三");
        list.leftPushAll("lastname","胡三毛","胡四毛","胡大姐");

        logger.info("size:{}",list.size("lastname"));
        // 指定失效的时间
        redisTemplate.expire("lastname",50,TimeUnit.SECONDS);

        // 遍历
        for (int i=0;i<list.size("lastname");i++){
            Object v = list.index("lastname", i);
            logger.info("{}",v);
        }
    }

}
