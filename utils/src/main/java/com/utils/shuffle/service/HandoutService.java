package com.utils.shuffle.service;

import static com.utils.shuffle.constant.ShuffleConstant.DECK_POKER_COUNT;
import static com.utils.shuffle.constant.ShuffleConstant.FLUSH_POKER_COUNT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author lichaoshuai
 * Created on 2022-11-15
 */
public class HandoutService {

    public static List<List<Integer>> handout(List<Integer> pokers, int peopleCount) {
        if (CollectionUtils.size(pokers) != DECK_POKER_COUNT) {
            throw new RuntimeException("扑克缺失");
        }
        if (peopleCount * FLUSH_POKER_COUNT > pokers.size()) {
            throw new RuntimeException("人数太多");
        }
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < peopleCount; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(pokers.get(i));
            list.add(pokers.get(i + peopleCount));
            list.add(pokers.get(i + 2 * peopleCount));
            list.sort(Comparator.comparingInt(o -> o));
            result.add(list);
        }
        return result;
    }

}
