package com.configclient.model;

import java.util.List;

/**
 * @Author: luohanwen
 * @Date: 2019/9/20 16:30
 * 放到topic里面的配置dto
 */
public class TopicPropertyDTO {
    private String serviceName;     //配置对应的服务名称
    private String environmentName; //配置对应的环境名称
    private List<PropertyDTO> list; //配置数据

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public List<PropertyDTO> getList() {
        return list;
    }

    public void setList(List<PropertyDTO> list) {
        this.list = list;
    }
}
