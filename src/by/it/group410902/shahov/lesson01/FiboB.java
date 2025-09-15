package by.it.group410902.shahov.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }

        // Создаем массив для хранения промежуточных результатов
        BigInteger[] fib = new BigInteger[n + 1];

        // Инициализируем начальные значения
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        // Заполняем массив значениями чисел Фибоначчи
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        // Возвращаем результат
        return fib[n];
    }

}

