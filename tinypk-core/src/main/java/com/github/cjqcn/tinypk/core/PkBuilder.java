package com.github.cjqcn.tinypk.core;

/**
 * @description:
 * @author: chenjinquan
 * @create: 2019-01-12 22:46
 **/
public interface PkBuilder<T> {

    Pk<T> getPrimaryKey();

    Pk<T> getPrimaryKey(String item);

}
