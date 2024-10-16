import java.math.BigInteger;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Створюємо ConcurrentHashMap для зберігання результатів
        ConcurrentHashMap<Integer, BigInteger> factorialsMap = new ConcurrentHashMap<>();

        // Створюємо пул потоків
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Створюємо список задач Callable
        int[] numbers = {5, 7, 10, 12, 15};
        Future<BigInteger>[] futures = new Future[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            final int number = numbers[i];
            futures[i] = executor.submit(new Callable<BigInteger>() {
                @Override
                public BigInteger call() throws InterruptedException {
                    // Емулюємо затримку для кожного потоку
                    Thread.sleep(1000);

                    // Обчислюємо факторіал
                    BigInteger factorial = factorial(number);
                    // Зберігаємо результат у мапі
                    factorialsMap.put(number, factorial);
                    return factorial;
                }
            });
        }

        // Скасовуємо одну із задач для демонстрації
        futures[2].cancel(true);

        // Збираємо результати
        for (int i = 0; i < futures.length; i++) {
            Future<BigInteger> future = futures[i];
            if (!future.isCancelled()) {
                // Виводимо результат, якщо задачу не було скасовано
                System.out.println("Факторіал числа " + numbers[i] + " = " + future.get());
            } else {
                System.out.println("Задачу було скасовано для числа " + numbers[i]);
            }
        }

        // Завершуємо роботу з executor
        executor.shutdown();
    }

    // Метод для обчислення факторіала
    public static BigInteger factorial(int number) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= number; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
