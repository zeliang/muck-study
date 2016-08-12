package deadlockdemo;

class ThreadTest2 {
	public static void main(String[] args) {
		ThreadDemo4 t1 = new ThreadDemo4(false);
		ThreadDemo4 t2 = new ThreadDemo4(true);
		t1.start();
		t2.start();
	}
}
