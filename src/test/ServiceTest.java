import com.springmvc.common.MyPlaceHolder;
import com.springmvc.service.DataService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.MessageListener;
import java.util.concurrent.Executors;

/**
 * @Author: luohanwen
 * @Date: 2019/9/11 16:52
 */
public class ServiceTest {

    public static ApplicationContext ctx;
    public static DataService dataService;

    @BeforeClass
    public static void init(){
        ctx=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        dataService=ctx.getBean(DataService.class);
        System.out.println("-----------------");
    }

    @Test
    public void test(){
        dataService.testData();
    }

    @Test
    public void testGetProperty(){
        System.out.println(MyPlaceHolder.getValue("database.url"));
    }

    @Test
    public void testSetProperty(){
        MyPlaceHolder.setValue("database.url","aslkdfjklsjdklfklsajfkljfd");
        System.out.println(MyPlaceHolder.getValue("database.url"));
    }

    @Test
    public void testScheduleRefresh(){
        for(int i=0;i<3;i++){
            System.out.println(MyPlaceHolder.getValue("database.url"));
            try{
                Thread.sleep(10*1000);
            }catch (Exception e){
               e.printStackTrace();
            }
        }
    }


    @Test
    public void testMq(){
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
