package deadlockdemo;

public class ThreadDemo4 extends Thread{  
    boolean flag ;  
    ThreadDemo4(boolean flag){  
        this.flag = flag;  
    }  
    @Override  
    public void run() {  
        //两个线程来回切换  
        if(flag){  
            while(true){  
                //同步嵌套  
                //locka嵌套lockb  
                synchronized (Lock.locka) {  
                    System.out.println(Thread.currentThread().getName()+"...if locka");  
                    synchronized (Lock.lockb) {  
                        System.out.println(Thread.currentThread().getName()+"...else lockb");  
                    }  
                }  
            }  
        }else{    
            while(true){  
                //同步嵌套  
                //lockb嵌套locka  
                synchronized (Lock.lockb) {   
                    System.out.println(Thread.currentThread().getName()+"...if lockb");  
                    synchronized (Lock.locka) {  
                        System.out.println(Thread.currentThread().getName()+"...else lockb");  
                    }  
                }     
            }  
        }  
    }  
}  