package com.rev.revpasswordmanagerp2.dto;

import lombok.Data;

@Data
public class MultipleRequest {

    private int count;
    private int length;
    private boolean upper;
    private boolean lower;
    private boolean number;
    private boolean special;
    private boolean excludeSimilar;
}