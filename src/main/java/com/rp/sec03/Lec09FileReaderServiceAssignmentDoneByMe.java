package com.rp.sec03;

import com.rp.courseutil.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//@Slf4j
public class Lec09FileReaderServiceAssignmentDoneByMe {

    private static final Path PATH = Paths.get("src/main/resources/assignment/sec03");

    public static void main(String[] args) {
        Flux.generate(
                        () -> getReaders("file01.txt"),
                        (readers, sink) -> {
                            if (Objects.isNull(readers)) {
                                sink.complete();
                            }
                            try {
                                String line = getLine((BufferedReader) readers.get(1));
                                if (line == null) {
                                    sink.complete();
                                }else{
                                    sink.next(line);
                                }
                            } catch (IOException e) {
                                sink.error(e);
                            }
                            return readers;
                        },
                        readers -> {
                            try {
                                closeConnection((BufferedReader) readers.get(1));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                )
                .subscribe(Util.subscriber());
    }

    private static String getLine(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    private static void closeConnection(BufferedReader reader) throws IOException {
        reader.close();
    }

    private static List<Reader> getReaders(final String fileName) throws FileNotFoundException {
        try {
            File file = new File(PATH.resolve(fileName).toString());
            FileReader fr = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);
            return List.of(fr, br);
        } catch (FileNotFoundException e) {
            throw e;
        }
    }
}
