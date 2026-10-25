package Repository;

import domain.Entity;
import domain.Exceptions.RepositoryException;
import domain.Validation.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E>{
    private final String fileName;
    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    protected abstract E extractEntity(List<String> attributes);
    protected abstract String createEntityAsString(E entity);

    private void loadData(){
        File file = new File(fileName);
        if(!file.exists()){
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line=reader.readLine()) != null){
                if(line.trim().isEmpty()) continue;

                List<String> attributes = Arrays.asList(line.split(";"));
                E entity = extractEntity(attributes);
                super.save(entity);
            }
        }catch (IOException e){
            throw new RepositoryException("Error reading from file: " + e.getMessage());
        }
    }


    private void WriteData(){
        try (BufferedWriter writer= new BufferedWriter(new FileWriter(fileName))){
            for(E entity : super.findAll()){
                String entityAsString = createEntityAsString(entity);
                writer.write(entityAsString);
                writer.newLine();
            }
        } catch (IOException e){
            throw new RepositoryException("Error writing to file: " + e.getMessage());
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
