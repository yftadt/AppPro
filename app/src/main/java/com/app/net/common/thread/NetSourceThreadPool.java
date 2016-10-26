package com.app.net.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetSourceThreadPool {

  private ExecutorService executor;

  private final int MAX_NUM_POOL_SIZE = 3;

  private static NetSourceThreadPool instance;

  private NetSourceThreadPool() {
    PriorityThreadFactory threadFactory = new PriorityThreadFactory("http-data",
        android.os.Process.THREAD_PRIORITY_BACKGROUND);
    executor = Executors.newFixedThreadPool(MAX_NUM_POOL_SIZE, threadFactory);
  }

  public static NetSourceThreadPool getInstance() {
    if (instance == null) {
      instance = new NetSourceThreadPool();
    }
    return instance;
  }

  public void execute(Runnable task) {
    if (executor != null && !executor.isShutdown()) {
      executor.execute(task);
    }
  }

  public void stop() {
    if (executor != null) {
      executor.shutdown();
      executor = null;
    }
  }

}
