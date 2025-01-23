package classes

import java.util.concurrent.BlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class TaskThread {
    private val taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val thread: Thread =
        Thread {
            while (true) {
                try {
                    taskQueue.poll(1, TimeUnit.SECONDS)?.run()
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }

    init {
        thread.start()
    }

    fun submitTask(task: Runnable): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        val runnable =
            Runnable {
                try {
                    task.run()
                    future.complete(null)
                } catch (e: Exception) {
                    future.completeExceptionally(e)
                }
            }
        taskQueue.put(runnable)
        return future
    }
}
