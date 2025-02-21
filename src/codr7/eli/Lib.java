package codr7.eli;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import codr7.eli.libs.CoreLib;

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

    public void bindMethod(final String id, final Arg[] arguments, final IType result, final JMethod.Body body) {
        bind(new JMethod(id, arguments, result, body));
    }

    public void bind(final IType value) { bind(value.id(), CoreLib.metaType, value); }
    public void bind(final JMacro value) { bind(value.id(), CoreLib.jMacroType, value); }
    public void bind(final JMethod value) { bind(value.id(), CoreLib.jMethodType, value); }
    public void bind(final Lib value) { bind(value.id, CoreLib.libType, value); }
    public void bind(final Method value) { bind(value.id(), CoreLib.methodType, value); }

    public boolean drop(final String id) { return bindings.remove(id) != null; }

    public IValue find(final String id) {
        final var v = bindings.get(id);
        return (v == null && parentLib != null) ? parentLib.find(id) : v;
    }

    public void importFrom(final Lib source, final Set<String> ids) {
        for (final var id: ids) { bindings.put(id, source.bindings.get(id)); }
    }

    public void importFrom(final Lib source) {
        importFrom(source, source.bindings.keySet());
    }
}





