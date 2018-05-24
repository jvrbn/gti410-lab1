package view;

import model.ObserverIF;
import model.Pixel;

import java.awt.image.BufferedImage;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {

    private int cyan;
    private int magenta;
    private int jaune;
    private int noir;
    private ColorSlider cyanCS;
    private ColorSlider magentaCS;
    private ColorSlider jauneCS;
    private ColorSlider noirCS;
    private int red;
    private int green;
    private int blue;
    private BufferedImage cyanImage;
    private BufferedImage magentaImage;
    private BufferedImage jauneImage;
    private BufferedImage noirImage;
    private int imageWidth;
    private int imageHeigth;
    private ColorDialogResult result;

    /**
     * Constructeur du mediateur CMYK, inspirée du médiateur RGB
     * @param result Le résultat du choix fait dans la barre de couleur
     * @param imageWidth Largeur de l'image
     * @param imageHeigth Hauteur de l'image
     */
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
        noirImage = new BufferedImage(imageWidth, imageHeigth, BufferedImage.TYPE_INT_ARGB);

        int rgbToCMYK[] = RGBtoCMYK(this.red, this.green, this.blue);
        this.cyan = rgbToCMYK[0];
        this.magenta = rgbToCMYK[1];
        this.jaune = rgbToCMYK[2];
        this.noir = rgbToCMYK[3];
        int cmykInRGB[] = CMYKtoRGB(this.cyan, this.magenta, this.jaune, this.noir);

        computeCyanImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        computeMagentaImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        computeJauneImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        computeNoirImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
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

        this.noir = Math.max(Math.max(1 - red/255, 1 - green/255),1 - blue/255);
        this.cyan = (255 - this.noir - red/255)/(255 - this.noir);
        this.magenta = (255 - this.noir - green/255)/(255 - this.noir);
        this.jaune = (255 - this.noir - blue/255)/(255 - this.noir);

        int rgbToCMYK[] = new int[4];
        rgbToCMYK[0] = this.cyan;
        rgbToCMYK[1] = this.jaune;
        rgbToCMYK[2] = this.magenta;
        rgbToCMYK[3] = this.noir;

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

        cmykToRGB[0] = Math.round(((255 - cyan)*(255-noir))/255); //red
        cmykToRGB[1] = Math.round(((255 - magenta)*(255-noir))/255); //green
        cmykToRGB[2] = Math.round(((255 - jaune)*(255-noir))/255); //blue

        return cmykToRGB;
    }

    public void computeCyanImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue, 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setRed((int) (255 - this.noir - ((double) i / (double) imageWidth) * (255 - this.noir)));
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
        Pixel p = new Pixel(red, green, blue, 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setGreen((int) (255 - this.noir - ((double) i / (double) imageWidth) * (255 - this.noir)));
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

    public void computeJauneImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setBlue((int)(255 - noir - ((double) i / (double) imageWidth * (255 - noir))));
            int rgb = p.getARGB();
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

    public void computeNoirImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue);

        for (int i = 0; i < imageWidth; i++) {
            p.setRed((int) (255 - this.noir - ((double) i / (double) imageWidth) * (255 - this.noir)));
            p.setGreen((int) (255 - this.noir - ((double) i / (double) imageWidth) * (255 - this.noir)));
            p.setBlue((int)(255 - this.noir - ((double) i / (double) imageWidth * (255 - this.noir))));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
               noirImage.setRGB(i, j, rgb);
            }
        }
        if (noirCS != null) {
            noirCS.update(noirImage);
        }
    }

    /**
     * Mettre à jour la couleur afficher, on prend la valeur actuelle de CMYK puis on la transforme
     * en RGB. On donne la valeur en CMYK au ColorSlider et on recalcule la nouvelle couleur
     */
    @Override
    public void update() {
        int cmykInRGB[] = CMYKtoRGB(this.cyan, this.magenta, this.jaune, this.noir);

        Pixel currentColor = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2],255);
        if (currentColor.getARGB() == result.getPixel().getARGB()) return;

        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();
        int rgbToCMYK[] = RGBtoCMYK(this.red, this.green, this.blue);

        cyanCS.setValue(rgbToCMYK[0]);
        magentaCS.setValue(rgbToCMYK[1]);
        jauneCS.setValue(rgbToCMYK[2]);
        noirCS.setValue(rgbToCMYK[3]);

        computeCyanImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        computeMagentaImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        computeJauneImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        computeNoirImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
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
        if (cs == noirCS && v!= noir){
            noir = v;
            updateCyan = true;
            updateMagenta = true;
            updateJaune = true;
        }

        int cmykInRGB[] = CMYKtoRGB(this.cyan, this.magenta, this.jaune, this.noir);

        if (updateCyan) {
            computeCyanImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        }
        if (updateMagenta) {
            computeMagentaImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        }
        if (updateJaune) {
            computeJauneImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        }

        if (updateNoir) {
            computeNoirImage(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2]);
        }

        Pixel currentColor = new Pixel(cmykInRGB[0], cmykInRGB[1], cmykInRGB[2], this.noir);
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

