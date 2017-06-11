package preprocessor.coreNlp;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class StopWordsRemover {

    static StructType _schema = new StructType(new StructField[]{
        new StructField("raw", DataTypes.createArrayType(DataTypes.StringType), false, Metadata.empty())
    });

    static org.apache.spark.ml.feature.StopWordsRemover _sw = new org.apache.spark.ml.feature.StopWordsRemover()
        .setInputCol("raw")
        .setOutputCol("filtered");

    static SparkSession _sparkSession = SparkSession.builder()
        .config("spark.sql.warehouse.dir", "spark-warehouse")
        .getOrCreate();

    public static Dataset<Row> Execute(JavaRDD<Row> rows){
        Dataset<Row> dataset =  _sparkSession.createDataFrame(rows.collect(), _schema);
        return _sw.transform(dataset).select("filtered");                                                                //производим удаление стоп слов и возвращаем колонку без стопслов
    }
}
