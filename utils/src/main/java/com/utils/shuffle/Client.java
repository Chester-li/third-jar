package com.utils.shuffle;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.utils.shuffle.model.DeckPoker;
import com.utils.shuffle.model.DeckPoker.FLUSH;
import com.utils.shuffle.service.HandoutService;
import com.utils.shuffle.service.ShuffleService;

/**
 * @author lichaoshuai
 * Created on 2022-11-14
 */
public class Client {

    public static void main(String[] args) {
        DeckPoker instance = DeckPoker.getInstance();
        for (int i = 0; i < 1000; i++) {
            List<Integer> shuffle = ShuffleService.shuffle();
            List<List<Integer>> handout = HandoutService.handout(shuffle, 10);
            List<FLUSH> checkResult = instance.check(handout);
            if (CollectionUtils.isNotEmpty(checkResult)) {
                System.out.println(checkResult);
            }
        }
    }

}
