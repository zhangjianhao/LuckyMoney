package com.zjianhao.https;

/**
 * Created by zjianhao on 15-12-12.
 */
public interface DownloadListner {
    public void onFinish();
    public void onProgress(long completedSize,long totalSize);
    public void onStart();
}
