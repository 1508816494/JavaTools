package com.xf.tpm.test;

import com.xf.tpm.core.ThreadPoolImpl;

/**
 * @author xufeng on 2017/11/2
 */
public class ThreadPoolExample {

    public static void main(String[] args) {

        ThreadPoolImpl pool = new ThreadPoolImpl();
        pool.init();
    }
}
