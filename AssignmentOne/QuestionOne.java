import java.util.Arrays;
import java.util.Comparator;

/* Q1. Enhance the code given so that in addition to the array of strings, also an array of numbers of type Double is
sorted, using the given sorting algorithm. Provide a suitable comparator (Comparator instance) for Double numbers.
Your main-method should print both the original and the sorted array of numbers */

/* Q2. Modify the code from Q1 so that the comparators for Strings and Doubles are provided as Lambda Expressions */

/* Q3. Modify code again so that it makes sensible use of parallel (using multithreading) in the body of method
sort(int low, int n), us class Thread explicitly */

public class Main {

    public static void main(String[] args) {
        // Output of main method from class Sort and lambdaSort
        System.out.println("Output for Q1");
        Sort.main(args);
    }
}

// Question 1.
class Sort<T> { // Class Sort is a generic class with type parameter T
    T[] array; // The array of objects of type T we want to sort
    Comparator<T> comp; // A Comparator instance suitable for comparing objects of type T
    public static void main(String[] args) {
        // A comparator for objects of type String:
        Comparator<String> compString = new Comparator<String>() {
            public int compare(String a, String b) {
                if (a.compareTo(b) > 0)
                    return 1;
                else
                    return 0;
            }
        };
        Sort<String> sortStrings = new Sort<String>();
        // Initialising an array of Strings with 16 unordered elements.
        // Array length must be a power of 2.
        String[] arrayOfStrings = { "Blue", "Yellow", "Almond", "Onyx", "Peach", "Gold",
                "Red", "Melon", "Lava", "Beige", "Aqua", "Lilac", "Capri", "Orange", "Mauve", "Plum" };
        System.out.println("Original String Array: " + Arrays.toString(arrayOfStrings));
        // Sorting the array by calling the sort-method
        sortStrings.sort(arrayOfStrings, compString);
        System.out.println("Sorted String Array: " + Arrays.toString(arrayOfStrings));

        // A comparator for objects of type double:
        Comparator<Double> compDouble = new Comparator<Double>() {
            public int compare(Double a, Double b) {
                if (a.compareTo(b) > 0)
                    return 1;
                else
                    return 0;
            }
        };
        Sort<Double> sortDoubles = new Sort<Double>();
        // Initialising an array of doubles with 16 unordered elements.
        // Array length must be a power of 2.
        Double[] arrayOfDoubles = { 5.12, 34.37, 48.95, 5.978, 10.521, 12.458,
                1598.72, 42.01, 42.001, 985.7, 476.598, 1574.6987, 95874.12, 0.000159, 658.95, 3.1742 };
        System.out.println("Original Double Array: " + Arrays.toString(arrayOfDoubles));
        // Sorting the array by calling the sort-method
        sortDoubles.sort(arrayOfDoubles, compDouble);
        System.out.println("Sorted Double Array: " + Arrays.toString(arrayOfDoubles));
    }
    public void sort(T[] array, Comparator<T> comp) { // Array length must be a power of 2
        this.array = array;
        this.comp = comp;
        sort(0, array.length);
    }

    private void sort(int low, int n) {
        if (n > 1) {
            int mid = n >> 1;
            sort(low, mid);
            sort(low + mid, mid);
            combine(low, n, 1);
        }
    }
    private void combine(int low, int n, int st) {
        int m = st << 1;
        if (m < n) {
            combine(low, n, m);
            combine(low + st, n, m);
            for (int i = low + st; i + st < low + n; i += m)
                compareAndSwap(i, i + st);
        } else
            compareAndSwap(low, low + st);
    }
    private void compareAndSwap(int i, int j) {
        if (comp.compare(array[i], array[j]) > 0)
            swap(i, j);
    }
    private void swap(int i, int j) {
        T h = array[i];
        array[i] = array[j];
        array[j] = h;
    }
}
