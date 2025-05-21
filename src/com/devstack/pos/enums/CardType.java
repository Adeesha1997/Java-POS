package com.devstack.pos.enums;

public enum CardType {
    GOLD("gold"),PLATINUM("platinum"),SILVER("sliver");
    private String cardType;

    CardType(String cardType) {
        this.cardType = cardType;
    }
}
