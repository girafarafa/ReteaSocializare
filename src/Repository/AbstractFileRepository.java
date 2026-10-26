package Repository;

import domain.Entity;
import domain.Exceptions.RepositoryException;
import domain.Validation.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.time.format.DateTimeParseException;

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
        File file = new File(fileName);
        if (!file.exists()) {
            return; // Fișierul nu există încă; repository-ul rămâne gol
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                try {
                    E entity = extractEntity(Arrays.asList(line.split(";")));
                    super.save(entity);
                } catch (Exception e) {
                    // Raportezi linia coruptă fără a opri aplicația
                    System.err.println("Atenție: Linia " + lineNumber + " din " + fileName +
                            " este coruptă și a fost ignorată. Motiv: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citirea fișierului " + fileName, e);
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
        }catch (DateTimeParseException e) {
            throw new RepositoryException("Format dată invalid în fișier: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new RepositoryException("Format ID invalid în fișier: " + e.getMessage(), e);
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
