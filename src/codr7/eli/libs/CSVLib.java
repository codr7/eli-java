package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public final class CSVLib extends Lib {
    public CSVLib() {
        super("csv", null);

        bindMethod("open-file", new Arg[]{new Arg("path", CoreLib.stringType)},
                (vm, args, rResult, loc) -> {
                    try {
                        final var path = args[0].cast(CoreLib.stringType);
                        final var file = new FileReader(vm.path.resolve(path).toString(), StandardCharsets.UTF_8);
                        //final var parser = new CSVParser(file, CSVFormat.DEFAULT);
                        //vm.registers.set(rResult, new Value<>(CoreLib.iterType, new RecordReader(parser.iterator())));
                    }
                    catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}