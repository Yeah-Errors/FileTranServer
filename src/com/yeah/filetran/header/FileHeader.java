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

package com.yeah.filetran.header;

import java.io.File;
import java.util.Map;


import static com.yeah.filetran.util.Util.getFileMD5;

public class FileHeader extends Header{


    FileHeader(File file){
        String MD5 = getFileMD5(file);
        Msg = "FILE_NAME="+file.getName()+"|MD5="+MD5;
        Header=BaseHeader +":"+Msg;
        msgMap.put("FILE_NAME",file.getName());
        msgMap.put("MD5",MD5);
    }
    @Override
    public String getMsg() {
        return Msg;
    }

    @Override
    public Map<String, String> getMsgMap() {
        return msgMap;
    }

}
