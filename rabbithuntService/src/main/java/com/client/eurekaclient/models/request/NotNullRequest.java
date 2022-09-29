package com.client.eurekaclient.models.request;

import java.lang.reflect.Field;

public class NotNullRequest {
    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null)
                return true;
        return false;
    }
}
