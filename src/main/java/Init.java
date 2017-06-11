import io.FSConfiguration;
import io.FileSystemTypeEnum;
import models.StartAppConfiguration;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import preprocessor.coreNlp.TwitDataPreprocessor;
import utils.SparkSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Init {
    static SparkSession spark;
    static JavaSparkContext sc;
    static StructType schema;

    public static void StartWork(){
        StartAppConfiguration config = StartAppConfiguration.Instance;
        FSConfiguration.Instance.FileSystemType = config.localStart ? FileSystemTypeEnum.WindowsFS : FileSystemTypeEnum.HDFS;

        schema = new StructType(new StructField[]{
                new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });

        sc = SparkSingleton.getContext();
        spark = SparkSession.builder()//сессия
                .config("spark.sql.warehouse.dir", "spark-warehouse")
                .getOrCreate();

        List<List<String>> trainWords = new ArrayList<>();

        for (String file: config.pathIn.split(","))
        {
            JavaRDD<String> trainTextRDD = sc.textFile(file);
            trainWords.addAll(TwitDataPreprocessor.GetWordsFromTwits(trainTextRDD).collect());//получаем слова
        }

        System.out.println("");

        JavaRDD<Row> trainData = sc.parallelize(trainWords).map(x -> RowFactory.create(x));

        Word2Vec word2Vec = new Word2Vec()
                .setInputCol("text")
                .setOutputCol("result")
                .setMinCount(0);

        Dataset<Row> trainDataFrame = spark.createDataFrame(trainData.collect(), schema);

        Word2VecModel w2vModel = word2Vec.fit(trainDataFrame);

        try {
            //FileUtils.deleteDirectory(new File(config.pathOut));
            w2vModel.save("ВВЕСТИ_ИМЯ_СЮДА");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
