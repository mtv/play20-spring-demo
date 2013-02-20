import play.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;

public class Global extends GlobalSettings {

	private AnnotationConfigApplicationContext ctx;

	@Override
	public void onStart(Application app) {
		ctx = new AnnotationConfigApplicationContext();
		ctx.setEnvironment(new StandardEnvironment());
		ctx.register(GlobalContext.class);
		ctx.refresh();
	}

	@Override
	public <T> T getControllerInstance(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

}