package com.oldlie.zshop.zshopvue.utils.captcha;

import java.util.Random;

public class Randoms {

    private static final Random RANDOM = new Random();

    //定义验证码字符.去除了O和I等容易混淆的字母
    public static final char ALPHA[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'G', 'K', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            '2', '3', '4', '5', '6', '7', '8', '9'};

    public static int num(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }

    public static int num(int num) {
        return RANDOM.nextInt(num);
    }

    public static char alpha() {
        return ALPHA[num(0, ALPHA.length)];
    }
}
