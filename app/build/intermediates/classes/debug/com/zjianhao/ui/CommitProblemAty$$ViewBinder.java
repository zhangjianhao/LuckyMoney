// Generated code from Butter Knife. Do not modify!
package com.zjianhao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommitProblemAty$$ViewBinder<T extends com.zjianhao.ui.CommitProblemAty> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558490, "field 'commitBackTv'");
    target.commitBackTv = finder.castView(view, 2131558490, "field 'commitBackTv'");
    view = finder.findRequiredView(source, 2131558491, "field 'userSuggestionsEt'");
    target.userSuggestionsEt = finder.castView(view, 2131558491, "field 'userSuggestionsEt'");
    view = finder.findRequiredView(source, 2131558492, "field 'commitProblemBtn'");
    target.commitProblemBtn = finder.castView(view, 2131558492, "field 'commitProblemBtn'");
  }

  @Override public void unbind(T target) {
    target.commitBackTv = null;
    target.userSuggestionsEt = null;
    target.commitProblemBtn = null;
  }
}
