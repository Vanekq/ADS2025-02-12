package by.it.group410902.gribach.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // сортировка отрезков по началу и (если равны) по концу
        quickSort(segments, 0, segments.length - 1);


        // Разделяем отрезки на два отдельных массива: начала и концы
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }
        // Отдельно сортируем массив концов отрезков для последующего бинарного поиска
        Arrays.sort(ends);

        // Для каждой точки считаем, сколько отрезков её покрывают
        for (int i = 0; i < m; i++) {
            int x = points[i];
            int left = upperBound(starts, x); // Количество отрезков, начавшихся до или на точке
            int right = lowerBound(ends, x);  // Количество отрезков, закончившихся до точки
            result[i] = left - right;         // Количество покрывающих отрезков
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;   // отдельно сортируем окончания
    }

    // Меняет местами два элемента в массиве отрезков
    private static void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Выбирает медианный элемент из трёх по значению (используется как pivot в quicksort)
    private static int medianOfThree(Segment[] arr, int a, int b, int c) {
        if (arr[a].compareTo(arr[b]) > 0) {
            swap(arr, a, b);
        }
        if (arr[a].compareTo(arr[c]) > 0) {
            swap(arr, a, c);
        }
        if (arr[b].compareTo(arr[c]) > 0) {
            swap(arr, b, c);
        }
        return b; // Возвращает индекс медианного значения
    }

    // Трёхпутевое разбиение массива (Dutch National Flag) с выбранным медианным pivot
    private static int[] partition(Segment[] arr, int low, int high) {
        int mid = low + (high - low) / 2;
        int pivotIndex = medianOfThree(arr, low, mid, high);
        swap(arr, pivotIndex, high);
        Segment pivot = arr[high];

        int i = low;   // указатель на конец области меньших элементов
        int j = high;  // указатель на начало области больших элементов
        int k = low;   // текущий элемент

        while (k <= j) {
            int cmp = arr[k].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, i++, k++);
            } else if (cmp > 0) {
                swap(arr, k, j--);
            } else {
                k++;
            }
        }
        return new int[]{i, j}; // возвращаем границы области, равной pivot;
    }

    // Быстрая сортировка с хвостовой рекурсией
    private static void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition(arr, low, high);
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                quickSort(arr, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;
            } else {
                quickSort(arr, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;
            }
        }
    }
    // upperBound — находит индекс первого элемента > key
    private static int upperBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // lowerBound — находит индекс первого элемента >= key
    private static int lowerBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        // Метод сравнения отрезков: сначала по start, затем по stop
        @Override
        public int compareTo(Object o) {
            Segment b = o instanceof Segment ? (Segment) o : null;
            if (start != b.start) {
                return Integer.compare(start, b.start);
            }
            return Integer.compare(stop, b.stop);
            //подумайте, что должен возвращать компаратор отрезков
        }
    }

}
