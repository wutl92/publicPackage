package sample.sys;

import java.util.concurrent.*;

/**
 * @author wutl
 * @Title:
 * @Package
 * @Description:
 * @date 2021-2-25 11:19
 */
public class SysThread implements ThreadFactory {

    public static ExecutorService threadPool = null;

    static {
        ThreadFactory thread = new SysThread();
        threadPool = new ThreadPoolExecutor(5, 10,
                10000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), thread, new ThreadPoolExecutor.AbortPolicy());
    }

    public static void main(String[] args) {
        threadPool.execute(new DemoThread());
    }
    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(false);
        return thread;
    }
}
