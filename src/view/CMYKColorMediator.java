package view;

import model.ObserverIF;
import model.Pixel;

import java.awt.image.BufferedImage;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {

    ColorSlider cyanCS;
    ColorSlider magentaCS;
    ColorSlider jauneCS;
    ColorSlider noirCS;
    int cyan;
    int magenta;
    int jaune;
    int noir;
    BufferedImage cyanImage;
    BufferedImage magentaImage;
    BufferedImage jauneImage;
    BufferedImage noirImage;
    int imageWidth;
    int imageHeigth;
    ColorDialogResult result;

    CMYKColorMediator(ColorDialogResult result, int imageWidth, int imageHeigth){
        this.imageWidth = imageWidth;
        this.imageHeigth = imageHeigth;
        this.cyan = result.getPixel().getCyan();
        this.magenta = result.getPixel().getMagenta();
        this.jaune = result.getPixel().getJaune();
        this.noir = result.getPixel().getNoir();
        this.result = result;
        result.addObserver(this);

        cyanImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        magentaImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        jauneImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        noirImage = new BufferedImage(imageHeigth, imageHeigth, BufferedImage.TYPE_INT_ARGB);

    }

    public void computeCyanImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++){
            p.setCyan((int)(((double) i / (double)imageWidth)*255.0));
            int rgb = p.getARGB();
            for (int j = 0; j<imageHeigth; j++) {
                cyanImage.setRGB(i,j,rgb);
            }
        }
        if (cyanCS != null) {
            cyanCS.update(cyanImage);
        }
    }

    public void computeMagentaImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++){
            p.setMagenta((int)(((double) i / (double)imageWidth)*255.0));
            int rgb = p.getARGB();
            for (int j = 0; j<imageHeigth; j++) {
                magentaImage.setRGB(i,j,rgb);
            }
        }
        if (cyanCS != null) {
            cyanCS.update(cyanImage);
        }
    }

    public void computeJauneImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++){
            p.setJaune((int)(((double) i / (double)imageWidth)*255.0));
            int rgb = p.getARGB();
            for (int j = 0; j<imageHeigth; j++) {
                jauneImage.setRGB(i,j,rgb);
            }
        }
        if (cyanCS != null) {
            cyanCS.update(cyanImage);
        }
    }

    @Override
    public void update() {
        Pixel currentColor = new Pixel(cyan, magenta, jaune);
        if(currentColor.getARGB() == result.getPixel().getARGB()) return;

        cyan = result.getPixel().getCyan();
        magenta = result.getPixel().getMagenta();
        jaune = result.getPixel().getJaune();
        noir = result.getPixel().getNoir();

        cyanCS.setValue(cyan);
        magentaCS.setValue(magenta);
        jauneCS.setValue(jaune);
        noirCS.setValue(noir);
        computeCyanImage(cyan, magenta, jaune);
        computeMagentaImage(cyan, magenta, jaune);
        computeJauneImage(cyan, magenta, jaune);
    }

    @Override
    public void update(ColorSlider cs, int v) {
        boolean updateCyan = false;
        boolean updateMagenta = false;
        boolean updateJaune = false;
        boolean updateNoir = false;
        if (cs == cyanCS && v != cyan){
            cyan = v;
            updateJaune = true;
            updateMagenta = true;
        }
        if (cs == magentaCS && v != magenta) {
            magenta = v;
            updateCyan = true;
            updateJaune = true;

        }
        if (cs == jauneCS && v != jaune) {
            jaune = v;
            updateCyan = true;
            updateMagenta = true;
        }
        if (updateCyan) {
            computeCyanImage(cyan, magenta, jaune);
        }
        if (updateMagenta) {
            computeMagentaImage(cyan, magenta, jaune);
        }
        if (updateJaune) {
            computeJauneImage(cyan, magenta, jaune);
        }

        Pixel pixel = new Pixel(cyan, magenta, jaune, 255);
        result.setPixel(pixel);
    }

    public BufferedImage getCyanImage(){
        return cyanImage;
    }

    public BufferedImage getMagentaImage(){
        return magentaImage;
    }

    public BufferedImage getJauneImage(){
        return jauneImage;
    }

    public BufferedImage getNoirImage() {
        return noirImage;
    }

    public void setCyanCS(ColorSlider slider) {
        this.cyanCS = slider;
        slider.addObserver(this);
    }

    public void setMagentaCS(ColorSlider slider) {
        this.magentaCS = slider;
        slider.addObserver(this);
    }

    public void setJauneCS(ColorSlider slider) {
        this.jauneCS = slider;
        slider.addObserver(this);
    }

    public void setNoirCS(ColorSlider slider) {
        this.noirCS = slider;
        slider.addObserver(this);
    }
}

