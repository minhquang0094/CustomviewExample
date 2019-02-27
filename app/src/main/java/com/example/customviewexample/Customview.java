package com.example.customviewexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Customview extends View {
    private Paint mPaint;
    private Path mPath;
    private int mDrawColor; //Color line
    private int mBackgroundColor; //Color background
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;
    private float mX, mY;

    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= 4 || dy >= 4) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            mExtraCanvas.drawPath(mPath, mPaint);
        }
    }

    private void touchUp() {
        mPath.reset();
    }

    void resetView() {
        mExtraCanvas.drawColor(mBackgroundColor);
        invalidate();
    }

    public Customview(Context context) {
        super(context);

    }

    public Customview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundColor = ResourcesCompat.getColor(getResources(),
                R.color.orange, null);
        mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.yellow, null);

        // Holds the path we are currently drawing.
        mPath = new Path();
        // Set up the paint with which to draw.
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);
        // Smoothes out edges of what is drawn without affecting shape.
        mPaint.setAntiAlias(true);
        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE); // default: FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND); // default: MITER
        mPaint.setStrokeCap(Paint.Cap.ROUND); // default: BUTT
        mPaint.setStrokeWidth(12); // default: Hairline-width (really thin)

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mExtraBitmap = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
        mExtraCanvas = new Canvas(mExtraBitmap);
        // Fill the Bitmap with the background color.
        mExtraCanvas.drawColor(mBackgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        return true;
    }
}
