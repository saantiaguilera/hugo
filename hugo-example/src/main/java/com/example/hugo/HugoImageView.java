package com.example.hugo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import hugo.weaving.anotations.DebugGenericLog;

/**
 * Created by saguilera on 7/22/16.
 */
@DebugGenericLog
public class HugoImageView extends ImageView {

    public HugoImageView(Context context) {
        super(context);
    }

    public HugoImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HugoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
