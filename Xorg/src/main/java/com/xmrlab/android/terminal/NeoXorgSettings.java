package com.xmrlab.android.terminal;

import com.xmrlab.android.terminal.xorg.NeoXorgViewClient;

public class NeoXorgSettings {
  public static void init(NeoXorgViewClient client) {
    Settings.Load(client);
  }
}
