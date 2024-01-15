package com.edoardo.bbs.factories;

public interface Factory <T, V> {
    T create ();
    V createDTO ();
}
