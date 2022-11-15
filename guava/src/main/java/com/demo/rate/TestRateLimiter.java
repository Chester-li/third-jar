package com.demo.rate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lichaoshuai
 * Created on 2022-11-07
 */
public class TestRateLimiter {

    private static ExecutorService executorService = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(10);
        long number = 0;
        while (true) {
            if (rateLimiter.tryAcquire()) {
                executorService.submit(new MyThread(number++, System.currentTimeMillis()));
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class MyThread extends Thread {
        private long number;
        private long time;

        @Override
        public void run() {
            System.out.println("acquire success, number : " + number + ", time : " + time);
        }
    }
}
