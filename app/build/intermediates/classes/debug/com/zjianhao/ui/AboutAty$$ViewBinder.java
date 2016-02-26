// Generated code from Butter Knife. Do not modify!
package com.zjianhao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AboutAty$$ViewBinder<T extends com.zjianhao.ui.AboutAty> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558477, "field 'aboutBackTv'");
    target.aboutBackTv = finder.castView(view, 2131558477, "field 'aboutBackTv'");
    view = finder.findRequiredView(source, 2131558479, "field 'updateInformationTv'");
    target.updateInformationTv = finder.castView(view, 2131558479, "field 'updateInformationTv'");
    view = finder.findRequiredView(source, 2131558478, "field 'checkUpdateRl'");
    target.checkUpdateRl = finder.castView(view, 2131558478, "field 'checkUpdateRl'");
    view = finder.findRequiredView(source, 2131558480, "field 'stayInMemoryRl'");
    target.stayInMemoryRl = finder.castView(view, 2131558480, "field 'stayInMemoryRl'");
    view = finder.findRequiredView(source, 2131558481, "field 'useHelpRl'");
    target.useHelpRl = finder.castView(view, 2131558481, "field 'useHelpRl'");
    view = finder.findRequiredView(source, 2131558482, "field 'problemCommitRl'");
    target.problemCommitRl = finder.castView(view, 2131558482, "field 'problemCommitRl'");
  }

  @Override public void unbind(T target) {
    target.aboutBackTv = null;
    target.updateInformationTv = null;
    target.checkUpdateRl = null;
    target.stayInMemoryRl = null;
    target.useHelpRl = null;
    target.problemCommitRl = null;
  }
}
