// Generated code from Butter Knife. Do not modify!
package com.zjianhao.getluckymoney;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.zjianhao.getluckymoney.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558483, "field 'startService'");
    target.startService = finder.castView(view, 2131558483, "field 'startService'");
    view = finder.findRequiredView(source, 2131558484, "field 'shareAppBtn'");
    target.shareAppBtn = finder.castView(view, 2131558484, "field 'shareAppBtn'");
    view = finder.findRequiredView(source, 2131558510, "field 'aboutBtn'");
    target.aboutBtn = finder.castView(view, 2131558510, "field 'aboutBtn'");
    view = finder.findRequiredView(source, 2131558511, "field 'userSettingBtn'");
    target.userSettingBtn = finder.castView(view, 2131558511, "field 'userSettingBtn'");
  }

  @Override public void unbind(T target) {
    target.startService = null;
    target.shareAppBtn = null;
    target.aboutBtn = null;
    target.userSettingBtn = null;
  }
}
