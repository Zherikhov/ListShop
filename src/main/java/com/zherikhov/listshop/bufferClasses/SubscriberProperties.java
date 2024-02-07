package com.zherikhov.listshop.bufferClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriberProperties  {
    private long id;
    private boolean isAddContact;
    private boolean isFeedback;
    private boolean isMakeList;
    private boolean isWaitNickName;

    public SubscriberProperties(long id) {
        this.id = id;
    }
}
