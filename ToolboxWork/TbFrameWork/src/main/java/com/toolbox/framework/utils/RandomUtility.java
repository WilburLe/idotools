/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:RandomUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 6, 2013 6:00:42 PM
 * 
 */
package com.toolbox.framework.utils;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 6, 2013
 * 
 */

public class RandomUtility extends RandomUtils {
    //weightRandom
    public static int nextWeightIndex(int... weights) {
        int total = 0;
        for (int i = 0; i < weights.length; i++) {
            total += weights[i];
        }
        int random = nextInt(total);
        int regionMin = 0;
        int regionMax = 0;
        for (int i = 0; i < weights.length; i++) {
            regionMin = regionMax;
            regionMax += weights[i];
            if (regionMin <= random && random < regionMax) {
                return i;
            }
        }
        return 0;
    }

    public static String strRandom(int length) {
        char[] abc = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                
        };
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int random = nextInt(abc.length);
            char s = abc[random];
            str.append(s);
        }
        return str.toString();
    }
}
