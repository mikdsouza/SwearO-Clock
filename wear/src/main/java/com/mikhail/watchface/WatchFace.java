package com.mikhail.watchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.text.format.Time;

import android.util.Log;
import android.view.SurfaceHolder;

import java.util.TimeZone;

/**
 * Created by Mikhail on 4/01/2015.
 */
public abstract class WatchFace extends CanvasWatchFaceService {
    protected static String TAG;

    /* a time object */
    protected Time mTime;

    /* device features */
    protected boolean mLowBitAmbient;
    protected boolean mBurnInProtection;

    /* implement service callback methods */
    protected abstract class Engine extends CanvasWatchFaceService.Engine {
        protected abstract int MSG_UPDATE_TIME();


        /** How often {@link #mUpdateTimeHandler} ticks in milliseconds. */
        protected abstract long mInteractiveUpdateRateMs();

        /** Handler to update the time periodically in interactive mode. */
        protected Handler mUpdateTimeHandler;

        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME());
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME());
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };
        boolean mRegisteredTimeZoneReceiver = false;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            mUpdateTimeHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    if (message.what == MSG_UPDATE_TIME()) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "updating time");
                        }
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs =
                                    mInteractiveUpdateRateMs() - (timeMs % mInteractiveUpdateRateMs());
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME(), delayMs);
                        }
                    }
                }
            };

            mTime = new Time();
        }

        protected Paint createTypefacePaint(Typeface typeface, int color, float textSize) {
            Paint result = new Paint();
            result.setTypeface(typeface);
            result.setColor(color);
            result.setTextSize(textSize);
            result.setAntiAlias(true);
            return result;
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            mBurnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION,
                    false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible and
            // whether we're in ambient mode), so we may need to start or stop the timer
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            WatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            WatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }
    }
}
