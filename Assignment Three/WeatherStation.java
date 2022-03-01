import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Create a class WeatherStation
class WeatherStation {
    // Add three attributes, city, measurements, stations
    // each city where measurements are being taken is given as a string variable in a list
    public String city;
    // We are asked to provide a list, but in reality it should be a list of lists, as any dataset does not provide
    // measurements by themselves. Time needs to be linked with each temperature measurement, with each nested list
    // given as a key : value pair.
    public static List<Integer> timeRange;
    public static List<Double> tempRange;
    // add static list of temperatures
    public static List<List<Double>> temps;
    // a static list of all weather stations, the question does not specify where each weather station is located,
    // again in reality each weather should be connected to a geographical location and the only item given in the
    // question is city. Each weather station will be connected to a city, we then need to consider if a city has one,
    // two, or many weather stations connected to it. Therefore, this list will contain nested lists where each nested
    // list is a pair with city and weather station number as a key : value pair
    static String station;

    // Create a class Measurements, this class gathers the measurements from each weather station in each city
    // We can set the type of the time measurements to integer and temperature to double as specified in the question
    // but neither the time nor the temperature is a standalone value, they must be linked together. How we do this
    // depends on how the data is presented. Rather than hard coding pairs of times/measurements I will create a pair
    // of lists, one containing time (its specified we have to have an integer value for time) with a measurement each
    // hour, and one containing temperatures, with a temperature measured at each time point.
    // Measurements are also unique to each weather station, so we need to link these back to their stations

    // Didn't create a class measurements but I sort of did what was described above.

    WeatherStation(String city, String station, List<Integer> timeRange, List<Double> tempRange, List<List<Double>> temps) {
        this.city = city;
        this.station = station;
        this.timeRange = timeRange;
        this.tempRange = tempRange;
        // Assignment 3 added new static temps list to WeatherStation parameters
        this.temps = temps;
    }

    // Add method maxTemperature, with parameters startTime & endTime
    public Double maxTemperature(int startTime, int endTime) {
        // https://stackoverflow.com/questions/39962796/creating-map-composed-of-2-lists-using-stream-collect-in-java/39963821
        Map<Integer, Double> measurements = IntStream.range(0, Math.min(timeRange.size(), tempRange.size()))
                .boxed()
                .collect(Collectors.toMap(timeRange::get, tempRange::get));
        // use java8 streams here
        // https://stackoverflow.com/questions/42060294/using-java8-stream-to-find-the-highest-values-from-map
        double maxTemp = measurements.entrySet().stream()
                .filter(t -> t.getKey().intValue() >= startTime && t.getKey().intValue() <= endTime)
                .max(Map.Entry.comparingByValue()).get().getValue();
        return maxTemp;
    }

    public LinkedHashMap<Double, Integer> countTemperatures(Double t1, Double t2, Integer r) {
        // Use Java 8 parallel streams here: ultimately didn't get to use parallel streams
        double t1Count = tempRange.stream()
                // Lambda Expression
                .filter(t -> t >= (t1 - r) && t <= (t1 + r))
                // Reduce
                .count();

        double t2Count = tempRange.stream()
                // Lambda Expression
                .filter(t -> t >= (t2 - r) && t <= (t2 + r))
                // Reduce
                .count();

        LinkedHashMap<Double, Integer> tempCounts = new LinkedHashMap<Double, Integer>();

        tempCounts.put(t1, (int)t1Count);
        tempCounts.put(t2, (int)t2Count);

        return tempCounts;
    }

    // It was requested to create this as a static method, however as I create my temperatures dynamically at each
    // run time in the previous assignment I ended up making a static nested lists of temperatures. Not ideal to be honest.
    public static List<Tuple2<Double, Integer>> countTemperature(Double t) throws Exception {
        System.setProperty("hadoop.home.dir", "C:/winutils");
        SparkConf sparkConf = new SparkConf()
                .setAppName("TempCount")
                .setMaster("local[4]").set("spark.executor.memory", "1g");

        JavaSparkContext ctx = new JavaSparkContext((sparkConf));

        List<Double> flatTempRanges = temps.stream().flatMap(Collection::stream).collect(Collectors.toList());
        JavaRDD<Double> tempRange = ctx.parallelize(flatTempRanges);
        JavaRDD<Double> filteredTempRange = tempRange.filter(k -> (k >= t-1 && k <= t+1));
        JavaPairRDD<Double, Integer> temperatures = filteredTempRange.mapToPair((Double d) -> new Tuple2<Double, Integer>(d, 1));
        JavaPairRDD<Double, Integer> counts = temperatures.reduceByKey((Integer i1, Integer i2) -> i1 + i2);
        List<Tuple2<Double, Integer>> tempCount = counts.collect();

        ctx.stop();
        ctx.close();

        return tempCount;

    }
}
