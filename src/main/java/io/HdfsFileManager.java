package io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HdfsFileManager extends FileManager {
    FileSystem _fs;
    Path _filePath;

    public HdfsFileManager(String path) {
        super(path);

        _filePath = new Path(_path);

        Configuration config = new Configuration();
        try {
            _fs = FileSystem.get(config);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Метод, отвечающий за проверку наличия пути к файлу
     *
     * @return
     */
    @Override
    public boolean IsPathExists() {
        try {
            _fs.exists(_filePath.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Метод, отвечающий за создание пути к файлу
     */
    @Override
    public void CreatePath() {
        try {
            _fs.create(_filePath.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, отвечающий за создание пустого файла
     */
    @Override
    public void CreateFile() {
        CreatePath();
        try {
            _fs.createNewFile(_filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, отвечающий за запись данных в файл
     *
     * @param text
     */
    @Override
    public void Write(String text) {
        try {
            Write(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, отвечающий за запись данных в файл
     *
     * @param bytes
     */
    @Override
    public void Write(byte[] bytes) {
        try {
            FSDataOutputStream out = _fs.create(_filePath);
            out.write(bytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, отвечающий за чтение данных из файла
     *
     * @return строка с данными
     */
    @Override
    public String ReadString() {
        try {
            FSDataInputStream out = _fs.open(_filePath);
            String result = out.readUTF();
            out.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод, отвечающий за чтение данных из файла
     *
     * @return набор байтов
     */
    @Override
    public byte[] ReadBytes() {
        try {
            FSDataInputStream out = _fs.open(_filePath);
            byte[] result = out.readUTF().getBytes();
            out.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
