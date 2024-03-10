package edu.ucsd.cse110.successorator.util;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;



public class LiveDataTestUtil {

    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final AtomicReference<T> data = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        // Ensure the observation is done on the main thread
        new Handler(Looper.getMainLooper()).post(() -> {
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(@Nullable T t) {
                    data.set(t);
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            };
            liveData.observeForever(observer);
        });

        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new IllegalStateException("LiveData value was never set.");
        }

        return data.get();
    }
}


