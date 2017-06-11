package io;

public class FileManagerFactory {
    public static FileManager CreateByFS(String path, FileSystemTypeEnum fileSystemType){
        switch (fileSystemType){
            case WindowsFS:
                return new WindowsFileManager(path);
            case HDFS:
                return new HdfsFileManager(path);
            default:
                return null;
        }
    }

    public static FileManager CreateByCurrentFS(String path){
        FileSystemTypeEnum currentFS = FSConfiguration.Instance.FileSystemType;
        return CreateByFS(path, currentFS);
    }
}
