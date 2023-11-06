package com.harderue.s.company.java_m1_sensors;

/*
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {
    private float azimuth;
    private Paint paint;
    private Path path;

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff0000ff); // Blue color
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        // Draw the compass circle
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw the north indicator
        path.reset();
        path.moveTo(centerX, centerY);
        path.lineTo(centerX, centerY - radius);
        canvas.save();
        canvas.rotate(-azimuth, centerX, centerY);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    public void SetAzimuth(float azimuth) {
        this.azimuth = azimuth;
        invalidate();
    }
}
*/

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

    private float lastNorthOrientation = -1;
    private static final float SMOOTH_FACTOR = 0.1f;

    private float northOrientation;
    private final Paint paint;
    private final Paint textPaint;
    private final Paint degreePaint;
    private final Path northArrow;

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        degreePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        northArrow = new Path();
        initPaints();
    }

    private void initPaints() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);

        degreePaint.setColor(Color.WHITE);
        degreePaint.setTextSize(30);

        // North arrow properties
        northArrow.setFillType(Path.FillType.EVEN_ODD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 40; // Offset from edge of the view

        // Draw the compass circle
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw the cardinal points
        canvas.drawText("N", centerX - textPaint.measureText("N") / 2, centerY - radius + textPaint.getTextSize(), textPaint);
        canvas.drawText("S", centerX - textPaint.measureText("S") / 2, centerY + radius - 20, textPaint);
        canvas.drawText("E", centerX + radius - textPaint.measureText("E"), centerY + textPaint.getTextSize() / 2, textPaint);
        canvas.drawText("W", centerX - radius + 20, centerY + textPaint.getTextSize() / 2, textPaint);

        // Draw the degree marks
        for (int i = 0; i < 360; i += 15) {
            // Draw a line for each 15 degrees
            double startx = centerX + radius * Math.cos(Math.toRadians(i));
            double starty = centerY + radius * Math.sin(Math.toRadians(i));
            double endx = centerX + (radius - 20) * Math.cos(Math.toRadians(i));
            double endy = centerY + (radius - 20) * Math.sin(Math.toRadians(i));

            canvas.drawLine((float) startx, (float) starty, (float) endx, (float) endy, paint);

            // Draw the degree numbers
            if (i % 45 != 0) { // Only draw the degree numbers for non-cardinal points
                String degreeText = String.valueOf(i);
                float textWidth = degreePaint.measureText(degreeText);
                float adjustedX = (float) (centerX + (radius - 40) * Math.cos(Math.toRadians(i)));
                float adjustedY = (float) (centerY + (radius - 40) * Math.sin(Math.toRadians(i)) + degreePaint.getTextSize() / 3);

                canvas.drawText(degreeText, adjustedX - textWidth / 2, adjustedY, degreePaint);
            }
        }

        // Draw the north arrow
        paint.setColor(Color.RED); // Red color for the north arrow
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        northArrow.reset();
        northArrow.moveTo(centerX, centerY - (radius - 20));
        northArrow.lineTo(centerX - 30, centerY);
        northArrow.lineTo(centerX + 30, centerY);
        northArrow.close();

        canvas.save();
        canvas.rotate(-northOrientation, centerX, centerY);
        canvas.drawPath(northArrow, paint);
        canvas.restore();

        paint.setStyle(Paint.Style.STROKE); // Reset to default after drawing the arrow
    }

    public void setAzimuth(float northOrientation) {
        /*
        if (lastNorthOrientation < 0) {
            // Initialize with the first value
            lastNorthOrientation = northOrientation;
        }

        // Apply a simple low-pass filter
        lastNorthOrientation = lastNorthOrientation + SMOOTH_FACTOR * (northOrientation - lastNorthOrientation);

        this.northOrientation = lastNorthOrientation;
        invalidate(); // Request to redraw the view with new angle
    }*/

        if (lastNorthOrientation < 0) {
            // Initialize with the first value if not already set
            lastNorthOrientation = northOrientation;
        }

        // Calculate the difference in angle
        float angleDifference = northOrientation - lastNorthOrientation;

        // Adjust for the discontinuity when crossing North (0 degrees)
        if (angleDifference < -180) {
            angleDifference += 360;
        } else if (angleDifference > 180) {
            angleDifference -= 360;
        }

        // Apply a simple low-pass filter to smooth the transition
        lastNorthOrientation = lastNorthOrientation + SMOOTH_FACTOR * angleDifference;

        // Normalize the result
        if (lastNorthOrientation < 0) {
            lastNorthOrientation += 360;
        } else if (lastNorthOrientation > 360) {
            lastNorthOrientation -= 360;
        }

        this.northOrientation = lastNorthOrientation;
        invalidate(); // Request to redraw the view with new angle
    }
}
