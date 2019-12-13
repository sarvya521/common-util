package com.sp.common.util;

import java.util.Iterator;

public class Range implements Iterable<Integer> {
    private final int begin;
    private final int end;
    private final int stride;
    private int cursor;
    private boolean isAscending = true;

    public Range(int begin, int end, int stride) {
        this.begin = begin;
        this.end = end;
        if (stride < 0) {
            Util.badArgument("Negative value for stride is not allowed");
        }
        this.stride = stride;
        if (begin < end) {
            cursor = begin - stride;
        } else if (begin > end) {
            isAscending = false;
            cursor = begin + stride;
        } else {
            cursor = begin;
        }
    }

    public Range(int begin, int end) {
        this.begin = begin;
        this.end = end;
        this.stride = 1;
        if (begin < end) {
            cursor = begin - stride;
        } else if (begin > end) {
            isAscending = false;
            cursor = begin + stride;
        } else {
            cursor = begin;
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                if (isAscending) {
                    return cursor + stride <= end;
                } else {
                    return cursor - stride >= end;
                }
            }

            @Override
            public Integer next() {
                if (isAscending) {
                    if (cursor + stride > end) {
                        Util.noSuchElement("Iterator extends its limit");
                    }
                    cursor = cursor + stride;
                } else {
                    if (cursor - stride < end) {
                        Util.noSuchElement("Iterator extends its limit");
                    }
                    cursor = cursor - stride;
                }
                return cursor;
            }
        };
    }
}