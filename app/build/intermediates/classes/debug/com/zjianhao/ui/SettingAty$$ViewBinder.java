// Generated code from Butter Knife. Do not modify!
package com.zjianhao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingAty$$ViewBinder<T extends com.zjianhao.ui.SettingAty> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558494, "field 'helpBackTv'");
    target.helpBackTv = finder.castView(view, 2131558494, "field 'helpBackTv'");
    view = finder.findRequiredView(source, 2131558509, "field 'callbackCommandMessage'");
    target.callbackCommandMessage = finder.castView(view, 2131558509, "field 'callbackCommandMessage'");
  }

  @Override public void unbind(T target) {
    target.helpBackTv = null;
    target.callbackCommandMessage = null;
  }
}
