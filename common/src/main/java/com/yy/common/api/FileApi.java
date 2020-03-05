package com.yy.common.api;

import com.yy.common.exception.AssertUtils;
import com.yy.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

@RestController
public class FileApi {
    private static final int BUFFER_LENGTH = 1024;
    private static final Logger logger = LoggerFactory.getLogger(FileApi.class);
    @Value("${files.uploadFiles}")
    private String attachFiles;

    @RequestMapping("/file/**/{name:.+}")
    public void DownloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AssertUtils.notBlank(request.getServletPath(),"文件key不能为空");
        File file = new File(attachFiles + request.getServletPath());
        if (file.exists()) {
            String fileName = file.getName();
            String userAgent = request.getHeader("User-Agent");
            if (StringUtils.contains(userAgent,"MSIE")
                    || StringUtils.contains(userAgent,"Trident")
                    || StringUtils.contains(userAgent,"Edge")) {
                //IE浏览器处理
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setCharacterEncoding("UTF-8");
            /*Path source = Paths.get(String.valueOf(file.toPath()));
            MimetypesFileTypeMap m = new MimetypesFileTypeMap(source.toString());
            String contentType = m.getContentType(file);
            response.setContentType( contentType+ ";charset=utf-8");*/
            /*if (StringUtils.isNotBlank(request.getServletPath())
                    && request.getServletPath().endsWith(".amr")){
                // armTOmp3
                String path = attachFiles+ java.io.File.separator+"amrTopm3Temp";
                File target = new File(path);
                if (!target.exists()){
                    target.mkdirs();
                }
                String filePath = path + java.io.File.separator + fileName+".mp3";
                File mp3File = new File(filePath);
                it.sauronsoftware.jave.AudioUtils.amrToMp3(file, mp3File);
                file = mp3File;
            }*/
            String contentType = Files.probeContentType(file.toPath());
            if (StringUtils.isBlank(contentType)
                    && StringUtils.isNotBlank(request.getServletPath())
                    && request.getServletPath().endsWith(".zip")){
                contentType ="application/x-zip-compressed";
            }
            response.setContentType( contentType+ ";charset=utf-8");
            if (StringUtils.isNotBlank(contentType) && contentType.indexOf("image") == -1 && contentType.indexOf("video") == -1 &&  contentType.indexOf("audio") == -1) {
                response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName +"\"");
            }
            FileInputStream inStream = null;
            BufferedInputStream bufferInStream = null;

            try {
                ServletOutputStream outStream = response.getOutputStream();
                inStream = new FileInputStream(file);
                bufferInStream = new BufferedInputStream(inStream);
                byte[] buffer = new byte[BUFFER_LENGTH];
                int len = -1;
                while ((len = bufferInStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferInStream != null) {
                    try {
                        bufferInStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            logger.debug("该路径下文件不存在存在{}",attachFiles + request.getServletPath());
            throw new BizException("文件不存在");
        }
    }
}
