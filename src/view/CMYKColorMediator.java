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
    int red;
    int green;
    int blue;
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
        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();
        this.noir = result.getPixel().getNoir();
        this.result = result;
        result.addObserver(this);

        cyanImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        magentaImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        jauneImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        noirImage = new BufferedImage(imageHeigth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        computeCyanImage(red, green, blue);
        computeMagentaImage(red, green, blue);
        computeJauneImage(red, green, blue);
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

        System.out.println("slider :"+ cyan);
        System.out.println("color:" + (1 - p.getRed()/255));
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
        if (magentaCS != null) {
            magentaCS.update(magentaImage);
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
        if (jauneCS != null) {
            jauneCS.update(jauneImage);
        }
    }

    public void computeNoirImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++){
            p.setNoir((int)(((double) i / (double)imageWidth)*255.0));
            int rgb = p.getARGB();
            for (int j = 0; j<imageHeigth; j++) {
                noirImage.setRGB(i,j,rgb);
            }
        }
        if (noirCS != null) {
            noirCS.update(noirImage);
        }
    }

    @Override
    public void update() {
        Pixel currentColor = new Pixel(red, green, blue);
        if(currentColor.getARGB() == result.getPixel().getARGB()) return;

        cyan = (1 - result.getPixel().getRed()/255 - result.getPixel().getNoir());
        magenta = 1 - result.getPixel().getGreen()/255;
        jaune = 1 - result.getPixel().getBlue()/255;
        noir = result.getPixel().getNoir();

        cyanCS.setValue(cyan);
        magentaCS.setValue(magenta);
        jauneCS.setValue(jaune);
        noirCS.setValue(noir);
        computeCyanImage(red, green, blue);
        computeMagentaImage(red, green, blue);
        computeJauneImage(red, green, blue);
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
            computeCyanImage(red, green, blue);
        }
        if (updateMagenta) {
            computeMagentaImage(red, green, blue);
        }
        if (updateJaune) {
            computeJauneImage(red, green, blue);
        }

        if (updateNoir) {
            computeNoirImage(red, green, blue);
        }

        Pixel pixel = new Pixel(red, green, blue, 255);
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

