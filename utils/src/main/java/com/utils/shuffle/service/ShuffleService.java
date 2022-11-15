package com.utils.shuffle.service;

import static com.utils.shuffle.constant.ShuffleConstant.DECK_POKER_COUNT;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * @author lichaoshuai
 * Created on 2022-11-14
 */
public class ShuffleService {

    private static final Random RAND = new Random();



    public static List<Integer> shuffle() {
        List<Integer> number = Lists.newArrayList();
        for (int i = 0; i < DECK_POKER_COUNT; i++) {
            number.add(i);
        }
        for (int i = 0; i < DECK_POKER_COUNT; i++) {
            int index = randInt(i, DECK_POKER_COUNT);
            Integer temp = number.get(index);
            number.set(index, number.get(i));
            number.set(i, temp);
        }
        return number;
    }


    private static int randInt(int start, int end) {
        return RAND.nextInt(end - start) + start;
    }

}
