package codr7.eli;

import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Stream;

public enum Form {
    ;

    public static String dump(final VM vm, final Stream<IForm> forms) {
        return '<' + String.join(" ", forms.map(f -> f.dump(vm)).toList()) + '>';
    }

    public static String dump(final VM vm, final Deque<IForm> forms) {
        return dump(vm, forms.stream());
    }

    public static String dump(final VM vm, final IForm[] forms) {
        return dump(vm, Arrays.stream(forms));
    }
}
