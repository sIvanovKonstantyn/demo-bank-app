package com.home.demos;

import com.home.demos.deposit.dto.DepositCommandResult;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class TestFutureWithTimeout {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Object> f = executor.submit(
                () -> {
                    int i = 0;
                    while (true) {
                        System.out.println("i: " + i++);
                        Thread.sleep(1000);
                    }
                }

        );

        try {
            f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            f.cancel(true);
        } finally {
            executor.shutdownNow();
        }
    }

    private static ScheduledFuture<DepositCommandResult> createTakeResultTask(ScheduledExecutorService executor, String requestID) {
        return executor.schedule(
                () -> takeCommandResult(requestID),
                1,
                TimeUnit.SECONDS
        );
    }

    private static DepositCommandResult takeCommandResult(String requestID) {
        System.out.printf("%s: find result for ID %s in url:%s%n", LocalDateTime.now(), requestID, "test url");

//        DepositCommandResult foundResult = new DepositCommandResult();
        throw new RuntimeException("SOME ERROR THERE");
//        System.out.printf("%s: found result: %s%n", LocalDateTime.now(), foundResult);

//        return foundResult;
    }
}
