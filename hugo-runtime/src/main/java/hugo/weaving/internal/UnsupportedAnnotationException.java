package hugo.weaving.internal;

/**
 * Created by saguilera on 7/22/16.
 */
public class UnsupportedAnnotationException extends RuntimeException {

    public UnsupportedAnnotationException() {
        super();
    }

    public UnsupportedAnnotationException(String detailMessage) {
        super(detailMessage);
    }

    public UnsupportedAnnotationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnsupportedAnnotationException(Throwable throwable) {
        super(throwable);
    }

}
