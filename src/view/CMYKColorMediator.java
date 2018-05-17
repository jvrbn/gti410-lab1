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
    BufferedImage cyanImage;
    BufferedImage magentaImage;
    BufferedImage jauneImage;
    int imageWidth;
    int imageHeigth;
    ColorDialogResult result;

    CMYKColorMediator(ColorDialogResult result, int imageWidth, int imageHeigth){
        this.imageWidth = imageWidth;
        this.imageHeigth = imageHeigth;
        this.cyan = result.getPixel().getCyan();
        this.magenta = result.getPixel().getMagenta();
        this.jaune = result.getPixel().getJaune();
        this.result = result;
        result.addObserver(this);

        cyanImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        magentaImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        jauneImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);

    }

    public void computeCyanImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++){
            //p.setRed();

        }

    }

    @Override
    public void update() {

    }

    @Override
    public void update(ColorSlider cs, int v) {

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
}

