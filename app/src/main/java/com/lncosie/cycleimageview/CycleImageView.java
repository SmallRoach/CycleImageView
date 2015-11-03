package com.lncosie.cycleimageview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * TODO: document your custom view class.
 */
public class CycleImageView extends FrameLayout {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable drawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    ClipDrawable clipDrawable;

    public CycleImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public CycleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CycleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CycleImageView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CycleImageView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.CycleImageView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CycleImageView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.CycleImageView_exampleDrawable)) {
            drawable = a.getDrawable(
                    R.styleable.CycleImageView_exampleDrawable);


            clipDrawable =new ClipDrawable(drawable, Gravity.RIGHT,ClipDrawable.HORIZONTAL);
            clipDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
        setImages(android.R.drawable.ic_btn_speak_now, android.R.drawable.ic_delete,android.R.drawable.ic_btn_speak_now);
        start();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int w = getWidth() - paddingLeft - paddingRight;
        int h = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(String.valueOf(fraction),
                paddingLeft + (w - mTextWidth) / 2,
                paddingTop + (h + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (drawable != null) {
//            drawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
            drawable.setBounds(-w / 2, 0,
                    w  / 2, h);

            //clipDrawable.setLevel(10000);
            drawable.draw(canvas);
            if(first!=null&&second!=null){

                second.setBounds((int)(w*(fraction-1)),0,(int)(w*(fraction)),h);
                first.setBounds((int)(w*(fraction)),0,(int)(w*(1.0+fraction)),h);
                first.draw(canvas);
                second.draw(canvas);
            }
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return drawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        drawable = exampleDrawable;
    }
    Drawable[]  drawables;
    int nextImage=0;
    void setImages(int... images){
        drawables= new Drawable[images.length];
        int step=0;
        for(int id:images){
            drawables[step]=getResources().getDrawable(id);
            step=step+1;
        }


        nextImage=0;
        prepareNext();
    }
    Drawable    first;
    Drawable    second;

    int         durtionShow=3000;
    int         durtionAnimate=2000;
    void prepareNext(){
        first=drawables[nextImage];
        nextImage=nextImage+1;
        if(nextImage==drawables.length)
            nextImage=0;
        second=drawables[nextImage];

    }

    void start(){

        move=ValueAnimator.ofInt(100);
        postDelayed(animate,durtionShow);
    }
    ValueAnimator move;
    float fraction;
    ValueAnimator.AnimatorUpdateListener updater=new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            fraction=animation.getAnimatedFraction();

            invalidate();
        }
    };
    Ender showNext=new Ender() {
        public void onAnimationEnd(Animator animation) {
           start();
        }

        public void onAnimationStart(Animator animation) {
            prepareNext();
        }
    };
    Runnable   animate=new Runnable() {
        @Override
        public void run() {

            move.addListener(showNext);
            move.addUpdateListener(updater);
            //move.setStartDelay(durtionShow);
            move.setDuration(durtionAnimate);
            //.setInterpolator(new AccelerateDecelerateInterpolator());
            move.start();
        }
    };
    static class Ender implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
