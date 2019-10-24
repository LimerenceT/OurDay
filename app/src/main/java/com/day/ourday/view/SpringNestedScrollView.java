package com.day.ourday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

/**
 * Created by long.qiu on 19-9-17
 */
public class SpringNestedScrollView extends NestedScrollView {
    private float startDragY;
    private SpringAnimation springAnim;

    public SpringNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        springAnim = new SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0);
        //刚度 默认1200 值越大回弹的速度越快
        springAnim.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
        springAnim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() <= 0) {
                    //顶部下拉
                    if (startDragY == 0) {
                        startDragY = e.getRawY();
                    }
                    if (e.getRawY() - startDragY > 0) {
                        setTranslationY((e.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                } else if ((getScrollY() + getHeight()) >= getChildAt(0).getMeasuredHeight()) {
                    //底部上拉
                    if (startDragY == 0) {
                        startDragY = e.getRawY();
                    }
                    if (e.getRawY() - startDragY < 0) {
                        setTranslationY((e.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getTranslationY() != 0) {
                    springAnim.start();
                }
                startDragY = 0;
                break;
        }
        return super.onTouchEvent(e);
    }

}
