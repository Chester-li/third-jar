package com.utils.shuffle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author lichaoshuai
 * Created on 2022-11-14
 */
@AllArgsConstructor
@Data
public class Poker {

    private Suit suit;
    private int number;

    @Getter
    @AllArgsConstructor
    public enum Suit {
        spade("黑桃"),
        heart("红桃"),
        club("梅花"),
        diamond("方块");

        private String desc;

    }

}
