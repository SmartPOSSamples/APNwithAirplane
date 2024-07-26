package com.cloudpos.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;


/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String)} read file</li>
 * <li>{@link #writeFile(String, String, boolean)} write file</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();
    public final static String FILE_EXTENSION_SEPARATOR = ".";



    /**
     * read file
     * 
     * @param filePath
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static byte[] readFile(String filePath){
        File file = new File(filePath);
        return readFile(file);
    }

    /**
     * read file
     *
     * @param file
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static byte[] readFile(File file){
        if (file == null || !file.isFile()) {
            return null;
        }
        byte [] contentBuffer = null ;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            contentBuffer = new byte[in.available()];
            in.read(contentBuffer);
            return contentBuffer;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * read file
     * 
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    /**
     * Get the specified file name
     * @param imagePath
     * @param charsets : indication filter and directed to the referenced destination ， example {"PNG","JPG"}
     * @return 
     * 
     * */
    public static List<String> getFileListName(String imagePath,String[] charsets){
    	
    	boolean isExist = isDirectoryExist(imagePath);
    	if(!isExist){
    		return null;
    	}
    	List<String> list = new ArrayList<String>();
    	File dirs = new File(imagePath);
    	for(String s:dirs.list()){
			String [] strs = s.split("\\.");
			if(strs.length < 2){
				continue;
			}else{
				for(String charset : charsets){
					if(strs[1].toUpperCase().equals(charset)){
						list.add(s);
						break;
					}
				}
			} 
		}
		return list;
    }

    /**
     * write file
     * 
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else reset content of file and write into it
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     * 
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file
     * 
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }


//    /**
//     * write file
//     *
//     * @param file the file to be opened for writing.
//     * @param stream the input stream
//     * @param md Computes and returns the final hash value for this
//     * @return return file's hash.
//     * @throws RuntimeException if an error occurs while operator FileOutputStream
//     */
//    public static String writeFile(File file, InputStream stream, MessageDigest md) {
//        OutputStream o = null;
//        try {
//            makeDirs(file.getAbsolutePath());
//            if(file.exists()){
//                if(file.length() != 0){
//                    byte[] buffer = readFile(file);
//                    md.update(buffer, 0, buffer.length);
//                }
//            }
//            o = new FileOutputStream(file, true);
//            byte data[] = new byte[1024];
//            int length = -1;
//            while ((length = stream.read(data)) != -1) {
//                o.write(data, 0, length);
//                md.update(data, 0, length);
//            }
//            o.flush();
//            return ByteConvert.getBestString(md.digest());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("FileNotFoundException occurred. ", e);
//        } catch (IOException e) {
//            throw new RuntimeException("IOException occurred. ", e);
//        } finally {
//            if (o != null) {
//                try {
//                    o.close();
//                    stream.close();
//                } catch (IOException e) {
//                    throw new RuntimeException("IOException occurred. ", e);
//                }
//            }
//        }
//    }
    /**
     * write file
     * 
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * copy file
     * 
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }
    
    public static boolean copyByStream(InputStream inputStream,OutputStream outputStream){
		try {
			byte[] bs = new byte[1024 * 4];
			int len;
			while((len = inputStream.read(bs)) != -1){
				outputStream.write(bs, 0, len);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
    
    @SuppressLint("WorldReadableFiles")
    public static void copy2Local(String zipFilePath, Context host) {
        try {
            InputStream inputStream = host.getAssets().open(zipFilePath);
            FileOutputStream outputStream = host.openFileOutput(zipFilePath, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
            copyByStream(inputStream, outputStream);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * copy file
     * 
     * @param sourceDirsPath
     * @param destDirsPath
     * @return
     */
    public static boolean copyDirs(String sourceDirsPath, String destDirsPath) {
    	boolean isExist = isDirectoryExist(sourceDirsPath);
    	if(isExist){
    		File files = new File(sourceDirsPath);
    		if(files.list() == null){
    			return false;
    		}
    		for(String s:files.list()){
    			String [] strs = s.split("\\.");
    			if(strs.length < 2){
    				continue;
    			}else if(strs[1].toUpperCase().equals("PNG")||strs[1].toUpperCase().equals("JPG")){
    				String outPath = sourceDirsPath + s;
    				String inputPath = destDirsPath + s;
    				copyFile(outPath, inputPath);
    			}
    		}
    		return true;
    	}
    	return false;
    }

    /**
     * read file to string list, a element of list is a line
     * 
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     * 
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * 
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     * 
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }


    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     * 
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {@link FileUtil#getFolderName(String)} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }
    /**
     * Indicates if this file represents a file on the underlying file system.
     * 
     * @param filePath
     * @return
     */
    public static boolean isDirectoryExist(String filePath) {
        File file = new File(filePath);
        return file.isDirectory();
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     * 
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }
    /**
     * 删除文件夹
     *
     * @param folder
     *            String 文件夹路径及名称 如c:/fqf
     * @return boolean
     */
    public static void delFolder(File folder) {
        File parentfile = folder.getParentFile();
        if (!parentfile.exists()) {
            parentfile.mkdirs();
        }
        String folderPath = folder.getPath();
        deleteFile(folderPath);
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     * 
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
    
 // 读文件在./data/data/com.tt/files/下面
    
    public static Reader getReaderFromFiles(Context host, String fileName) {
        BufferedReader bufReader = null;
        try {
            FileInputStream fin = host.openFileInput(fileName);
            if(fin == null){
                return null;
            }
            int length = fin.available();
            byte[] buffer = new byte[length];
            int verify = fin.read(buffer);
            if(verify != length){
                return null;
            }
//          res = EncodingUtils.getString(buffer, "UTF-8");
            InputStream is = new ByteArrayInputStream(buffer);
            InputStreamReader inputReader = new InputStreamReader(is);
            bufReader = new BufferedReader(inputReader);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
            bufReader = null;
        }
        return bufReader;
    }
    
    
    /**
     * @param :String fileName ,
     * @param :Activity host
     * @return: BufferedReader bufReader
     * */
    public static Reader getReaderFromAssets(Activity host, String settingName)
            throws Exception {
        InputStreamReader inputReader = new InputStreamReader(host.getResources().getAssets().open(settingName));
//      XmlResourceParser xml = host.getResources().getAssets().openXmlResourceParser(fileName);
        BufferedReader bufReader = new BufferedReader(inputReader);
        return bufReader;
    }

    /**
     * @param :String path ,
     * @param :String fileName
     * @return: FileReader favReader
     * */
    public static Reader getReaderFromPath(String path, String fileName)
            throws Exception {
        // 绝对路径名字符串 Environment.getRootDirectory().getAbsolutePath()
        File favFile = new File(Environment.getRootDirectory().getAbsolutePath(), fileName);
        boolean isExists = favFile.exists();
        if (!isExists) {
            return null;
        }
        FileReader favReader = new FileReader(favFile);
        return favReader;
    }

    public static Reader getReaderFromPath(String path) {
        File favFile = new File(path);
        boolean isExists = favFile.exists();
        if (!isExists) {
            return null;
        }
        FileReader favReader = null;
        try {
            favReader = new FileReader(favFile);
        } catch (FileNotFoundException e) {
//          e.printStackTrace();
            favReader = null;
        }
        return favReader;
    }

    public static File getLastModifyFile(File[] arrayList){
        Arrays.sort(arrayList, new FileComparator());
        return arrayList[0];
    }
    public static class FileComparator implements Comparator<File> {
        public int compare(File file1, File file2) {
            if(file1.lastModified() < file2.lastModified()){
                return -1;
            }else{
                return 1;
            }
        }
    }

}
