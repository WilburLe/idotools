package com.toolbox.stat;

import com.toolbox.framework.thread.ThreadManager;
import com.toolbox.framework.utils.HttpUtility;

public class Test {

    public static void main(String[] args) {
        System.out.println(123);
    }

    public static void main1(String[] args) {
        HttpUtility.get("http://127.0.0.1:8088/Financing/service/openad/financing/uid8_1");

    }

    public static void main2(String[] args) {
        for (int i = 0; i < 10; i++) {
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        HttpUtility.get("http://127.0.0.1:8088/Financing/service/openad/financing/u_id2_" + j);
                    }
                }
            });
        }
        System.out.println("end");
    }
}
