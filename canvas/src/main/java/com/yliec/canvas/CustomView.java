package com.yliec.canvas;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lecion on 5/21/15.
 */
public class CustomView extends View {
    private Paint mPaint;


    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setTextSize(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, 0, 100, 100);

//        canvas.drawRect(rectF, mPaint);

        canvas.drawArc(new RectF(0, 0, 100, 100), 0, 90, true, mPaint);
        canvas.drawArc(new RectF(200, 0, 300, 100), 0, 180, true, mPaint);
        canvas.drawArc(new RectF(400, 0, 500, 100), 45, 225, true, mPaint);

        canvas.drawArc(new RectF(0, 200, 100, 300), 0, 90, false, mPaint);
        canvas.drawArc(new RectF(200, 200, 300, 300), 0, 180, false, mPaint);
        canvas.drawArc(new RectF(400, 200, 500, 300), 0, 270, false, mPaint);

        canvas.drawLine(0, 400, 100, 500, mPaint);
        canvas.drawLine(100, 400, 0, 500, mPaint);

        canvas.drawOval(new RectF(200, 400, 400, 500), mPaint);

        canvas.drawRoundRect(new RectF(500, 400, 700, 500), 10, 10, mPaint);

        canvas.translate(200, 0);
        Path path = new Path();
        path.moveTo(200, 600);
        path.lineTo(100, 700);
        path.lineTo(300, 700);
        path.lineTo(200, 600);
        canvas.drawTextOnPath("Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者", path, 10, 10, mPaint);
        canvas.save();
        canvas.translate(-200, 0);
        canvas.drawTextOnPath("Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者Android开发者", path, 10, 10, mPaint);
        canvas.restore();
        canvas.translate(-100, 0);
        canvas.drawRect(0, 600, 100, 700, mPaint);

        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sell), 100, 800, mPaint);



//        canvas.drawPath(path, mPaint);


    }
}
