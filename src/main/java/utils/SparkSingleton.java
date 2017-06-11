package utils;

import models.StartAppConfiguration;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkSingleton {
    /**
     * Use for make JavaSparkContext singleton object
     */
    private static JavaSparkContext sparkContext; //инстанс контекста для Spark

    public static synchronized JavaSparkContext getContext() {
        if(sparkContext == null) {//ленивое создание и возвращение инстанса.
            try {
                SparkConf conf = new SparkConf()
                        .setAppName("Spark user-activity");
                if (StartAppConfiguration.Instance.localStart)
                    conf = conf
                            .setMaster("local[2]")            //local - означает запуск в локальном режиме.
                            .set("spark.driver.host", "localhost");    //это тоже необходимо для локального режима
                sparkContext = new JavaSparkContext(conf);
            } catch (Exception e) {
                throw new RuntimeException("При создании sparkContext произошла ошибка\n" + e.toString());
            }
        }
        return sparkContext;
    }
}
