package com.github.cjqcn.tinypk.service;

public interface PKService {

    long getAsLong();

    String getAsSimpleString();

    long getAsLong(String scope);

    String getAsSimpleString(String scope);
}
