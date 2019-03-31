import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 异常文件解析
 *@param: args 启动jar包的文件路径
 *
 */
public class FileParse {

    private static final Logger logger = LoggerFactory.getLogger(FileParse.class);

    public static void main(String[] args){
        //异常文件解析
        String fileUrl = args[0];
        if (StringUtils.isBlank(fileUrl)){
            logger.error("======路径参数为空======");
            System.exit(0);
        }
        logger.info("======文件路径:"+fileUrl);
        parseFile(fileUrl);
    }

    private static void parseFile(String fileUrl){
        //开始计时
        long startTime = System.currentTimeMillis();
        //如果有相同的参数，只留一个
        Set<String> anrSet = new HashSet<String>();
        //有相同的参数也继续添加
        List<String> crashList = new ArrayList<String>(100);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(fileUrl));
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.contains("ANR")){
                    //截取包名: ANR in com.example.newnewlxccxyv_4 (aaa)
                    int firstIndex = line.trim().indexOf("(");
                    anrSet.add(line.substring(5, firstIndex).trim());
                }else if (line.contains("CRASH")){
                    //截取包名：CRASH:com.lenovo.browser(pid 17851);
                    int crashIndex  = line.indexOf("CRASH")+1;
                    int firstIndex = line.trim().indexOf("(");
                    crashList.add(line.substring(crashIndex,firstIndex).trim());
                }
            }
            //结束时间
            long endTime = System.currentTimeMillis();
            long time = (endTime-startTime)/1000;
            logger.info("======解析文件用时：" + time +"s");
            logger.info(String.valueOf(anrSet.size()));
            logger.info(String.valueOf(crashList.size()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            //关流
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
