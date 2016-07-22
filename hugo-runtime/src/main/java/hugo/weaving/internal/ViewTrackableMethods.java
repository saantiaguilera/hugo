package hugo.weaving.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saguilera on 7/22/16.
 */
public final class ViewTrackableMethods {

    private static final String METHOD_MEASURE = "measure";
    private static final String METHOD_LAYOUT = "layout";
    private static final String METHOD_DRAW = "draw";
    private static final String METHOD_DISPATCH_DRAW = "dispatchDraw";

    public static final List<String> methodsName;

    static {
        methodsName = new ArrayList<>();
        methodsName.add(METHOD_MEASURE);
        methodsName.add(METHOD_LAYOUT);
        methodsName.add(METHOD_DISPATCH_DRAW); //Testing
        methodsName.add(METHOD_DRAW);
    }

}
