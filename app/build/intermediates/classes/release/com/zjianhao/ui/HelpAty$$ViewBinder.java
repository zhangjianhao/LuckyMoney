// Generated code from Butter Knife. Do not modify!
package com.zjianhao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HelpAty$$ViewBinder<T extends com.zjianhao.ui.HelpAty> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558494, "field 'helpBackTv'");
    target.helpBackTv = finder.castView(view, 2131558494, "field 'helpBackTv'");
    view = finder.findRequiredView(source, 2131558495, "field 'helpWebview'");
    target.helpWebview = finder.castView(view, 2131558495, "field 'helpWebview'");
  }

  @Override public void unbind(T target) {
    target.helpBackTv = null;
    target.helpWebview = null;
  }
}
