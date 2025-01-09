package codr7.jx.libs;

import codr7.jx.Arg;
import codr7.jx.Lib;
import com.opencsv.CSVReaderHeaderAware;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public class CSV extends Lib {
    public CSV() {
        super("csv");

        bindMethod("open-file", new Arg[]{new Arg("path", Core.stringType)}, null,
                (vm, args, rResult, loc) -> {
                    try {
                        final var p = args[0].cast(Core.stringType);
                        final var vs = new CSVReaderHeaderAware(new FileReader(p, StandardCharsets.UTF_8)).readMap();
                        System.out.println(vs);
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}