
import services.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "controllers" })
public class GlobalContext {
	
	@Bean
	public HelloService helloService() {
		return new HelloService();
	}
}