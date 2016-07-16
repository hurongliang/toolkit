package com.hurl.toolkit.spider;

import java.io.Serializable;

/**
 * Created by hurongliang on 16/7/16.
 */
@FunctionalInterface
public interface PageProcessor<T extends Serializable> {
    T process(Page page);
}