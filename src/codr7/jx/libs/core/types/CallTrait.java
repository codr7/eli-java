package codr7.jx.libs.core.types;

import codr7.jx.IValue;
import codr7.jx.Location;
import codr7.jx.VM;

public interface CallTrait {
    void call(VM vm, IValue target, int rArguments, int rResult, Location location);
}
