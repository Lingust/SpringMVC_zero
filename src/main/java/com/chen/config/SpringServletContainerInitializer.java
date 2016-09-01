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
 * ��������ʱ���Զ�ɨ�赱ǰ�����д˽ӿڵ�ʵ���࣬������onStratup����
 * �����Set<Class<?>> c ͨ��ע��HandlesTypes�Զ�ע��
 * ��ע���ɨ����Ŀ�����е�xxx.classʵ���࣬������ȫ��ע��set
 * ���Դ�ע���ڵ�WebApp...������ص㣬ֻҪ��servlet,filter,listener
 * д�뵽�ýӿڵ�ʵ���༴��
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
