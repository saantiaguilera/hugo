package com.example.hugo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
import hugo.weaving.anotations.DebugRenderLog;

/**
 * Created by saguilera on 7/22/16.
 */
@DebugRenderLog
public class HugoTextView extends TextView {

    public HugoTextView(Context context) {
        super(context);
    }

    public HugoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setText("HUGOTEXTVIEW TEST");
    }

    public HugoTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
