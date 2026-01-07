import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class AsyncParallelDemo {

    public static void main(String[] args) throws Exception {

        int N = 100;

        
        Callable<Integer> sumTask = () -> {
            int sum = 0;
            for (int i = 1; i <= N; i++) {
                sum += i;
            }
            return sum;
        };

        
        ExecutorService executor = Executors.newSingleThreadExecutor();

        System.out.println("Submitting Callable task...");
        Future<Integer> future = executor.submit(sumTask);

        System.out.println("Waiting for result (blocking)...");
        
        int result = future.get();
        System.out.println("Sum from 1 to " + N + " = " + result);

        executor.shutdown();

        int size = 1_000_000;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            numbers.add(i);
        }

        
        long startSeq = System.currentTimeMillis();
        long sumSeq = numbers.stream().mapToLong(Integer::longValue).sum();
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential sum = " + sumSeq + ", time = " + (endSeq - startSeq) + " ms");

        
        long startPar = System.currentTimeMillis();
        long sumPar = numbers.parallelStream().mapToLong(Integer::longValue).sum();
        long endPar = System.currentTimeMillis();
        System.out.println("Parallel sum   = " + sumPar + ", time = " + (endPar - startPar) + " ms");
    }
}
