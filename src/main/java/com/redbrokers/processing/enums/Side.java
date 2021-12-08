package com.redbrokers.processing.enums;

public enum Side {
    BUY("BUY"), SELL("SELL");
    private final String side;
    Side(String side) {
        this.side = side;
    }
    public String getSide(){
        return side;
    }
}
