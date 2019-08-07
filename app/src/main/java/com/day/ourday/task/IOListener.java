package com.day.ourday.task;

import java.util.List;

/**
 * Created by LimerenceT on 19-8-1
 */
public interface IOListener<T> {

    void onTaskResult(List<T> data);

    void onTaskSuccess();

}
