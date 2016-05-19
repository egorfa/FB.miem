package com.hse_miem.fb_miem.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Egor on 18/05/16.
 */
public class Beacon {

    @Getter @Setter
    private Coordinates coordinates;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer major;
    private Integer minor;

    private class Coordinates {
        @Getter @Setter
        private double x;
        @Getter @Setter
        private double y;
    }
}
