package com.example.hojasdeservicio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CapturarFirma extends View {

    private Bitmap _bitmap;
    private Canvas _canvas;
    private Path _path;
    private Paint _bitmapPaint;

    public CapturarFirma(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
