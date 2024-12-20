package codr7.jx;

import java.util.HashMap;
import java.util.Map;
import codr7.jx.libs.Core;

public class Lib {
    public final String id;
    public final Lib parentLib;
    public final Map<String, IValue> bindings = new HashMap<>();

    public Lib(final String id) {
        this.id = id;
        this.parentLib = null;
    }

    public Lib(final Lib parentLib) {
        this.id = parentLib.id;
        this.parentLib = parentLib;
    }

    public void bind(final String id, final IValue value) { bindings.put(id, value); }

    public <T> void bind(final String id, final IDataType<T> type, final T data) {
        bind(id, new Value<>(type, data));
    }

    public void bindMacro(final String id, final Arg[] arguments, final IType result, final JMacro.Body body) {
        bind(new JMacro(id, arguments, result, body));
    }

    public void bind(final IType value) { bind(value.id(), Core.metaType, value); }

    public void bind(final JMacro value) { bind(value.id(), Core.jMacroType, value); }

    public void bind(final Lib value) { bind(value.id, Core.libType, value); }

    public IValue find(final String id) {
        final var v = bindings.get(id);
        return (v == null && parentLib != null) ? parentLib.find(id) : v;
    }

}





