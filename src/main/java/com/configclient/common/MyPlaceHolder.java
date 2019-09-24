package com.configclient.common;

import com.alibaba.fastjson.JSON;
import com.configclient.model.PropertyDTO;
import com.configclient.util.HttpUtil;
import com.configclient.util.IPUtil;
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
 *
 * 刷新配置两种方式，定时刷新，以及监听mq实时刷新
 * 初始化时会主动拉取一次
 */
public class MyPlaceHolder extends PropertyPlaceholderConfigurer {
    //会有定时刷新的功能，也会主动监听mq去刷新，估为了防止并发问题，需要使用读写锁
    private static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    private static Lock readLock=lock.readLock();
    private static Lock writeLock=lock.writeLock();

    private static String url; //请求配置中心的地址
    private static String environment; //环境
    private static String serviceName;  //服务名称

    //可能出现并发修改
    private static Map<String, String> ctxPropertiesMap=new ConcurrentHashMap<>();

    private static ScheduledExecutorService schedule= Executors.newSingleThreadScheduledExecutor();

    public MyPlaceHolder(String url,String environment,String serviceName){
        this.url=url;
        this.environment=environment;
        this.serviceName=serviceName;
    }

    /**
     * 重写读取完配置后的操作，把拿到的值塞到一个map中，后续所有操作都操作这个map
     * 如果改为读取配置中心，这里可以改成发起http请求
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        //发起http请求，去读取配置
        Map param=new HashMap<>();
        param.put("ip", IPUtil.getIP());
        param.put("environment", Environment.getCodeByName(environment));
        param.put("serviceName",serviceName);

        try {
            String str=HttpUtil.httpGetData(url,param);
            List<PropertyDTO> list= JSON.parseArray(str,PropertyDTO.class);

            for(PropertyDTO propertyDTO:list){
                props.setProperty(propertyDTO.getKey(),propertyDTO.getValue());
            }

            //调用父类设置，把值塞到占位符
            super.processProperties(beanFactoryToProcess, props);

            setProperty(list);
            //延迟半小时，然后每半小时刷新一次
            schedule.scheduleAtFixedRate(new PropertyTask(),30*60,30*60, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new PropertyException("读取配置异常，服务启动失败",e);
        }

        System.out.println("----------启动加载配置完成---------------");
    }

    /**
     * 判断是否属于当前服务器该读取的配置
     * 由于一个服务可能部署了多台机器，估不需要通过ip来比较，只要服务名，环境名一致，那么就表示当前机器需要更改配置了
     * @return
     */
    public static boolean judgeServerProperty(String environmentName,String serviceName){
        if(environmentName.equalsIgnoreCase(environment) &&
           serviceName.equalsIgnoreCase(MyPlaceHolder.serviceName)){
            return true;
        }
        return false;
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
     * 该task是全部刷新，主动拉取配置
     */
    class PropertyTask implements Runnable{

        @Override
        public void run() {
            // 主动发起http请求，请求到配置中心服务器
            Map param=new HashMap<>();
            param.put("ip", IPUtil.getIP());
            param.put("environment", Environment.getCodeByName(environment));
            param.put("serviceName",serviceName);

            try {
                String str = HttpUtil.httpGetData(url, param);
                List<PropertyDTO> list = JSON.parseArray(str, PropertyDTO.class);
                setProperty(list);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis()+"----refresh----");
        }
    }

}
