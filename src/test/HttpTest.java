import com.configclient.common.Environment;
import com.configclient.util.HttpUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: luohanwen
 * @Date: 2019/9/18 16:15
 */
public class HttpTest {

    public static ApplicationContext ctx;

    @BeforeClass
    public static void init(){
        ctx=new ClassPathXmlApplicationContext("classpath:configCenterContext.xml");
        System.out.println("-----------------");
    }

    @Test
    public void testHtpp(){
        String url="http://127.0.0.1:8080/server/app/property";
        Map param=new HashMap<>();
        param.put("ip","127.0.0.1");
        param.put("environment", Environment.DEV.code);
        param.put("serviceName","DataService");

        try {
            System.out.println(HttpUtil.httpGetData(url,param));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
