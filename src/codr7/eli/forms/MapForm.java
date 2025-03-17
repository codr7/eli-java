package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.*;

import java.util.ArrayList;
import java.util.TreeMap;

public class MapForm extends BaseForm {
    public final IForm[] items;

    public MapForm(final IForm[] items, final Loc loc) {
        super(loc);
        this.items = items;
    }

    @Override
    public void bindValue(final VM vm, final IValue value, final Loc loc) {
        final var m = value.cast(CoreLib.Map);

        for (final var it: items) {
            switch (it) {
                case IdForm f: {
                    final var v = m.get(new Value<>(CoreLib.Sym, f.id));

                    if (v == null) {
                        throw new EvalError("Failed to bind: " + f.dump(vm), it.loc());
                    }

                    it.bindValue(vm, v, it.loc());
                    break;
                }
                case PairForm f: {
                    final var srcId = f.right.cast(vm, IdForm.class).id;
                    final var v = m.get(new Value<>(CoreLib.Sym, srcId));

                    if (v == null) {
                        throw new EvalError("Failed to bind id: " + f.dump(vm), it.loc());
                    }

                    f.left.bindValue(vm, v, f.left.loc());
                    break;
                }
                default:
                    throw new EvalError("Expected id or pair: " + it.dump(vm), it.loc());
            }
        }
    }

    @Override
    public void bindRegister(final VM vm, final int rValue, final IType type, final Loc loc) {
        for (final var it: items) {
            switch (it) {
                case IdForm f: {
                    final var rItem = vm.alloc(1);
                    vm.emit(new MapGet(rValue, new Value<>(CoreLib.Sym, f.id), rItem, it.loc()));
                    it.bindRegister(vm, rItem, null, it.loc());
                    break;
                }
                case PairForm f: {
                    final var srcId = f.right.cast(vm, IdForm.class).id;
                    final var rItem = vm.alloc(1);
                    vm.emit(new MapGet(rValue, new Value<>(CoreLib.Sym, srcId), rItem, it.loc()));
                    f.left.bindRegister(vm, rItem, null, f.left.loc());
                    break;
                }
                default:
                    throw new EvalError("Expected id or pair: " + it.dump(vm), it.loc());
            }
        }
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            new Value<>(CoreLib.Map, new TreeMap<>()).emit(vm, rResult, loc());
            final var rItem = vm.alloc(1);

            for (final var it : items) {
                it.emit(vm, rItem);
                vm.emit(new MapSet(rResult, rItem, it.loc()));
            }
        } else {
            v.emit(vm, rResult, loc());
        }
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof MapForm f) {
            if (f.items.length != items.length) {
                return false;
            }

            for (var i = 0; i < items.length; i++) {
                if (!f.items[i].eq(items[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String dump(VM vm) {
        final var result = new StringBuilder();
        result.append('{');

        for (var i = 0; i < items.length; i++) {
            if (i > 0) {
                result.append(' ');
            }

            result.append(items[i].dump(vm));
        }

        result.append('}');
        return result.toString();
    }

    @Override
    public IValue quote(final VM vm, final Loc loc) {
        final var result = new TreeMap<IValue, IValue>();

        for (final var it : items) {
            final var p = it.quote(vm, loc).cast(CoreLib.Pair);
            result.put(p.left(), p.right());
        }

        return new Value<>(CoreLib.Map, result);
    }

    @Override
    public IValue rawValue(VM vm) {
        final var vs = Form.rawValues(vm, items);

        if (vs == null) {
            return null;
        }

        final var out = new TreeMap<IValue, IValue>();

        vs.forEach(v -> {
            final var p = v.cast(CoreLib.Pair);
            out.put(p.left(), p.right());
        });

        return new Value<>(CoreLib.Map, out);
    }

    @Override
    public IValue value(VM vm) {
        final var vs = Form.values(vm, items);

        if (vs == null) {
            return null;
        }

        final var out = new TreeMap<IValue, IValue>();

        vs.forEach(v -> {
            final var p = v.cast(CoreLib.Pair);
            out.put(p.left(), p.right());
        });

        return new Value<>(CoreLib.Map, out);
    }
}