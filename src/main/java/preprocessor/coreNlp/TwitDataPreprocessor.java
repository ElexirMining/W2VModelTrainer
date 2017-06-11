package preprocessor.coreNlp;

import functions.LemmatizeMapPartition;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.codehaus.janino.Java;

import java.io.Serializable;
import java.util.List;

public class TwitDataPreprocessor implements Serializable{
    /**
     * Возвращает RDD с списками лемматизированных\токенехированных твитов
     * @param twits RDD документов(твитов)
     * @return RDD списки очищенных документов
     */
    public static JavaRDD<Row> Lemmatize(JavaRDD<String> twits){
        return twits.map(x -> {
            //TODO: move string manipulation to other class or this.method
            String clearDocText = x.replaceAll("(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?", "")//регулярка для удаленния ссылок
                    .replaceAll("[^A-z ]", "").toLowerCase();//регулярка для удаления всех символов которые не входят в промежуток A-z и приведение к нижнему регистру
            return RowFactory.create(Lemmatizer.Execute(clearDocText));//наполнение списка после очистки в Lemmatizer
        });
    }

    public static Dataset<Row> RemoveStopWords(JavaRDD<Row> twits){
        return StopWordsRemover.Execute(twits);
    }

    /**
     * Возвращает RDD с не пустыми списками очищенных слов из твитов
     * @param twits RDD "грязных" твитов
     * @return списки очищенных твитов
     */
    public static JavaRDD<List<String>> GetWordsFromTwits(JavaRDD<String> twits){
        JavaRDD<Row> lemmatizedRDD = Lemmatize(twits);
        JavaRDD<Row> removeStopWords = RemoveStopWords(lemmatizedRDD).toJavaRDD();
        JavaRDD<List<String>> words = removeStopWords.map(row -> row.getList(0));
        return words.filter(row -> !row.isEmpty());
    }
}