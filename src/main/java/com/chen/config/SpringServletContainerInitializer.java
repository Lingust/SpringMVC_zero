package com.chen.config;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.WebApplicationInitializer;

/**
 * 容器启动时会自动扫描当前服务中此接口的实现类，并调用onStratup方法
 * 其参数Set<Class<?>> c 通过注解HandlesTypes自动注入
 * 此注解会扫描项目中所有的xxx.class实现类，并将其全部注入set
 * 所以此注解内的WebApp...类才是重点，只要把servlet,filter,listener
 * 写入到该接口的实现类即可
 * @author Chenxf
 *
 */
@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {

	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		// TODO Auto-generated method stub
		List<WebApplicationInitializer> initializers = 
				new LinkedList<WebApplicationInitializer>();
		
		if(c != null) {
			for(Class<?> waiClass : c) {
				if(!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
						WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
					try {
						initializers.add((WebApplicationInitializer) waiClass.newInstance());
					} catch (Throwable ex) {
						throw new ServletException("fail");
					}
				}
			}
		}
		
		if (initializers.isEmpty()) {
			ctx.log("no spring webapplicatoninitializer types detected");
			return ;
		}
		
		AnnotationAwareOrderComparator.sort(initializers);
		ctx.log("Spring WebApplicationInitializers detected on classpath: " + initializers);
		
		for (WebApplicationInitializer initializer : initializers) {
			initializer.onStartup(ctx);
		}
	}

}
