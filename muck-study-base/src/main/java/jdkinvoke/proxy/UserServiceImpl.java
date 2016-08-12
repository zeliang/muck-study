package jdkinvoke.proxy;

/**
 * 目标对象
 * 
 * @author zyb
 * @since 2012-8-9
 * 
 */
public class UserServiceImpl implements UserService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynamic.proxy.UserService#add()
	 */
	public void add(int user_id) {
		System.out.println("add_user_id----->" + user_id);
	}
}