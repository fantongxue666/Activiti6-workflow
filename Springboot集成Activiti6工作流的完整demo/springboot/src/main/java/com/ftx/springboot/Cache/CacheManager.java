package com.ftx.springboot.Cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName CacheManager.java
 * @Description TODO
 * @createTime 2020年05月27日 11:33:00
 */
@Component
public class CacheManager<T> {

    private Map<String,T> cache=new ConcurrentHashMap<String, T>();

    //从缓存中取数据
    public T get(String key){
       return cache.get(key);
    }

    //添加缓存
    public void addCache(String key,T value){
        cache.put(key,value);
    }

    //根据key删除缓存的一条记录
    public void removeCache(String key){
        if(cache.containsKey(key)){
            cache.remove(key);
        }
    }

    //清空缓存
    public void removeAllCache(){
        cache.clear();
    }

}
