package com.springmvc.common;

import com.springmvc.model.PropertyDTO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: luohanwen
 * @Date: 2019/9/12 10:27
 */
public class MyPlaceHolder extends PropertyPlaceholderConfigurer {
    //会有定时刷新的功能，也会主动监听mq去刷新，估为了防止并发问题，需要使用读写锁
    private static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    private static Lock readLock=lock.readLock();
    private static Lock writeLock=lock.writeLock();

    //可能出现并发修改
    private static Map<String, String> ctxPropertiesMap=new ConcurrentHashMap<>();

    private static ScheduledExecutorService schedule= Executors.newSingleThreadScheduledExecutor();

    /**
     * 重写读取完配置后的操作，把拿到的值塞到一个map中，后续所有操作都操作这个map
     * 如果改为读取配置中心，这里可以改成发起http请求
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }

        schedule.scheduleAtFixedRate(new PropertyTask(),1,10, TimeUnit.SECONDS);
    }

    public static String getValue(String key){
        String value=null;
        try{
            readLock.lock();
            value=ctxPropertiesMap.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readLock.unlock();
        }
        return value;
    }

    public static void setValue(String key,String value){
        try{
            writeLock.lock();
            ctxPropertiesMap.put(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            writeLock.unlock();
        }

    }

    /**
     * 刷新多个配置
     * @param list
     */
    public static void setProperty(List<PropertyDTO> list){
        if(list==null || list.size()==0){
            return;
        }
        try{
            writeLock.lock();
            for(PropertyDTO propertyDTO:list){
                ctxPropertiesMap.put(propertyDTO.getKey(),propertyDTO.getValue());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            writeLock.unlock();
        }

    }

    /**
     * 该task应该是全部刷新，主动拉取配置
     */
    class PropertyTask implements Runnable{

        @Override
        public void run() {
            //刷新配置两种方式，定时刷新，以及监听mq实时刷新

            //TODO 主动发起http请求，请求到配置中心服务器
            setValue("database.url",Math.random()+":"+Math.random()+":"+Math.random());
            System.out.println("----refresh----");
        }
    }
}
