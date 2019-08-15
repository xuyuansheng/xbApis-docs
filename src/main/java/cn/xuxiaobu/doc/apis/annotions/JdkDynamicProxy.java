package cn.xuxiaobu.doc.apis.annotions;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * jdk动态代理
 *
 * @author 020102
 * @date 2019-07-30 13:18
 */
@Slf4j
public class JdkDynamicProxy implements InvocationHandler {

    public JdkDynamicProxy(Annotation target) {
        this.target = target;
    }

    private Annotation target;

    public Apis getProxy() {
        return (Apis) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{Apis.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Optional<Method> methodT = Stream.of(target.getClass().getMethods()).filter(m -> m.getName().equals(method.getName())).findFirst();
        try {
            if (methodT.isPresent()) {
                return methodT.get().invoke(target, args);
            }
        } catch (IllegalAccessException e) {
            log.error("代理执行参数不对  ",e);
            log.info("代理执行参数不对 ,{}",methodT);
        } catch (InvocationTargetException e) {
            log.error("代理执行失败  ",e);
            log.info("代理执行失败 ,{}",methodT);
        }
        return null;
    }
}
