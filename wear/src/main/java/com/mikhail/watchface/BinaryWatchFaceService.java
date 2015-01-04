package com.mikhail.watchface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

/**
 * This is the binary watchface
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
        private Paint mBackgroundPaint;
        private Paint mBackgroundPaintAmbient;
        private Paint mHollowPaint;
        private Paint mSolidPaint;

        private final int mSolidDiameter = 8;
        private final int mHollowDiameter = 16;
        private final int mHollowDotSeparation = 10;

        private final int mHourOffsetY = 70;
        private final int mMinuteOffsetY = mHourOffsetY + mHollowDiameter + mHollowDotSeparation;
        private final int mSecondOffsetY = mMinuteOffsetY + mHollowDiameter + mHollowDotSeparation;
        private final int mDateOffsetY = mSecondOffsetY + mHollowDiameter +
                mHollowDotSeparation * 2;

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

            mNormalPaint = createTypefacePaint(Typeface.DEFAULT, Color.WHITE, 20.0f);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(Color.GREEN);

            mBackgroundPaintAmbient = new Paint();
            mBackgroundPaint.setColor(Color.BLACK);

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
                canvas.drawRect(bounds, mBackgroundPaintAmbient);
            }
            else {
                canvas.drawRect(bounds, mBackgroundPaint);
            }

            String hourBinary = getBinaryString(mTime.hour, 5);
            int[] hourDots = getDotPositions(bounds, 5);

            for(int i = 0; i < 5; i++) {
                canvas.drawCircle(hourDots[i], mHourOffsetY, mHollowDiameter / 2, mHollowPaint);

                if(hourBinary.charAt(i) == '1')
                    canvas.drawCircle(hourDots[i], mHourOffsetY, mSolidDiameter / 2, mSolidPaint);
            }

            String minuteBinary = getBinaryString(mTime.minute, 6);
            int[] minuteDots = getDotPositions(bounds, 6);

            for(int i = 0; i < 6; i++) {
                canvas.drawCircle(minuteDots[i], mMinuteOffsetY, mHollowDiameter / 2, mHollowPaint);

                if(minuteBinary.charAt(i) == '1')
                    canvas.drawCircle(minuteDots[i], mMinuteOffsetY, mSolidDiameter / 2, mSolidPaint);
            }

            if(!isInAmbientMode()) {
                String secondBinary = getBinaryString(mTime.second, 6);
                int[] secondDots = getDotPositions(bounds, 6);

                for(int i = 0; i < 6; i++) {
                    canvas.drawCircle(secondDots[i], mSecondOffsetY, mHollowDiameter / 2, mHollowPaint);

                    if(secondBinary.charAt(i) == '1')
                        canvas.drawCircle(secondDots[i], mSecondOffsetY, mSolidDiameter / 2, mSolidPaint);
                }
            }

            String date = SwearStrings.getDateString(mTime);
            float dateX = (bounds.width() - getTextWidth(date, mNormalPaint)) / 2;
            canvas.drawText(date, dateX, mDateOffsetY, mNormalPaint);
        }

        private float getTextWidth(String text, Paint paint) {
            float[] widths = new float[text.length()];
            paint.getTextWidths(text, 0, text.length(), widths);

            float result = 0;

            for(float width : widths) {
                result += width;
            }

            return result;
        }

        private String getBinaryString(int number, int size) {
            return String.format("%" + size + "s", Integer.toBinaryString(number)).replace(' ', '0');
        }

        private int[] getDotPositions(Rect bounds, final int numOfDots) {
            // 5 hour dots
            int[] dots = new int[numOfDots];
            int left = (bounds.width() - getDotsLength(numOfDots)) / 2;

            for(int i = 0; i < numOfDots; i++) {
                dots[i] = left + mHollowDiameter / 2;
                left += mHollowDiameter + mHollowDotSeparation;
            }

            return dots;
        }

        /**
         * Calculates the length of the dots on the screen. Useful for centering
         * @param numOfDots Number of hollow dots to be shown
         * @return Returns a float representing the length of the dots
         */
        private int getDotsLength(int numOfDots) {
            return (numOfDots * mHollowDiameter) + ((numOfDots - 1) * mHollowDotSeparation);
        }
    }
}
