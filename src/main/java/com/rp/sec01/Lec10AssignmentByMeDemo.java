package com.rp.sec01;

import com.rp.courseutil.Util;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Lec10AssignmentByMeDemo {
    public static void main(String[] args) {
//        readFile().subscribeOn(Schedulers.boundedElastic())
//                .subscribe(Util.onNext(),
//                        Util.onError(),
//                        Util.onComplete());
//
//        createFileAndWriteStuff().subscribeOn(Schedulers.boundedElastic())
//                .subscribe(Util.onNext(),
//                        Util.onError(),
//                        Util.onComplete());

        deleteFile("")//.subscribeOn(Schedulers.boundedElastic()).block();
                .subscribe(Util.onNext(),
                        Util.onError(),
                        Util.onComplete());
    }

    private static Mono<String> readFile() {
        return Mono.fromFuture(Lec10AssignmentByMeDemo::getFileContents);
    }

    private static CompletableFuture<String> getFileContents() {
        String fileName = "sec01/file1.txt";
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Files.readString(Path.of(fileName));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        });
    }

    private static Mono<Void> createFileAndWriteStuff() {
        return Mono.fromRunnable(() -> {
            Path newFilePath = Paths.get("sec01/file3.txt");
            createNewFile(newFilePath).thenApplyAsync((path) -> {
                String content = "hello world !!";
                try {
                    Files.writeString(path, content);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                return path;
            });
        });
    }

    private static CompletableFuture<Path> createNewFile(Path filepath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Files.createFile(filepath);
            } catch (IOException e) {
                return Path.of("");
            }
        });
    }

    private static Mono<Boolean> deleteFile(String path) {
        return Mono.fromSupplier(() -> {
            var deleteFile = deleteFileFuture(path)
                    .thenAccept((isDeleted) -> {
                        System.out.println(isDeleted);
                        if (!isDeleted) throw new RuntimeException();
                    }).isCompletedExceptionally();
            if (deleteFile) {
                throw new RuntimeException();
            }
            return true;
        });
    }

    private static CompletableFuture<Boolean> deleteFileFuture(String path) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Files.deleteIfExists(
                        Paths.get(path));
            } catch (Exception e) {
                return false;
            }
        });
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    private static void printInputStream(InputStream is) {

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
