package codr7.eli;

import codr7.eli.errors.EmitError;
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

    public void bind(final String bid, final IValue value) {
        if (value.type() == CoreLib.Method) {
            final var v = bindings.get(bid);

            if (v != null) {
                final var m = value.cast(CoreLib.Method);

                if (v.type() == CoreLib.Method) {
                    bind(bid, CoreLib.Dispatch, new Dispatch(bid, v.cast(CoreLib.Method), m));
                    return;
                } else if (v.type() == CoreLib.Dispatch) {
                    final var d = v.cast(CoreLib.Dispatch);
                    final var ms = new IMethod[d.methods.length+1];
                    ms[0] = m;

                    for (var i = 0; i < d.methods.length; i++) {
                        ms[i+1] = d.methods[i];
                    }

                    bind(bid, CoreLib.Dispatch, new Dispatch(bid, ms));
                    return;
                }
            }
        }

        bindings.put(bid, value);
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
        bind(value.id(), CoreLib.Method, value);
    }

    public boolean drop(final String bid) {
        return bindings.remove(bid) != null;
    }

    public IValue find(final String id) {
        final var v = bindings.get(id);
        return (v == null && parentLib != null) ? parentLib.find(id) : v;
    }

    public void importFrom(final Lib source, final Set<String> ids, final Loc loc) {
        for (final var id : ids) {
            final var v = source.find(id);
            if (v == null) {
                throw new EmitError("Not found " + source.id + '/' + id, loc);
            }
            bind(id, v);
        }
    }

    public void importFrom(final Lib source, String[] ids, final Loc loc) {
        importFrom(source, new TreeSet<>(Arrays.stream(ids).toList()), loc);
    }

    public void importFrom(final Lib source, final Loc loc) {
        importFrom(source, source.bindings.keySet(), loc);
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





