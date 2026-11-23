package com.xmrlab.android.terminal.component.pm;

import com.xmrlab.android.terminal.framework.database.annotation.ID;
import com.xmrlab.android.terminal.framework.database.annotation.Table;
import com.xmrlab.android.terminal.framework.database.annotation.ID;
import com.xmrlab.android.terminal.framework.database.annotation.Table;

@Table
public class Source {
  @ID(autoIncrement = true)
  private int id;

  public String url;

  public String repo;

  public boolean enabled;

  public Source() {
  }

  public Source(String url, String repo, boolean enabled) {
    this.url = url;
    this.repo = repo;
    this.enabled = enabled;
  }
}
