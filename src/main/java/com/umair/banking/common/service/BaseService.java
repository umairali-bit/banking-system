package com.umair.banking.common.service;

import java.util.List;

public interface BaseService <T, ID>{

    T getById(ID id);
    List<T> getAll();

}
