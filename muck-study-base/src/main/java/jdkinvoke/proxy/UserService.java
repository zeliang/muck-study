package jdkinvoke.proxy;

/**
 * 目标对象实现的接口，用JDK来生成代理对象一定要实现一个接口
 * 
 * @author zyb
 * @since 2012-8-9
 * 
 */
public interface UserService {

	/**
	 * 目标方法
	 */
	public abstract void add(int user_id);

}