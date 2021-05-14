package nothing.fighur.eddie.folder;

import java.io.*;

public class FilePersistenceProvider implements  PersistenceProvider {
    @Override
    public void persist(CharSequence text, String key) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(key));
        writer.write(text.toString());
        writer.close();
    }

    @Override
    public CharSequence load(String key) throws IOException {
        File file = new File(key);

        if (file.createNewFile()) {
            return "";
        } else {
            StringBuilder text = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(key));
            String content = reader.readLine();
            while (content != null) {
                text.append(content);
                text.append('\n');
                content = reader.readLine();
            }
            reader.close();
            return text;
        }
    }
}
