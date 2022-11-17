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
        long number = 0;
        while (true) {
            List<Integer> shuffle = ShuffleService.shuffle();
            List<List<Integer>> handout = HandoutService.handout(shuffle, 12);
            List<FLUSH> checkResult = instance.check(handout);
            if (CollectionUtils.isNotEmpty(checkResult)) {
                System.out.println(checkResult);
            }
            number++;
            long flushCount = checkResult.stream().filter(item -> item.getCode() <= FLUSH.FLUSH.getCode()).count();
            if (flushCount >= 6) {
                System.out.println("current number : " + number);
                break;
            }
            long boomCount = checkResult.stream().filter(item -> item == FLUSH.BOOM).count();
            if (boomCount >= 3) {
                System.out.println("current number : " + number);
                break;
            }
        }
    }

}
