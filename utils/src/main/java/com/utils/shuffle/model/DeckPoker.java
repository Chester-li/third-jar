package com.utils.shuffle.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utils.shuffle.model.Poker.Suit;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lichaoshuai
 * Created on 2022-11-14
 */
public class DeckPoker {

    private static final List<Poker> POKERS = Lists.newArrayList();
    private static final Map<Integer, Poker> MAPPING = Maps.newHashMap();
    private static final DeckPoker INSTANCE = new DeckPoker();

    private DeckPoker() {
        int count = 0;
        for (int i = 1; i <= 13; i++) {
            for (Suit suit : Suit.values()) {
                Poker poker = new Poker(suit, i);
                POKERS.add(poker);
                MAPPING.put(count++, poker);
            }
        }
    }

    public static DeckPoker getInstance() {
        return INSTANCE;
    }

    public void print() {
        for (Poker poker : POKERS) {
            System.out.println(poker.getSuit().getDesc() + poker.getNumber());
        }
    }

    public String print(List<Integer> indexes) {
        StringBuilder sb = new StringBuilder();
        for (Integer index : indexes) {
            Poker poker = MAPPING.get(index);
            sb.append(poker.getSuit().getDesc()).append(poker.getNumber()).append("   ");
        }
        return sb.toString();
    }

    public String print(Poker... pokers) {
        StringBuilder sb = new StringBuilder();
        for (Poker poker : pokers) {
            sb.append(poker.getSuit().getDesc()).append(poker.getNumber()).append("   ");
        }
        return sb.toString();
    }

    public List<FLUSH> check(List<List<Integer>> indexes) {
        List<FLUSH> flushes = Lists.newArrayList();
        for (List<Integer> index : indexes) {
            Poker poker1 = MAPPING.get(index.get(0));
            Poker poker2 = MAPPING.get(index.get(1));
            Poker poker3 = MAPPING.get(index.get(2));
            FLUSH flush = check(poker1, poker2, poker3);
            if (flush.getCode() < FLUSH.DOUBLE.getCode()) {
                flushes.add(flush);
            }
        }
        return flushes;
    }

    private FLUSH check(Poker poker1, Poker poker2, Poker poker3) {
        boolean boom = isBoom(poker1, poker2, poker3);
        if (boom) {
            System.out.println(FLUSH.BOOM.getDesc() + "    " + print(poker1, poker2, poker3));
            return FLUSH.BOOM;
        }
        boolean straightFlush = isStraightFlush(poker1, poker2, poker3);
        if (straightFlush) {
            System.out.println(FLUSH.STRAIGHT_FLUSH.getDesc() + "    " + print(poker1, poker2, poker3));
            return FLUSH.STRAIGHT_FLUSH;
        }
        boolean flush = isFlush(poker1, poker2, poker3);
        if (flush) {
            System.out.println(FLUSH.FLUSH.getDesc() + "    " + print(poker1, poker2, poker3));
            return FLUSH.FLUSH;
        }
        boolean straight = isStraight(poker1, poker2, poker3);
        if (straight) {
            System.out.println(FLUSH.STRAIGHT.getDesc() + "   " + print(poker1, poker2, poker3));
            return FLUSH.STRAIGHT;
        }
        boolean doubleFlush = isDouble(poker1, poker2, poker3);
        if (doubleFlush) {
            //            System.out.println(FLUSH.DOUBLE.getDesc() + "    " + print(poker1, poker2, poker3));
            return FLUSH.DOUBLE;
        }
        return FLUSH.SINGLE_CARD;
    }

    private boolean isDouble(Poker poker1, Poker poker2, Poker poker3) {
        boolean equals1 = poker1.getNumber() == poker2.getNumber();
        boolean equals2 = poker2.getNumber() == poker3.getNumber();
        if (equals1 || equals2) {
            return true;
        }
        return false;
    }


    private boolean isStraightFlush(Poker poker1, Poker poker2, Poker poker3) {
        boolean straight = isStraight(poker1, poker2, poker3);
        boolean flush = isFlush(poker1, poker2, poker3);
        if (straight && flush) {
            return true;
        }
        return false;
    }

    private boolean isStraight(Poker poker1, Poker poker2, Poker poker3) {
        boolean suitEqual1 = poker1.getSuit() == poker2.getSuit();
        boolean suitEqual2 = poker2.getSuit() == poker3.getSuit();
        if (suitEqual1 && suitEqual2) {
            return true;
        }
        return false;
    }

    private boolean isFlush(Poker poker1, Poker poker2, Poker poker3) {
        boolean plusOne1 = poker2.getNumber() - poker1.getNumber() == 1;
        boolean plusOne2 = poker3.getNumber() - poker2.getNumber() == 1;
        if (plusOne1 && plusOne2) {
            return true;
        }
        if (poker1.getNumber() == 1 && poker2.getNumber() == 12 && poker3.getNumber() == 13) {
            return true;
        }
        return false;
    }

    private boolean isBoom(Poker poker1, Poker poker2, Poker poker3) {
        boolean equals1 = poker1.getNumber() == poker2.getNumber();
        boolean equals2 = poker2.getNumber() == poker3.getNumber();
        if (equals1 && equals2) {
            return true;
        }
        return false;
    }


    @Getter
    @AllArgsConstructor
    public enum FLUSH {
        BOOM("炸弹", 0),
        STRAIGHT_FLUSH("同花顺", 1),
        FLUSH("顺子", 2),
        STRAIGHT("同花", 3),
        DOUBLE("一对", 4),
        SINGLE_CARD("单牌", 5);
        private final String desc;
        private final int code;
    }
}
