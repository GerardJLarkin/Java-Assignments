import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create list of static temperatures
        List<List<Double>> temps = new ArrayList<>();
        List<Double> temp1 = Arrays.asList(0.1,1.2,4.5,9.5,12.6);
        List<Double> temp2 = Arrays.asList(1.0,8.1,9.2,10.3,11.4);
        List<Double> temp3 = Arrays.asList(0.7,1.5,4.8,10.5, 12.9);
        temps.add(temp1);
        temps.add(temp2);
        temps.add(temp3);

        // AssignmentThree QuestionOne
        WeatherStation tempCount = new WeatherStation(null, null, null, null, temps);
        System.out.println(tempCount.countTemperature(9.0));

    }
}
