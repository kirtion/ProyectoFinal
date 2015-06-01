package es.chinonymous.clientemovil.fragments.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TouchPadView extends View {

    private Context context;

	public TouchPadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        this.context = context;
	}

	public TouchPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context = context;
	}

	public TouchPadView(Context context) {
		super(context);
        this.context = context;
	}

	private int mWidth;
	private int mHeight;
	public float mCx, mCy;
	public float mRadius;
	public float mOffset;
	Paint paint = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRGB(0, 0, 0);
		//paint.setColor(0xffffffff);
        paint.setColor(0xff27aae2);
		paint.setStrokeWidth(15);
		paint.setStyle(Paint.Style.STROKE);
		for(int i = 1; i <= 6; i *= 6) {
            canvas.drawArc(new RectF(mCx-mRadius/i, mCy-mOffset-mRadius/i, mCx+mRadius/i, mCy-mOffset+mRadius/i), 180f, 180f, false, paint);
            canvas.drawArc(new RectF(mCx-mRadius/i, mCy+mOffset-mRadius/i, mCx+mRadius/i, mCy+mOffset+mRadius/i), 0f, 180f, false, paint);
		}
		canvas.drawLine(mCx+0.16666f*mRadius, mCy-mOffset, mCx+mRadius, mCy-mOffset, paint);
		canvas.drawLine(mCx-0.16666f*mRadius, mCy-mOffset, mCx-mRadius, mCy-mOffset, paint);
		canvas.drawLine(mCx+0.16666f*mRadius, mCy+mOffset, mCx+mRadius, mCy+mOffset, paint);
		canvas.drawLine(mCx-0.16666f*mRadius, mCy+mOffset, mCx-mRadius, mCy+mOffset, paint);
		canvas.drawLine(mCx, mCy+mOffset+0.16666f*mRadius, mCx, mCy+mOffset+mRadius, paint);
		canvas.drawLine(mCx, mCy-mOffset-0.16666f*mRadius, mCx, mCy-mOffset-mRadius, paint);
		canvas.drawLine(mCx+0.16666f*mRadius*0.70710f, mCy+mOffset+0.16666f*mRadius*0.70710f, mCx+mRadius*0.70710f, mCy+mOffset+mRadius*0.70710f, paint);
		canvas.drawLine(mCx-0.16666f*mRadius*0.70710f, mCy+mOffset+0.16666f*mRadius*0.70710f, mCx-mRadius*0.70710f, mCy+mOffset+mRadius*0.70710f, paint);
		canvas.drawLine(mCx+0.16666f*mRadius*0.70710f, mCy-mOffset-0.16666f*mRadius*0.70710f, mCx+mRadius*0.70710f, mCy-mOffset-mRadius*0.70710f, paint);
		canvas.drawLine(mCx-0.16666f*mRadius*0.70710f, mCy-mOffset-0.16666f*mRadius*0.70710f, mCx-mRadius*0.70710f, mCy-mOffset-mRadius*0.70710f, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mWidth = w;
		mHeight = h;
		mCx = mWidth/2;
		mCy = mHeight/2;
		if (mHeight >= 1.2f*mWidth) {
			mRadius = 0.9f*mWidth*0.55f;
		} else {
			mRadius = 0.9f*mHeight/2;
		}
		mOffset = 25;
	}

}
