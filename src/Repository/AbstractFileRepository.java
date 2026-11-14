package Repository;

import domain.Entity;
import domain.Exceptions.RepositoryException;
import domain.Validation.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E>{
    private final String fileName;
    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    protected abstract E extractEntity(List<String> attributes);
    protected abstract String createEntityAsString(E entity);

    private void loadData() {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            return;
        }

        try (Stream<String> lines = Files.lines(path)) {
            lines.filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        try {
                            // Transformăm linia în List<String> folosind separatorul din Lab 3 (ex: ";" sau ",")
                            List<String> attributes = Arrays.asList(line.split(";"));
                            E entity = extractEntity(attributes);
                            super.save(entity);
                        } catch (Exception e) {
                            System.err.println("Warning: Corrupted line skipped -> " + line + " (" + e.getMessage() + ")");
                        }
                    });
        } catch (IOException e) {
            throw new RepositoryException("Error reading file: " + fileName, e);
        }
    }


    private void WriteData(){
        Path path = Paths.get(fileName);
        List<String> lines = StreamSupport.stream(findAll().spliterator(), false)
                .map(this::createEntityAsString)
                .collect(Collectors.toList());

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RepositoryException("Error writing file: " + fileName, e);
        }
    }

    @Override
    public E save(E entity) {
        E result = super.save(entity);
        WriteData();
        return result;
    }

    @Override
    public E delete(ID id) {
        E result = super.delete(id);
        WriteData();
        return result;
    }

    @Override
    public E update(E entity) {
        E result = super.update(entity);
        WriteData();
        return result;
    }

}
