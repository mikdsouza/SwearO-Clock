package com.mikhail.watchface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

/**
 * Created by Mikhail on 28/12/2014.
 */
public class SwearWatchFaceService extends WatchFace{

    @Override
    public Engine onCreateEngine() {
        /* provide your watch face implementation */
        TAG = "SwearWatchFaceService";
        return new SwearEngine();
    }

    protected class SwearEngine extends Engine {

        private final Typeface BOLD_TYPEFACE = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        private final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);

        private Paint mBackground;
        private Paint mBoldPaint;
        private Paint mNormalPaint;
        private Paint mStrokePaint;
        private Paint mBackgroundAmbient;

        private static final float mOffsetX = 65.0f;
        private static final float mHourOffsetX = 25.0f;
        private static final float mFuckingOffsetX = 10.0f;
        private static final float mMinuteOffsetX = 10.0f;
        private static final float mDateOffsetX = 15.0f;

        private static final float mOffsetY = 75.0f;
        private static final float mHourOffsetY = 110.0f;
        private static final float mFuckingOffsetY = 145.0f;
        private static final float mMinuteOffsetY = 180.0f;
        private static final float mDateOffsetY = 215.0f;

        @Override
        protected int MSG_UPDATE_TIME() {
            return 0;
        }

        @Override
        protected long mInteractiveUpdateRateMs() {
            return 5000;
        }

        @Override
        public void doOnCreate(SurfaceHolder holder) {
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
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setStrokeWidth(1);

            mTime = new Time();
        }

        @Override
        public void doOnDraw(Canvas canvas, Rect bounds) {
            float Xoffset1, Xoffest2, Yoffset1, Yoffset2;

            if(SwearStrings.hasMinutes(mTime)) {
                Xoffset1 = mMinuteOffsetX;
                Xoffest2 = mHourOffsetX;
                Yoffset1 = mMinuteOffsetY;
                Yoffset2 = mHourOffsetY;
            }
            else {
                Xoffset1 = mHourOffsetX;
                Xoffest2 = mMinuteOffsetX;
                Yoffset1 = mHourOffsetY;
                Yoffset2 = mMinuteOffsetY;
            }

            if(isInAmbientMode()) {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundAmbient);
                canvas.drawText(SwearStrings.IT_IS, mOffsetX, mOffsetY, mStrokePaint);

                canvas.drawText(SwearStrings.getHour(mTime), Xoffset1, Yoffset1, mStrokePaint);
                canvas.drawText(SwearStrings.getFucking(mTime), mFuckingOffsetX, mFuckingOffsetY,
                        mStrokePaint);
                canvas.drawText(SwearStrings.getMinute(mTime), Xoffest2, Yoffset2, mStrokePaint);

                canvas.drawText(SwearStrings.getDateString(mTime), mDateOffsetX, mDateOffsetY,
                        mStrokePaint);
            }
            else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackground);
                canvas.drawText(SwearStrings.IT_IS, mOffsetX, mOffsetY, mBoldPaint);

                canvas.drawText(SwearStrings.getHour(mTime), Xoffset1, Yoffset1, mNormalPaint);
                canvas.drawText(SwearStrings.getFucking(mTime), mFuckingOffsetX, mFuckingOffsetY,
                        mNormalPaint);
                canvas.drawText(SwearStrings.getMinute(mTime), Xoffest2, Yoffset2, mNormalPaint);

                canvas.drawText(SwearStrings.getDateString(mTime), mDateOffsetX, mDateOffsetY,
                        mNormalPaint);
            }
        }
    }
}
