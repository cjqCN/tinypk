package com.github.cjqcn.tinypk.service;

public interface PKService {

    long getAsLong();

    String getAsShortString();

    long getAsLong(String scope);

    String getAsShortString(String scope);
}
