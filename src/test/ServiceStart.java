import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: luohanwen
 * @Date: 2019/9/20 16:08
 */
public class ServiceStart {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:configCenterContext.xml");
        ctx.start();

        System.out.println("-------------服务启动成功--------------");
        //等待，模拟程序一直启动
        synchronized (ServiceStart.class){
            try {
                ServiceStart.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
