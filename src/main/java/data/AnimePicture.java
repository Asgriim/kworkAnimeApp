package data;

import javafx.scene.control.Button;

import java.io.InputStream;

public class AnimePicture {
    InputStream file;
    String fileName;
    Button button;
    ImageType type;

    public AnimePicture(InputStream file, String fileName, ImageType type) {
        this.file = file;
        this.fileName = fileName;
        this.type = type;
    }


    public void setButton(Button button) {
        this.button = button;
    }

    public InputStream getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public Button getButton() {
        return button;
    }

    public ImageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AnimePicture{" +
                "file=" + file +
                ", fileName='" + fileName + '\'' +
                ", button=" + button +
                ", type=" + type +
                '}';
    }
}
