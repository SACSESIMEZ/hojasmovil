package com.example.hojasdeservicio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CapturarFirma extends View {

    private Bitmap _bitmap;
    private Canvas _canvas;
    private Path _path;
    private Paint _bitmapPaint, _paint;
    private float _mX, _mY, _touchTolerancia = 4, _lineaEspesor = 4;

    public CapturarFirma(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        _path = new Path();
        _bitmapPaint = new Paint(Paint.DITHER_FLAG);
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setDither(true);
        _paint.setColor(Color.BLACK);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeJoin(Paint.Join.ROUND);
        _paint.setStrokeCap(Paint.Cap.ROUND);
        _paint.setStrokeWidth(_lineaEspesor);
    }
}