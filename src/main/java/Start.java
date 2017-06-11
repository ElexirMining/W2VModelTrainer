import models.StartAppConfiguration;

public class Start {
    /**
     * Точка входа в приложение
     * @param args аргументы которые получает программа при старте
     */
    public static void main(String[] args){
        StartAppConfiguration config = StartAppConfiguration.Instance;
        int currentArg = 0;

        config.pathIn = args[currentArg++];                                                                             //1
       // config.pathOut = args[currentArg++];                                                                            //2

        config.localStart = true;

        Init.StartWork();
    }
}
