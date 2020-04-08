/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exo.source;

import com.google.android.exo.C;
import com.google.android.exo.Timeline;
import com.google.android.exo.util.Assertions;

/**
 * A {@link Timeline} consisting of a single period and static window.
 */
public final class SinglePeriodTimeline extends Timeline {

  private static final Object ID = new Object();

  private final long presentationStartTimeMs;
  private final long windowStartTimeMs;
  private final long periodDurationUs;
  private final long windowDurationUs;
  private final long windowPositionInPeriodUs;
  private final long windowDefaultStartPositionUs;
  private final boolean isSeekable;
  private final boolean isDynamic;

  /**
   * Creates a timeline containing a single period and a window that spans it.
   *
   * @param durationUs The duration of the period, in microseconds.
   * @param isSeekable Whether seeking is supported within the period.
   * @param isDynamic Whether the window may change when the timeline is updated.
   */
  public SinglePeriodTimeline(long durationUs, boolean isSeekable, boolean isDynamic) {
    this(durationUs, durationUs, 0, 0, isSeekable, isDynamic);
  }

  /**
   * Creates a timeline with one period, and a window of known duration starting at a specified
   * position in the period.
   *
   * @param periodDurationUs The duration of the period in microseconds.
   * @param windowDurationUs The duration of the window in microseconds.
   * @param windowPositionInPeriodUs The position of the start of the window in the period, in
   *     microseconds.
   * @param windowDefaultStartPositionUs The default position relative to the start of the window at
   *     which to begin playback, in microseconds.
   * @param isSeekable Whether seeking is supported within the window.
   * @param isDynamic Whether the window may change when the timeline is updated.
   */
  public SinglePeriodTimeline(long periodDurationUs, long windowDurationUs,
      long windowPositionInPeriodUs, long windowDefaultStartPositionUs, boolean isSeekable,
      boolean isDynamic) {
    this(C.TIME_UNSET, C.TIME_UNSET, periodDurationUs, windowDurationUs, windowPositionInPeriodUs,
        windowDefaultStartPositionUs, isSeekable, isDynamic);
  }

  /**
   * Creates a timeline with one period, and a window of known duration starting at a specified
   * position in the period.
   *
   * @param presentationStartTimeMs The start time of the presentation in milliseconds since the
   *     epoch.
   * @param windowStartTimeMs The window's start time in milliseconds since the epoch.
   * @param periodDurationUs The duration of the period in microseconds.
   * @param windowDurationUs The duration of the window in microseconds.
   * @param windowPositionInPeriodUs The position of the start of the window in the period, in
   *     microseconds.
   * @param windowDefaultStartPositionUs The default position relative to the start of the window at
   *     which to begin playback, in microseconds.
   * @param isSeekable Whether seeking is supported within the window.
   * @param isDynamic Whether the window may change when the timeline is updated.
   */
  public SinglePeriodTimeline(long presentationStartTimeMs, long windowStartTimeMs,
      long periodDurationUs, long windowDurationUs, long windowPositionInPeriodUs,
      long windowDefaultStartPositionUs, boolean isSeekable, boolean isDynamic) {
    this.presentationStartTimeMs = presentationStartTimeMs;
    this.windowStartTimeMs = windowStartTimeMs;
    this.periodDurationUs = periodDurationUs;
    this.windowDurationUs = windowDurationUs;
    this.windowPositionInPeriodUs = windowPositionInPeriodUs;
    this.windowDefaultStartPositionUs = windowDefaultStartPositionUs;
    this.isSeekable = isSeekable;
    this.isDynamic = isDynamic;
  }

  @Override
  public int getWindowCount() {
    return 1;
  }

  @Override
  public Window getWindow(int windowIndex, Window window, boolean setIds,
      long defaultPositionProjectionUs) {
    Assertions.checkIndex(windowIndex, 0, 1);
    Object id = setIds ? ID : null;
    long windowDefaultStartPositionUs = this.windowDefaultStartPositionUs;
    if (isDynamic && defaultPositionProjectionUs != 0) {
      if (windowDurationUs == C.TIME_UNSET) {
        // Don't allow projection into a window that has an unknown duration.
        windowDefaultStartPositionUs = C.TIME_UNSET;
      } else {
        windowDefaultStartPositionUs += defaultPositionProjectionUs;
        if (windowDefaultStartPositionUs > windowDurationUs) {
          // The projection takes us beyond the end of the window.
          windowDefaultStartPositionUs = C.TIME_UNSET;
        }
      }
    }
    return window.set(id, presentationStartTimeMs, windowStartTimeMs, isSeekable, isDynamic,
        windowDefaultStartPositionUs, windowDurationUs, 0, 0, windowPositionInPeriodUs);
  }

  @Override
  public int getPeriodCount() {
    return 1;
  }

  @Override
  public Period getPeriod(int periodIndex, Period period, boolean setIds) {
    Assertions.checkIndex(periodIndex, 0, 1);
    Object id = setIds ? ID : null;
    return period.set(id, id, 0, periodDurationUs, -windowPositionInPeriodUs);
  }

  @Override
  public int getIndexOfPeriod(Object uid) {
    return ID.equals(uid) ? 0 : C.INDEX_UNSET;
  }

}
