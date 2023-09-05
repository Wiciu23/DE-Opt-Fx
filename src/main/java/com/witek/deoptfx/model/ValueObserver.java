package com.witek.deoptfx.model;

import java.io.IOException;

public interface ValueObserver {
    void update() throws IOException;
}
