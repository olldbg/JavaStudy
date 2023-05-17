package com.liminjun.test.virtualThread.controller;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.SuspendableCallable;
import com.liminjun.test.someTask.BenchMarkTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private ExecutorService virtualThreadExecutorService = Executors.newVirtualThreadPerTaskExecutor();

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            4,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100000),
            new ThreadFactory() {
                AtomicInteger idx = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "test-thread-" + idx.getAndAdd(1));
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());

    private String runTask(){
        return String.valueOf(BenchMarkTask.calFibonacci(10000));
//        return BenchMarkTask.httpRequest("http://127.0.0.1");
    }

    /**
     * jdk虚拟线程跑一个任务
     *
     * @return {@link String}
     */
    @GetMapping("/virtualThread")
    public String virtualThread() throws ExecutionException, InterruptedException {
        log.info("invoke using virtualThread");
        Future<String> stringFuture = virtualThreadExecutorService.submit(() -> {
            log.info("inner invoke using virtualThread");
            return runTask();
        });
        return stringFuture.get();
    }

    /**
     * 仅仅是普通的线程池
     *
     * @return {@link String}
     */
    @GetMapping("/normalThreadPool")
    public String normalThreadPool() throws ExecutionException, InterruptedException {
        log.info("invoke using normalThreadPool");
        Future<String> stringFuture = threadPoolExecutor.submit(()->{
            log.info("inner invoke using normalThreadPool");
            return runTask();
        });
        return stringFuture.get();
    }

    /**
     * quasar纤程
     *
     * @return {@link String}
     */
    @GetMapping("/quasar")
    public String quasar() throws ExecutionException, InterruptedException {
        log.info("invoke using quasar");
        Fiber<String> fiber = new Fiber<>((SuspendableCallable<String>) () -> {
            return runTask();
        });
        fiber.start();
        return fiber.get();
    }

}
