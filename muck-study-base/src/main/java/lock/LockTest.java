package lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {
	public static void main(String[] args) {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		ReentrantLock lock1 = new ReentrantLock();
	}
}
