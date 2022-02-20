import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
	    // output from main method lambdaSort
        System.out.println("Output for Q2");
        lambdaSort.main(args);
    }
}
// Question 2.
class lambdaSort<T> { // Class Sort is a generic class with type parameter T
    T[] array; // The array of objects of type T we want to sort
    Comparator<T> comp; // A Comparator instance suitable for comparing objects of type T
    public static void main(String[] args) {
        // An updated comparator for objects of type String in the form of a lambda expression:
        Comparator<String> compString = (a, b) -> {
            if (a.compareTo(b) > 0)
                return 1;
            else
                return 0;
        };
        lambdaSort<String> sortStrings = new lambdaSort<String>();
        // Initialising an array of Strings with 16 unordered elements.
        // Array length must be a power of 2.
        String[] arrayOfStrings = { "Blue", "Yellow", "Almond", "Onyx", "Peach", "Gold",
                "Red", "Melon", "Lava", "Beige", "Aqua", "Lilac", "Capri", "Orange", "Mauve", "Plum" };
        System.out.println("Original String Array: " + Arrays.toString(arrayOfStrings));
        // Sorting the array by calling the sort-method
        sortStrings.sort(arrayOfStrings, compString);
        System.out.println("Sorted String Array: " + Arrays.toString(arrayOfStrings));

        // An updated comparator for objects of type Double in the form of a lambda expression:
        Comparator<Double> compDouble = (a, b) -> {
            if (a.compareTo(b) > 0)
                return 1;
            else
                return 0;
        };
        lambdaSort<Double> sortDoubles = new lambdaSort<Double>();
        // Initialising an array of doubles with 16 unordered elements.
        // Array length must be a power of 2.
        Double[] arrayOfDoubles = { 5.12, 34.37, 48.95, 5.978, 10.521, 12.458,
                1598.72, 42.01, 42.001, 985.7, 476.598, 1574.6987, 95874.12, 0.000159, 658.95, 3.1742 };
        System.out.println("Original Dobule Array: " + Arrays.toString(arrayOfDoubles));
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


