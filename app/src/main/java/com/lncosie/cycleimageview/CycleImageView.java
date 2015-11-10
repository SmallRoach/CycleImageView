package com.lncosie.cycleimageview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CycleImageView extends View {

    Animate direction = new RightToLeft();
    Drawable[] drawables;
    int nextImage = 0;
    Drawable first;
    Drawable second;
    int durtionShow = 3000;
    int durtionAnimate = 2000;
    ValueAnimator move;
    float fraction;
    ValueAnimator.AnimatorUpdateListener updater = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            fraction = animation.getAnimatedFraction();

            invalidate();
        }
    };
    Runnable animate = new Runnable() {
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
    Ender showNext = new Ender() {

        @Override
        public void onAnimationStart(Animator animation) {
            prepareNext();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            start();
        }
    };
    void prepareNext() {
        first = drawables[nextImage];
        nextImage = nextImage + 1;
        if (nextImage == drawables.length)
            nextImage = 0;
        second = drawables[nextImage];
    }

    public void start() {
        move = ValueAnimator.ofInt(100);
        postDelayed(animate, durtionShow);
    }
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
        setImages(android.R.drawable.ic_btn_speak_now, android.R.drawable.ic_popup_sync, android.R.drawable.ic_dialog_email);
        start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int w = getWidth() - paddingLeft - paddingRight;
        int h = getHeight() - paddingTop - paddingBottom;

        if (drawables != null) {
            if (first != null && second != null) {
                direction.step(w, h);
                first.draw(canvas);
                second.draw(canvas);
            }
        }
    }

    public void setDirection(Direction dir) {
        if (dir == Direction.LEFT_TO_RIGHT)
            direction = new LeftToRight();
        else
            direction = new RightToLeft();
    }

    public void setImages(int... images) {
        drawables = new Drawable[images.length];
        int step = 0;
        for (int id : images) {
            drawables[step] = getResources().getDrawable(id);
            step = step + 1;
        }
        nextImage = 0;
        first=drawables[0];
        second=first;
    }


    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT;
    }
    interface Animate {
        void step(int width, int height);
    }

    static class Ender implements Animator.AnimatorListener {

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

    class LeftToRight implements Animate {
        @Override
        public void step(int width, int height) {
            second.setBounds((int) (width * (fraction - 1)), 0, (int) (width * (fraction)), height);
            first.setBounds((int) (width * (fraction)), 0, (int) (width * (1.0 + fraction)), height);
        }
    }

    class RightToLeft implements Animate {
        @Override
        public void step(int width, int height) {
            second.setBounds((int) (width * (1 - fraction)), 0, (int) (width * (2 - fraction)), height);
            first.setBounds((int) (width * (-fraction)), 0, (int) (width * (1.0 - fraction)), height);
        }
    }
}
