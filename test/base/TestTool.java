package base;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestTool {
	private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(    
            new String[]{"applicationContext.xml"});
	
	public static <T> T getBean(String beanName, Class<T> cls){
		 return context.getBean(cls);
	}
}
