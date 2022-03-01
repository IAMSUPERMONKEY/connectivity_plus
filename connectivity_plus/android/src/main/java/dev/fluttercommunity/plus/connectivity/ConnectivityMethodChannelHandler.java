// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package dev.fluttercommunity.plus.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * The handler receives {@link MethodCall}s from the UIThread, gets the related information from
 * a @{@link Connectivity}, and then send the result back to the UIThread through the {@link
 * MethodChannel.Result}.
 */
class ConnectivityMethodChannelHandler implements MethodChannel.MethodCallHandler {

  private Connectivity connectivity;

  private final Context context;

  /**
   * Construct the ConnectivityMethodChannelHandler with a {@code connectivity}. The {@code
   * connectivity} must not be null.
   */
  ConnectivityMethodChannelHandler(Context context) {
    assert (context != null);
    this.context = context;
  }

  @Override
  public void onMethodCall(MethodCall call, @NonNull MethodChannel.Result result) {
    if ("check".equals(call.method)) {
      if (connectivity == null) {
        result.error("-1", "初始化失败", "初始化失败");
        return;
      }
      result.success(connectivity.getNetworkType());
    } else if ("initConfig".equals(call.method)) {
      // 初始化 connectivity
      Log.d("flutter", "初始化 ");
      ConnectivityManager connectivityManager =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

      connectivity = new Connectivity(connectivityManager);

      ConnectivityBroadcastReceiver.bindConnect(connectivity);

    } else {
      result.notImplemented();
    }
  }
}
