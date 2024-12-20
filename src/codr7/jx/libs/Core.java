package codr7.jx.libs;

import codr7.jx.IValue;
import codr7.jx.Lib;
import codr7.jx.Value;
import codr7.jx.libs.core.types.*;

public class Core extends Lib {
    public static final BindingType bindingType = new BindingType("Binding");
    public static final BitType bitType = new BitType("Bit");
    public static final IntType intType = new IntType("Int");
    public static final LibType libType = new LibType("Lib");
    public static final MetaType metaType = new MetaType("Meta");
    public static final NilType nilType = new NilType("Nil");

    public static final IValue NIL = new Value<>(nilType, null);

    public Core() {
        super("core");

        bind(bindingType);
        bind(bitType);
        bind(intType);
        bind(libType);
        bind(metaType);
        bind(nilType);

        bind("_", NIL);

        bind("T", new Value<>(bitType, true));
        bind("F", new Value<>(bitType, false));
    }
}
