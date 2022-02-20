import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // Trial data for time range (24hrs) and temperature range (one measurement each hour)
        // List of cities
        List<String> cities = Arrays.asList("Dublin", "Limerick", "Mexico", "Bali", "Vilnius");
        // List of stations in each city
        List<String> stations = Arrays.asList("stationOne", "stationTwo", "stationThree");
        // List of times temperatures were measured
        List<Integer> timeRange = IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList());
        // Random list of temperatures will be created at each instantiation of an object of the WeatherStation class,
        // an empty nested list is being created here that we can append our lists of temps to.
        List<List<Double>> tempRanges = new ArrayList<>();

        //Question 1:
        // Set start time and end time between 6 and 18 o'clock.
        Integer startTime = 6;
        Integer endTime = 18;

        // Instantiate a new object of WeatherStation class
        for (int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            for (int j = 0; j < stations.size(); j++) {
                String station = stations.get(j);
                List<Double> tempRange = new Random().doubles(timeRange.size(), -10, 30)
                        .boxed()
                        .collect(Collectors.toList());
                WeatherStation weatherCityStation = new WeatherStation(city, station, timeRange, tempRange);
                // Get maximum temperature for each weather station in each city
                System.out.println("For " + station + " in " + city + ", the maximum temperature between " + startTime + " and "
                        + endTime + " o'clock is: " + weatherCityStation.maxTemperature(startTime, endTime));
                tempRanges.add(tempRange);
            }

        }

        // Question 2:
        // Print out the count of temperatures within the specified range
        double t1 = 21.5;
        double t2 = 5.1;
        int r = 3;

        // flatten nested list of temperatures
        List<Double> flatTempRanges = tempRanges.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // Instantiate a new object of WeatherStation class
        WeatherStation tempCounts = new WeatherStation(null, null, timeRange, flatTempRanges);
        System.out.println("Temperature One: "+t1+" degrees Celsius, in the range +/- "+r+" was counted "
                +tempCounts.countTemperatures(t1, t2, r).values().toArray()[0]+" times.");
        System.out.println("Temperature Two: "+t2+" degrees Celsius, in the range +/- "+r+" was counted "
                +tempCounts.countTemperatures(t1, t2, r).values().toArray()[1]+" times.");
    }
}

