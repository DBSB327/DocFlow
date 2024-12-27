package com.neoskat.docflow.extractor;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class OcrContentExtractor implements ContentExtractor {
    @Autowired
    private  Tesseract tesseract;

    @Override
    public String extract(String filePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        try {
            return tesseract.doOCR(bufferedImage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed";
    }
}
