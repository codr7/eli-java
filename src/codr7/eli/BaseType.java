package codr7.eli;

import java.util.*;

public abstract class BaseType<T> implements IDataType<T> {
    public final String id;
    private final Set<IType> parentTypes = new HashSet<>();

    public BaseType(final String id, final IType[] parentTypes) {
        this.id = id;
        addParentType(this);

        for (final var pt : parentTypes) {
            pt.addParentTypes(this);
        }
    }

    @Override
    final public void addParentType(final IType type) {
        parentTypes.add(type);
    }

    @Override
    final public void addParentTypes(final IType childType) {
        for (final var pt : parentTypes) {
            childType.addParentType(pt);
        }
    }

    @Override
    public String dump(VM vm, IValue value) {
        return value.cast(this).toString();
    }

    @Override
    public boolean equalValues(IValue left, IValue right) {
        final var lv = left.cast(this);
        final var rv = right.cast(this);
        return Objects.equals(lv, rv);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean is(IValue left, IValue right) {
        return left.cast(this) == right.cast(this);
    }

    @Override
    public boolean isa(final IType type) {
        return parentTypes.contains(type);
    }

    @Override
    public int weight() {
        return parentTypes.size();
    }
}

