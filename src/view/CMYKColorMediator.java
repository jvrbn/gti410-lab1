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
        this.result = result;
        result.addObserver(this);
        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();

        cyanImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        magentaImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        jauneImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);
        noirImage = new BufferedImage(imageHeigth, imageHeigth, BufferedImage.TYPE_INT_ARGB);

        int rgbToCMYK[] = RGBtoCMYK(red, green, blue);

        this.noir = rgbToCMYK[3];

        computeCyanImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
        computeMagentaImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
        computeJauneImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
        computeNoirImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
    }

    /**
     * Code inspiré de l'algorithme de ce site web :
     * https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
     * Conversion de la couleur en RGB vers CMYK
     * @param red Couleur rouge
     * @param green Couleur verte
     * @param blue Couleur bleu
     * @return Tableau contenant les couleurs cyan, magenta, jaune et noir
     */
    public int[] RGBtoCMYK(int red, int green, int blue){

        noir = Math.max(Math.max(1 - red/255, 1 - green/255),1 - blue/255);
        cyan = (255 - noir - red/255)/(255 - noir);
        magenta = (255 - noir - green/255)/(255 - noir);
        jaune = (255 - noir - blue/255)/(255 - noir);

        int rgbToCMYK[] = new int[4];
        rgbToCMYK[0] = cyan;
        rgbToCMYK[1] = jaune;
        rgbToCMYK[2] = magenta;
        rgbToCMYK[3] = noir;

        return rgbToCMYK;
    }

    /**
     * Code inspiré de l'algorithme de ce site web :
     * https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
     * Conversion de la couleur en CMYK vers RGB
     * @param cyan Couleur cyan
     * @param magenta Couleur magenta
     * @param jaune Couleur jaune
     * @param noir Ton noir
     * @return Tableau contenant les couleurs red, green, blue
     */
    public int[] CMYKtoRGB(int cyan, int magenta, int jaune, int noir){

        int cmykToRGB[] = new int[3];

        cmykToRGB[0] = Math.round(((255 - cyan)*(255-noir))/255);
        cmykToRGB[1] = Math.round(((255 - magenta)*(255-noir))/255);
        cmykToRGB[2] = Math.round(((255 - jaune)*(255-noir))/255);

        return cmykToRGB;
    }

    public void computeCyanImage(int cyan, int magenta, int jaune, int noir) {
        int cmykInRGB[] =  CMYKtoRGB(cyan, magenta, jaune, noir);
        Pixel p = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2], 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setRed((int)(255 - noir - ((double) i / (double) imageWidth * (255 - noir))));
            int rgb = p.getARGB();
            System.out.println("RGB cyan: " + rgb);
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


    public void computeMagentaImage(int cyan, int magenta, int jaune, int noir) {
        int cmykInRGB[] =  CMYKtoRGB(cyan, magenta, jaune, noir);
        Pixel p = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2], 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setGreen((int)(255 - noir - ((double) i / (double) imageWidth * (255 - noir))));
            int rgb = p.getARGB();
            System.out.println("RGB magenta : " + rgb);
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

    public void computeJauneImage(int cyan, int magenta, int jaune, int noir) {
        int cmykInRGB[] =  CMYKtoRGB(cyan, magenta, jaune, noir);
        Pixel p = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2], 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setBlue((int)(255 - noir - ((double) i / (double) imageWidth * (255 - noir))));
            int rgb = p.getARGB();
            System.out.println("RGB jaune: " + rgb);
            for (int j = 0; j < imageHeigth; j++) {
                jauneImage.setRGB(i, j, rgb);
            }
        }

        System.out.println("----jaune----");
        System.out.println("color red:" + p.getRed());
        System.out.println("color green:" + p.getGreen());
        System.out.println("color blue:" + p.getBlue());
        System.out.println("-------------");

        if (jauneCS != null) {
            jauneCS.update(jauneImage);
        }
    }

    public void computeNoirImage(int cyan, int magenta, int jaune, int noir) {
        int cmykInRGB[] =  CMYKtoRGB(cyan, magenta, jaune, noir);
        Pixel p = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);

        for (int i = 0; i < imageWidth; i++) {

            int currentColor = (int)Math.round((((double) i / (double)imageWidth))*255);
            cmykInRGB = CMYKtoRGB(cyan, magenta, jaune, currentColor);
            p.setRed(cmykInRGB[0]);
            p.setGreen(cmykInRGB[1]);
            p.setBlue(cmykInRGB[2]);
            int rgb = p.getARGB();
            System.out.println("RGB noir: " + rgb);
            for (int j = 0; j < imageHeigth; j++) {
               //noirImage.setRGB(i, j, rgb);
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

        red = result.getPixel().getRed();
        green = result.getPixel().getGreen();
        blue = result.getPixel().getBlue();
        int rgbToCMYK[] = RGBtoCMYK(red, green, blue);

        cyanCS.setValue(rgbToCMYK[0]);
        magentaCS.setValue(rgbToCMYK[1]);
        jauneCS.setValue(rgbToCMYK[2]);
        noirCS.setValue(rgbToCMYK[3]);

        computeCyanImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
        computeMagentaImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
        computeJauneImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
        computeNoirImage(rgbToCMYK[0], rgbToCMYK[1], rgbToCMYK[2], rgbToCMYK[3]);
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
            computeCyanImage(cyan, magenta, jaune, noir);
        }
        if (updateMagenta) {
            computeMagentaImage(cyan, magenta, jaune, noir);
        }
        if (updateJaune) {
            computeJauneImage(cyan, magenta, jaune, noir);
        }

        if (updateNoir) {
            computeNoirImage(cyan, magenta, jaune, noir);
        }

        int cmykInRGB[] = CMYKtoRGB(cyan, magenta, jaune, noir);
        Pixel currentColor = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2], 255);
        result.setPixel(currentColor);
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

