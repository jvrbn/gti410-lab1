package view;

import model.ObserverIF;
import model.Pixel;

import java.awt.image.BufferedImage;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {

    int cyan;
    int magenta;
    int jaune;
    int noir;
    ColorSlider cyanCS;
    ColorSlider magentaCS;
    ColorSlider jauneCS;
    ColorSlider noirCS;
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

    CMYKColorMediator(ColorDialogResult result, int imageWidth, int imageHeigth) {
        this.imageWidth = imageWidth;
        this.imageHeigth = imageHeigth;

        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();

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

    public void RGBToCMYK(int red, int green, int blue){

        cyan = 1 - red/255;
        cyan = 255 - noir - red/255;
        magenta = 1 - green/255;
        jaune = 1 - blue/255;
        noir = Math.max(Math.max(cyan, magenta),jaune);
    }

    public void computeCyanImage(int red, int green, int blue) {
        RGBToCMYK(red, green, blue);
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++) {
            p.setRed((int) ((255 - noir - (double)i/(double)imageWidth))/(255 - noir));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                cyanImage.setRGB(i, j, rgb);
            }
        }

        System.out.println("-----cyan----");
        System.out.println("color red:" + p.getRed());
        System.out.println("color green:" + p.getGreen());
        System.out.println("color blue:" + p.getBlue());
        System.out.println("-------------");

        if (cyanCS != null) {
            cyanCS.update(cyanImage);
        }
    }


    public void computeMagentaImage(int red, int green, int blue) {
        RGBToCMYK(red, green, blue);
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++) {
            p.setGreen((int) ((255 - noir - (double)i/(double)imageWidth))/(255 - noir));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                magentaImage.setRGB(i, j, rgb);
            }
        }

        System.out.println("----magenta-----");
        System.out.println("color red:" + p.getRed());
        System.out.println("color green:" + p.getGreen());
        System.out.println("color blue:" + p.getBlue());
        System.out.println("-------------");

        if (magentaCS != null) {
            magentaCS.update(magentaImage);
        }
    }

    public void computeJauneImage(int red, int green, int blue) {
        RGBToCMYK(red, green, blue);
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++) {
            p.setBlue((int) ((255 - noir - (double)i/(double)imageWidth))/(255 - noir));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                jauneImage.setRGB(i, j, rgb);
            }
        }

        System.out.println("----yellow----");
        System.out.println("color red:" + p.getRed());
        System.out.println("color green:" + p.getGreen());
        System.out.println("color blue:" + p.getBlue());
        System.out.println("-------------");

        if (jauneCS != null) {
            jauneCS.update(jauneImage);
        }
    }

    public void computeNoirImage(int red, int green, int blue) {
        RGBToCMYK(red, green, blue);
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i < imageWidth; i++) {
            noir = ((int) ((255 - noir - (double)i/(double)imageWidth))/(255 - noir));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                noirImage.setRGB(i, j, rgb);
            }
        }
        if (noirCS != null) {
            noirCS.update(noirImage);
        }
    }

    @Override
    public void update() {
        Pixel currentColor = new Pixel(red, green, blue,255);
        if (currentColor.getARGB() == result.getPixel().getARGB()) return;

        cyan = result.getPixel().getRed();
        magenta = result.getPixel().getGreen();
        jaune = result.getPixel().getBlue();
        noir = result.getPixel().getBlue();

        //System.out.println("color Cyan :"+ currentColor.getCyan());

        System.out.println("color red:" + result.getPixel().getRed());
        System.out.println("color green:" + result.getPixel().getGreen());
        System.out.println("color blue:" + result.getPixel().getBlue());


        cyanCS.setValue(cyan);
        magentaCS.setValue(magenta);
        jauneCS.setValue(jaune);
        noirCS.setValue(noir);
        computeCyanImage(red, green, blue);
        computeMagentaImage(red, green, blue);
        computeJauneImage(red, green, blue);
        computeNoirImage(red, green, blue);
    }

    @Override
    public void update(ColorSlider cs, int v) {
        boolean updateCyan = false;
        boolean updateMagenta = false;
        boolean updateJaune = false;
        boolean updateNoir = false;
        if (cs == cyanCS && v != cyan) {
            cyan = v;
            updateJaune = true;
            updateMagenta = true;
            updateNoir = true;
        }
        if (cs == magentaCS && v != magenta) {
            magenta = v;
            updateCyan = true;
            updateJaune = true;
            updateNoir = true;

        }
        if (cs == jauneCS && v != jaune) {
            jaune = v;
            updateCyan = true;
            updateMagenta = true;
            updateNoir = true;
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

        System.out.println("color red:" + result.getPixel().getRed());
        System.out.println("color green:" + result.getPixel().getGreen());
        System.out.println("color blue:" + result.getPixel().getBlue());
    }

    public BufferedImage getCyanImage() {
        return cyanImage;
    }

    public BufferedImage getMagentaImage() {
        return magentaImage;
    }

    public BufferedImage getJauneImage() {
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

    public int getCyan() {
        return cyan;
    }

    public int getMagenta() {
        return magenta;
    }

    public int getJaune() {
        return jaune;
    }

    public int getNoir() {
        return noir;
    }


}

