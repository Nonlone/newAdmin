package com.feitai.admin.core.service;

import lombok.Data;

@Data
public class Sort {

    public enum Direction {

        ASC, DESC;
    }

    private Direction direction;

    private String property;

    public Sort(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    public Sort(String property, String direction) {
        this.property = property;
        this.direction = Direction.valueOf(direction.toUpperCase());
    }

    public Sort(String orderBy) {
        String[] order = orderBy.split(":");
        if (order.length > 2) {
            this.property = order[0];
            this.direction = Direction.valueOf(order[1].toUpperCase());
        }
    }

    @Override
    public String toString() {
        return this.property + " " + this.direction.toString().toUpperCase();
    }
}
