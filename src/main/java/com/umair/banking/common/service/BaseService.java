package com.umair.banking.common.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService <T, ID>{

    T getById(ID id);
    List<T> getAll();

}
