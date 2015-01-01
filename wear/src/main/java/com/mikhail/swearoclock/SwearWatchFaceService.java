package com.mikhail.swearoclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.TimeZone;

/**
 * Created by Mikhail on 28/12/2014.
 */
public class SwearWatchFaceService extends CanvasWatchFaceService{
    static final String TAG = "SwearWatchFaceService";

    /** Update every 5 seconds. Don't really need any more accuracy than that
     *  considering the precision of the clock is already at 5 minutes
     */
    static final long NORMAL_UPDATE_RATE_MS = 5000;

    static final Typeface BOLD_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
    static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    static final int MSG_UPDATE_TIME = 0;

    /* a time object */
    Time mTime;

    /* device features */
    boolean mLowBitAmbient;
    boolean mBurnInProtection;

    @Override
    public Engine onCreateEngine() {
        /* provide your watch face implementation */
        return new Engine();
    }

    /* implement service callback methods */
    private class Engine extends CanvasWatchFaceService.Engine {

        /** Alpha value for drawing time when in mute mode. */
        static final int MUTE_ALPHA = 100;

        /** Alpha value for drawing time when not in mute mode. */
        static final int NORMAL_ALPHA = 255;

        /** How often {@link #mUpdateTimeHandler} ticks in milliseconds. */
        long mInteractiveUpdateRateMs = NORMAL_UPDATE_RATE_MS;

        /** Handler to update the time periodically in interactive mode. */
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "updating time");
                        }
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs =
                                    mInteractiveUpdateRateMs - (timeMs % mInteractiveUpdateRateMs);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };
        private Paint mBackground;
        private Paint mBoldPaint;
        private Paint mNormalPaint;
        private Paint mStrokePaint;
        private Paint mBackgroundAmbient;

        private static final float mOffsetX = 65.0f;
        private static final float mOffsetY = 75.0f;
        private static final float mHourOffsetY = 105.0f;
        private static final float mFuckingOffsetY = 135.0f;
        private static final float minuteOffsetY = 165.0f;

        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
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

            /* configure the system UI */
            setWatchFaceStyle(new WatchFaceStyle.Builder(SwearWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle
                            .BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            mBackground = new Paint();
            mBackground.setColor(Color.rgb(41, 1, 73));

            mBackgroundAmbient = new Paint();
            mBackgroundAmbient.setColor(Color.BLACK);

            mBoldPaint = createTypefacePaint(BOLD_TYPEFACE, Color.WHITE, 35.0f);

            mNormalPaint = createTypefacePaint(NORMAL_TYPEFACE, Color.WHITE, 35.0f);

            mStrokePaint = createTypefacePaint(BOLD_TYPEFACE, Color.WHITE, 35.0f);
            mStrokePaint.setAlpha(MUTE_ALPHA);
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setStrokeWidth(1);

            mTime = new Time();
        }

        private Paint createTypefacePaint(Typeface typeface, int color, float textSize) {
            Paint result = new Paint();
            result.setTypeface(typeface);
            result.setColor(color);
            result.setTextSize(textSize);
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
            float offset1, offset2;

            if(SwearStrings.hasMinutes(mTime)) {
                offset1 = minuteOffsetY;
                offset2 = mHourOffsetY;
            }
            else {
                offset1 = mHourOffsetY;
                offset2 = minuteOffsetY;
            }

            if(isInAmbientMode()) {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundAmbient);
                canvas.drawText(SwearStrings.IT_IS, mOffsetX, mOffsetY, mStrokePaint);

                canvas.drawText(SwearStrings.getHour(mTime), mOffsetX, offset1, mStrokePaint);
                canvas.drawText(SwearStrings.getFucking(mTime), mOffsetX, mFuckingOffsetY, mStrokePaint);
                canvas.drawText(SwearStrings.getMinute(mTime), mOffsetX, offset2, mStrokePaint);
            }
            else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackground);
                canvas.drawText(SwearStrings.IT_IS, mOffsetX, mOffsetY, mBoldPaint);

                canvas.drawText(SwearStrings.getHour(mTime), mOffsetX, offset1, mNormalPaint);
                canvas.drawText(SwearStrings.getFucking(mTime), mOffsetX, mFuckingOffsetY, mNormalPaint);
                canvas.drawText(SwearStrings.getMinute(mTime), mOffsetX, offset2, mNormalPaint);
            }
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
            SwearWatchFaceService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            SwearWatchFaceService.this.unregisterReceiver(mTimeZoneReceiver);
        }
    }
}
