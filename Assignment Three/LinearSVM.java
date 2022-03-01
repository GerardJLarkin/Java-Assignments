import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import java.util.Arrays;

// Using the default parameters for the model over 100 iterations and combining the 3 datasets
// into 1 gets me an average evaluation score ~0.61 auROC over 5 runs
// Using just the amazon data I get ~0.64 auROC over 5 runs
// Using just the imbd data I get ~0.56 auROC over 5 runs
// Using just the yelp data I get ~0.59 auROC over 5 runs
// I have tried playing around with the model hyperparameters, but I can't seem to get a better accuracy
// As an alternative I searched for non-linear SVM's in Spark and they don't appear to exist.
class LinearSVM {
    // Get data for creating and training model
    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "C:/winutils");
        SparkConf sparkConf = new SparkConf()
                .setAppName("TempCount")
                .setMaster("local[4]").set("spark.executor.memory", "1g");

        JavaSparkContext ctx = new JavaSparkContext((sparkConf));

        final HashingTF tf = new HashingTF(100);

        // Import data to train and test model on
        JavaRDD<String> dataOne = ctx.textFile("C:\\Users\\Ger\\Desktop\\College\\CT5150\\assignmentThree\\amazon_cells_labelled.txt", 1);
        JavaRDD<String> dataTwo = ctx.textFile("C:\\Users\\Ger\\Desktop\\College\\CT5150\\assignmentThree\\imdb_labelled.txt", 1);
        JavaRDD<String> dataThree = ctx.textFile("C:\\Users\\Ger\\Desktop\\College\\CT5150\\assignmentThree\\yelp_labelled.txt", 1);

        // Combine three data files into one overall data file
        JavaRDD<String> data = ctx.union(dataOne, dataTwo, dataThree);

        // Create pairs for each instance of data, one element of the pair is the label
        // and the other elements is the hashed vector of the text to be analysed.
        JavaRDD<LabeledPoint> sentimentAnalysis = data
                // Flatten data set by splitting on newline character
                .flatMap(s -> Arrays.asList(s.split("\n")).iterator())
                // Split each instance (sentiment text and label) on tab character
                .map(s -> Arrays.asList(s.split("\\t")))
                // Create a tuple for each sentiment text sentence and its label
                .map(y -> new Tuple2<Integer, String>(Integer.parseInt(y.get(1)), y.get(0)))
                // Transform the sentiment sentence to be analysed into vector form
                .map(y -> new LabeledPoint(y._1, tf.transform(Arrays.asList(y._2.split(" ")))));

        // Randomly split data 60% for training, 40% for testing
        JavaRDD<LabeledPoint>[] splitData = sentimentAnalysis.randomSplit(new double[] {0.6, 0.4});
        // Create training data from first index in splitData RDD
        JavaRDD<LabeledPoint> trainingData = splitData[0];
        // Create testing data from second index in splitData RDD0
        JavaRDD<LabeledPoint> testingData = splitData[1];
        // Cache the training data as the data will need to be revisited multiple times
        trainingData.cache();
        // Create Support Vector Machine model (default setting L2 regularization)
        SVMModel model = SVMWithSGD.train(trainingData.rdd(), 1000, 1.0, 1.0, 1.0);

        /*// Create comparative customized model with L1 regularization, reg parameter = 0.2
        SVMWithSGD svmAlg = new SVMWithSGD();
        svmAlg.optimizer()
                .setNumIterations(1000)
                .setRegParam(0.9)
                .setUpdater(new L1Updater());
        SVMModel modelL1 = svmAlg.run(trainingData.rdd());*/

        // Select subset of test data for evaluation
        JavaRDD<LabeledPoint> testSubset = testingData.sample(false, 0.01);

        // Print out subset of testing data in Vector form
        for(LabeledPoint s:testSubset.collect())
            System.out.println(s);

        // Test Model using subset of test data
        JavaRDD<Tuple2<Object, Object>> scoreAndLabelSubset = testSubset.map(s -> new Tuple2<>(model.predict(s.features()),
                s.label()));

        // View score and label of subset test data
        for(Tuple2 t:scoreAndLabelSubset.collect())
            System.out.println(t);

        // Get evaluation metrics of default L2 reg model on total testingData set
        JavaRDD<Tuple2<Object, Object>> scoreAndLabelTestset = testingData.map(s -> new Tuple2<>(model.predict(s.features()),
                s.label()));
        BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabelTestset));
        double auROC = metrics.areaUnderROC();

        System.out.println("L2: Area under ROC = " + auROC);

    }
}
