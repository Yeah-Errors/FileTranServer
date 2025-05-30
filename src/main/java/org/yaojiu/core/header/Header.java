/*
 * Copyright (c) 2023 Yeah-Errors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package org.yaojiu.core.header;

import org.yaojiu.uitils.Util;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Header {
    private final String fileName;
    private final String MD5;
    private final long fileSize;
    public Header(File file) {
        this.fileName = file.getName();
        this.fileSize = file.length();
        MD5=Util.getFileMD5(file);
    }
    private Header(String fileName,String MD5, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.MD5 = MD5;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMD5() {
        return MD5;
    }

    public long getFileSize() {
        return fileSize;
    }
    public byte[] toByteArray() {
       return toString().getBytes(StandardCharsets.UTF_8);
    }
    public static Header fromByteArray(byte[] bytes) {
        String header = new String(bytes, StandardCharsets.UTF_8);
        if (!header.startsWith("Yeah FILE TRANSMISSION SERVICE")) throw new RuntimeException("Invalid header format");
        String[] headerInfo = header.split("[=|]");
        String fileName=headerInfo[2];
        String MD5=headerInfo[4];
        long fileSize=Long.parseLong(headerInfo[6]);
        return new Header(fileName,MD5,fileSize);
    }

    @Override
    public String toString() {
        return String.format("Yeah FILE TRANSMISSION SERVICE|FILE_NAME=%s|MD5=%s|FILE_SIZE=%s",fileName,MD5,fileSize);
    }
}
