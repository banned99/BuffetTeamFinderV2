package com.buffet.customs;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by YaYaTripleSix on 22-Oct-16.
 */

public class CustomNestedScrollView extends NestedScrollView {
    public CustomNestedScrollView(Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Log.i("CustomScrollView", "onInterceptTouchEvent: DOWN super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                return false; // redirect MotionEvents to ourself

            case MotionEvent.ACTION_CANCEL:
                // Log.i("CustomScrollView", "onInterceptTouchEvent: CANCEL super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                //Log.i("CustomScrollView", "onInterceptTouchEvent: UP super false" );
                return false;

            default:
                //Log.i("CustomScrollView", "onInterceptTouchEvent: " + action );
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        //Log.i("CustomScrollView", "onTouchEvent. action: " + ev.getAction() );
        return true;
    }
}