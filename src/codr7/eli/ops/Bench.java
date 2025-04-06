package codr7.eli.ops;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

import java.time.Duration;

public record Bench(long reps, Label bodyEnd, int rResult, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Bench reps: " + reps + " bodyEnd: " + bodyEnd + " rResult: " + rResult;
    }

    @Override
    public void eval(final VM vm) {
        final var started = System.nanoTime();
        final var startPc = vm.pc + 1;

        for (var i = 0; i < reps; i++) {
            vm.eval(startPc, bodyEnd.pc);
        }

        final var elapsed = Duration.ofNanos(System.nanoTime() - started);
        vm.registers.set(rResult, new Value<>(CoreLib.Time, elapsed));
        vm.pc = bodyEnd.pc;
    }
}