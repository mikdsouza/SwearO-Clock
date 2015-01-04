package com.mikhail.watchface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

/**
 * Created by Mikhail on 4/01/2015.
 */
public class BinaryWatchFaceService extends WatchFace {

    @Override
    public Engine onCreateEngine() {
        /* provide your watch face implementation */
        TAG = "BinaryWatchFaceService";
        return new BinaryEngine();
    }

    protected class BinaryEngine extends Engine {

        private Paint mNormalPaint;
        private Paint mBackgroudPaint;
        private Paint mBackgroudPaintAmbient;
        private Paint mHollowPaint;
        private Paint mSolidPaint;

        private final int mSolidDiameter = 8;
        private final int mHollowDiameter = 16;
        private final int mHollowDotSerparation = 10;

        private final int mHourOffestY = 70;

        @Override
        protected int MSG_UPDATE_TIME() {
            return 0;
        }

        @Override
        protected long mInteractiveUpdateRateMs() {
            return 500;
        }

        @Override
        protected void doOnCreate(SurfaceHolder holder) {
            /* configure the system UI */
            setWatchFaceStyle(new WatchFaceStyle.Builder(BinaryWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle
                            .BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            mNormalPaint = createTypefacePaint(Typeface.DEFAULT, Color.WHITE, 35.0f);

            mBackgroudPaint = new Paint();
            mBackgroudPaint.setColor(Color.GREEN);

            mBackgroudPaintAmbient = new Paint();
            mBackgroudPaint.setColor(Color.BLACK);

            mHollowPaint = new Paint();
            mHollowPaint.setColor(Color.WHITE);
            mHollowPaint.setStyle(Paint.Style.STROKE);
            mHollowPaint.setStrokeWidth(1);

            mSolidPaint = new Paint();
            mSolidPaint.setColor(Color.WHITE);
        }

        @Override
        protected void doOnDraw(Canvas canvas, Rect bounds) {
            if(isInAmbientMode()) {
                canvas.drawRect(bounds, mBackgroudPaintAmbient);
            }
            else {
                canvas.drawRect(bounds, mBackgroudPaint);
            }

            String hourBinary = String.format("%5s", Integer.toBinaryString(mTime.hour))
                    .replace(' ', '0');
            int[] hourDots = getDotPositions(bounds, 5);

            for(int i = 0; i < 5; i++) {
                canvas.drawCircle(hourDots[i], mHourOffestY, mHollowDiameter / 2, mHollowPaint);

                if(hourBinary.charAt(i) == '1')
                    canvas.drawCircle(hourDots[i], mHourOffestY, mSolidDiameter / 2, mSolidPaint);
            }
        }

        private int[] getDotPositions(Rect bounds, final int numOfDots) {
            // 5 hour dots
            int[] dots = new int[numOfDots];
            int left = (bounds.width() - getDotsLength(numOfDots)) / 2;

            for(int i = 0; i < numOfDots; i++) {
                dots[i] = left + mHollowDiameter / 2;
                left += mHollowDiameter + mHollowDotSerparation;
            }

            return dots;
        }

        /**
         * Calculates the length of the dots on the screen. Useful for centering
         * @param numOfDots Number of hollow dots to be shown
         * @return Returns a float representing the length of the dots
         */
        private int getDotsLength(int numOfDots) {
            return (numOfDots * mHollowDiameter) + ((numOfDots - 1) * mHollowDotSerparation);
        }
    }
}
