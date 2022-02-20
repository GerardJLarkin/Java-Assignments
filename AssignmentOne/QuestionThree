import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
	    // output of processes below
        System.out.println("Output for Q3");
        threadedLambdaSort.main(args);
    }
}

// Question 3.
class threadedLambdaSort<T> { // Class Sort is a generic class with type parameter T
    T[] array; // The array of objects of type T we want to sort
    Comparator<T> comp; // A Comparator instance suitable for comparing objects of type T
    // main method represents main thread
    public static void main(String[] args) {
        // An updated comparator for objects of type String in the form of a lambda expression:
        Comparator<String> compString = (a, b) -> {
            if (a.compareTo(b) > 0)
                return 1;
            else
                return 0;
        };
        threadedLambdaSort<String> sortStrings = new threadedLambdaSort<String>();
        // Initialising an array of Strings with 16 unordered elements.
        // Array length must be a power of 2.
        String[] arrayOfStrings = { "Blue", "Yellow", "Almond", "Onyx", "Peach", "Gold",
                "Red", "Melon", "Lava", "Beige", "Aqua", "Lilac", "Capri", "Orange", "Mauve", "Plum" };
        System.out.println("Original String Array: " + Arrays.toString(arrayOfStrings));
        // Sorting the array by calling the sort-method
        sortStrings.sort(arrayOfStrings, compString);
        System.out.println("  Sorted String Array: " + Arrays.toString(arrayOfStrings));

        // An updated comparator for objects of type Double in the form of a lambda expression:
        Comparator<Double> compDouble = (a, b) -> {
            if (a.compareTo(b) > 0)
                return 1;
            else
                return 0;
        };
        threadedLambdaSort<Double> sortDoubles = new threadedLambdaSort<Double>();
        // Initialising an array of doubles with 16 unordered elements.
        // Array length must be a power of 2.
        Double[] arrayOfDoubles = { 5.12, 34.37, 48.95, 5.978, 10.521, 12.458,
                1598.72, 42.01, 42.001, 985.7, 476.598, 1574.6987, 95874.12, 0.000159, 658.95, 3.1742 };
        System.out.println("Original Double Array: " + Arrays.toString(arrayOfDoubles));
        // Sorting the array by calling the sort-method
        sortDoubles.sort(arrayOfDoubles, compDouble);
        System.out.println("  Sorted Double Array: " + Arrays.toString(arrayOfDoubles));
    }
    public void sort(T[] array, Comparator<T> comp) { // Array length must be a power of 2
        this.array = array;
        this.comp = comp;
        // this sort method is the one declared as private void sort(int low, int n)
        sort(0, array.length);
    }

    // Method I will update to employ use of multithreading, will comment and understand what the current method for
    // Sorting the arrays does before we rebuild it with a multi-thread process.

    // declare the sort method, use of void indicates we do not expect a return value from this method
    // the data types accepted into this method are integers for all parameters
    private void sort(int low, int n) {
        // work of method (calculation/computation) occurs within the curly braces
        // logical if n (2nd parameter) is greater than 1 then
        /*if (n > 1) {
            // work of method (calculation/computation) occurs within the curly braces
            // declare a new variable of int type called 'mid'
            // utilises a bitwise right shift operator >>, useful to have an understanding of binary representation,
            // e.g if n = 8, in binary that is 00001000 and moved 1 bit to the right gives us 00000100, which is 4
            int mid = n >> 1;
            // method is recursively called in a finite manner based on the second parameter of the method being greater
            // than one, and taking in the value from this newly created 'mid' variable.
            sort(low, mid);
            // method is called again for another round of recursion, this time adding the value from the newly created
            // 'mid' variable to the first parameter of the method
            sort(low + mid, mid);
            // combine method is called, which is similar to the sort method but uses a left bitwise operator
            // need further understanding of why 3rd parameter is hard coded.
            combine(low, n, 1);

        }*/
        // new code that will include multi-thread process
        // multiple threads are created based on the method calls within the original function
        // I updated the conditional n to be greater than 0 as I found when limiting to greater than 1, I
        // wasn't capturing the final recursion and the first 2 elements in the sorted arrays were out of order
        // Exception in thread "Thread-917" java.lang.ArrayIndexOutOfBoundsException: Index 16 out of bounds for length 16
        // I can't figure out how to fix this error without significantly changing the code :/
        if (n > 0) {
            int mid = n >> 1;
            Thread sortTasks1 = new Thread() {
                @Override
                public void run() {
                    sort(mid, low);
                }
            };
            Thread sortTasks2 = new Thread() {
                @Override
                public void run() {
                    sort(low + mid, mid);
                }
            };
            Thread sortTasks3 = new Thread() {
                @Override
                public void run() {
                    combine(low, n, 1);
                }
            };
            // start each thread
            sortTasks1.start();
            sortTasks2.start();
            sortTasks3.start();
            // given that the original function is recursive, it is necessary to allow one thread to finish before
            // we start into the next one. One way java handles threads execution is through the .join() method which
            // blocks other threads until the current thread is finished
            try {
                sortTasks1.join();
                sortTasks2.join();
                sortTasks3.join();
            } catch (Exception e) {
                // Can leave empty
            }
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
