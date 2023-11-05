package dice.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dice.core.types.DiceCollection;

public class DiceCollectionBinarySerialiser implements DiceCollectionSerialiser {

    public static final String EXT = "dic";

    @Override
    public boolean serialise(final File file, final List<DiceCollection> diceCollectionList) {
        final File absFile = file.getAbsoluteFile();
        if (!EXT.equals(getExtension(absFile))) {
            return false;
        }

        try (final FileOutputStream fileOutputStream = new FileOutputStream(absFile);
             final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {

            objectOutputStream.writeInt(diceCollectionList.size());
            for (final DiceCollection diceCollection : diceCollectionList) {
                objectOutputStream.writeObject(diceCollection);
            }
            objectOutputStream.flush();

            return true;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getExtension(final File file) {
        final String filePath = file.toString();
        final int separatorIndex = filePath.lastIndexOf('.');

        return separatorIndex > 0 ? filePath.substring(separatorIndex + 1) : "";
    }

    @Override
    public Optional<List<DiceCollection>> deserialise(final File file) {
        final File absFile = file.getAbsoluteFile();
        if (!EXT.equals(getExtension(absFile))) {
            return Optional.empty();
        }

        try (FileInputStream fileInputStream = new FileInputStream(absFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

            final List<DiceCollection> diceCollectionList = new ArrayList<>();

            final int count = objectInputStream.readInt();
            for (int i = 0; i < count; i++) {
                diceCollectionList.add((DiceCollection) objectInputStream.readObject());
            }

            return Optional.of(diceCollectionList);
        } catch (final ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
