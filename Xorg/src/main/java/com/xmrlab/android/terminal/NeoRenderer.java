package com.xmrlab.android.terminal;

public class NeoRenderer {
  public static void callNativeTextInputFinished() {
    DemoRenderer.nativeTextInputFinished();
  }

  public static void callNativeTextInput(int ascii, int unicode) {
    DemoRenderer.nativeTextInput(ascii, unicode);
  }
}
