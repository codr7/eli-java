package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Value;
import codr7.eli.libs.csv.iters.RecordReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public class CSVLib extends Lib {
    public CSVLib() {
        super("csv");

        bindMethod("open-file", new Arg[]{new Arg("path", CoreLib.stringType)}, null,
                (vm, args, rResult, loc) -> {
                    try {
                        final var path = args[0].cast(CoreLib.stringType);
                        final var file = new FileReader(vm.path.resolve(path).toString(), StandardCharsets.UTF_8);
                        final var parser = new CSVParser(file, CSVFormat.DEFAULT);
                        vm.registers.set(rResult, new Value<>(CoreLib.iterType, new RecordReader(parser.iterator())));
                    }
                    catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}