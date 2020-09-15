package com.yyide.doorcontrol.utils;

import java.util.HashMap;
import java.util.Iterator;


public class MyHashMap extends HashMap<String, String> {
    /** 变量描述 */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder buffer = new StringBuilder(size() * 28);
        Iterator<Entry<String, String>> it = entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            Object key = entry.getKey();
            if (key != this) {
                buffer.append(key);
            } else {
                buffer.append("(this Map)");
            }
            buffer.append('=');
            Object value = entry.getValue();
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
            if (it.hasNext()) {
                buffer.append("&");
            }
        }
        return buffer.toString();
    }


}
