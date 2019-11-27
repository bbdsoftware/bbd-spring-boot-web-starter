package com.bbdsoftware.service.response;

import java.util.*;

public interface Result<T> {
     T getResult();

     String getBusinessCode();

     List<ResultMessage> getMessages();
}
