package codr7.eli;

import codr7.eli.libs.CoreLib;

import java.util.*;

public class Lib {
    public final String id;
    public final Lib parentLib;
    public final Map<String, IValue> bindings = new HashMap<>();
    private boolean isInit = false;

    public Lib(final String id, final Lib parentLib) {
        this.id = id;
        this.parentLib = parentLib;
    }

    public void bind(final String id, IValue value) {
        if (value.type() == CoreLib.Method) {
            final var v = bindings.get(id);

            if (v != null) {
                if (v.type() == CoreLib.Method) {
                    final var d = new Dispatch(id);
                    d.add(v.cast(CoreLib.Method));
                    d.add(value.cast(CoreLib.Method));
                    bind(id, CoreLib.Dispatch, d);
                    return;
                } else if (v.type() == CoreLib.Dispatch) {
                    v.cast(CoreLib.Dispatch).add(value.cast(CoreLib.Method));
                    return;
                }
            }
        }

        bindings.put(id, value);
    }

    public <T> void bind(final String id, final IDataType<T> type, final T data) {
        bind(id, new Value<>(type, data));
    }

    public void bindMacro(final String id, final Arg[] arguments, final JMacro.Body body) {
        bind(new JMacro(id, arguments, body));
    }

    public void bindMethod(final String id, final Arg[] arguments, final JMethod.Body body) {
        bind(new JMethod(id, arguments, body));
    }

    public void bind(final IType value) {
        bind(value.id(), CoreLib.Meta, value);
    }

    public void bind(final JMacro value) {
        bind(value.id(), CoreLib.JMacro, value);
    }

    public void bind(final Lib value) {
        bind(value.id, CoreLib.Lib, value);
    }

    public void bind(final IMethod value) {
        final var id = value.id();
        final var v = bindings.get(id);

        if (v == null) {
            bind(id, CoreLib.Method, value);
        } else if (v.type() == CoreLib.Method) {
            final var d = new Dispatch(id);
            d.add(v.cast(CoreLib.Method));
            d.add(value);
            bind(id, CoreLib.Dispatch, d);
        } else if (v.type() == CoreLib.Dispatch) {
            v.cast(CoreLib.Dispatch).add(value);
        }
    }

    public boolean drop(final String id) {
        return bindings.remove(id) != null;
    }

    public IValue find(final String id) {
        final var v = bindings.get(id);
        return (v == null && parentLib != null) ? parentLib.find(id) : v;
    }

    public void importFrom(final Lib source, final Set<String> ids) {
        for (final var id : ids) {
            bind(id, source.find(id));
        }
    }

    public void importFrom(final Lib source, String... ids) {
        importFrom(source, new TreeSet<>(Arrays.stream(ids).toList()));
    }

    public void importFrom(final Lib source) {
        importFrom(source, source.bindings.keySet());
    }

    public void tryInit(final VM vm) {
        if (!isInit) {
            final var loc = new Loc(id + " init");
            vm.doLib(this, () -> init(vm, loc));
            isInit = true;
        }
    }

    protected void init(final VM vm, final Loc loc) {
    }
}





