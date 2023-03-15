package com.example.hojasdeservicio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class CapturarFirma extends View {

    private Bitmap _bitmap;
    private Canvas _canvas;
    private Path _path;
    private Paint _bitmapPaint, _paint;
    private float _mX, _mY, _touchTolerancia = 4, _lineaEspesor = 8;

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _bitmap = Bitmap.createBitmap(w, (h > 0 ? h : ((View) this.getParent()).getHeight()), Bitmap.Config.ARGB_8888);
        _canvas = new Canvas(_bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(_bitmap, 0, 0, _bitmapPaint);
        canvas.drawPath(_path, _paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        super.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    public void clearCanvas(){
        _canvas.drawColor(Color.WHITE);
        invalidate();
    }

    public Bitmap getBitmap(){
        View v = (View) this.getParent();
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);

        return b;
    }

    public byte[] getBytes(){
        Bitmap b = getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void touchStart(float x, float y){
        _path.reset();
        _path.moveTo(x, y);
        _mX = x;
        _mY = y;
    }

    private void touchMove(float x, float y){
        float dx = Math.abs(x - _mX);
        float dy = Math.abs(y - _mY);

        if(dx >= _touchTolerancia || dy >= _touchTolerancia){
            _path.quadTo(_mX, _mY, (x + _mX) / 2, (y + _mY) / 2);
            _mX = x;
            _mY = y;
        }
    }

    private void touchUp(){
        if(!_path.isEmpty()){
            _path.lineTo(_mX, _mY);
            _canvas.drawPath(_path, _paint);
        } else{
            _canvas.drawPoint(_mX, _mY, _paint);
        }
        _path.reset();
    }
}