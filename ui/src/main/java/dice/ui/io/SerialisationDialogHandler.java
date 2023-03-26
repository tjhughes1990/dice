package dice.ui.io;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import dice.core.io.DiceCollectionBinarySerialiser;
import dice.core.io.DiceCollectionSerialiser;
import dice.core.types.DiceCollection;
import dice.ui.SceneMgr;
import dice.ui.Utils;

public class SerialisationDialogHandler {

    private SerialisationDialogHandler() {
    }

    public static void saveDiceCollectionList(final List<DiceCollection> diceCollectionList) {
        final FileChooser fileChooser = createFileChooser();

        final File file = fileChooser.showSaveDialog(SceneMgr.INSTANCE.getStage());
        if (file != null) {
            final DiceCollectionSerialiser serialiser = new DiceCollectionBinarySerialiser();
            serialiser.serialise(file, diceCollectionList);
        }
    }

    private static FileChooser createFileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Utils.RESOURCES.getString("save_file_dlg_title"));
        final String diceFileLbl = Utils.RESOURCES.getString("save_file_dlg_lbl");
        final String diceFileExt = "*." + DiceCollectionBinarySerialiser.EXT;
        fileChooser.getExtensionFilters().add(new ExtensionFilter(diceFileLbl, diceFileExt));
        fileChooser.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());

        return fileChooser;
    }

    public static Optional<List<DiceCollection>> loadDiceCollectionList() {
        final FileChooser fileChooser = createFileChooser();

        final File file = fileChooser.showOpenDialog(SceneMgr.INSTANCE.getStage());
        if (file != null) {
            final DiceCollectionSerialiser serialiser = new DiceCollectionBinarySerialiser();
            return serialiser.deserialise(file);
        }

        return Optional.empty();
    }
}
