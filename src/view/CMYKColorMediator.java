package view;

import com.sun.org.apache.regexp.internal.RE;
import model.ObserverIF;
import model.Pixel;

import java.awt.image.BufferedImage;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {

    private final int RED=0;
    private final int GREEN=1;
    private final int BLUE=2;
    private final int CYAN=0;
    private final int MAGENTA=1;
    private final int JAUNE=2;
    private final int NOIR=3;
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

        int rgbInCMYK[] = RGBtoCMYK(this.red, this.green, this.blue);
        this.cyan = rgbInCMYK[CYAN];
        this.magenta = rgbInCMYK[MAGENTA];
        this.jaune = rgbInCMYK[JAUNE];
        this.noir = rgbInCMYK[NOIR];
        int cmykInRGB[] = CMYKtoRGB(this.cyan, this.magenta, this.jaune, this.noir);

        computeCyanImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        computeMagentaImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        computeJauneImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        computeNoirImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
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
        rgbToCMYK[CYAN] = this.cyan;
        rgbToCMYK[MAGENTA] = this.magenta;
        rgbToCMYK[JAUNE] = this.jaune;
        rgbToCMYK[NOIR] = Math.max(Math.max(1 - red/255, 1 - green/255),1 - blue/255);

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

        cmykToRGB[RED] = Math.round(((255 - cyan)*(255-noir))/255); //red
        cmykToRGB[GREEN] = Math.round(((255 - magenta)*(255-noir))/255); //green
        cmykToRGB[BLUE] = Math.round(((255 - jaune)*(255-noir))/255); //blue

        return cmykToRGB;
    }

    /**
     * Calcul de la couleur cyan
     * @param red
     * @param green
     * @param blue
     */
    public void computeCyanImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue, 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setRed((int) (255 - this.noir - ((double) i / (double) imageWidth * (255 - this.noir))));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                cyanImage.setRGB(i, j, rgb);
            }
        }
        if (cyanCS != null) {
            cyanCS.update(cyanImage);
        }
    }


    public void computeMagentaImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue, 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setGreen((int) (255 - this.noir - ((double) i / (double) imageWidth * (255 - this.noir))));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                magentaImage.setRGB(i, j, rgb);
            }
        }
        if (magentaCS != null) {
            magentaCS.update(magentaImage);
        }
    }

    public void computeJauneImage(int red, int green, int blue){
        Pixel p = new Pixel(red, green, blue, 255);

        for (int i = 0; i < imageWidth; i++) {
            p.setBlue((int)(255 - this.noir - ((double) i / (double) imageWidth * (255 - this.noir))));
            int rgb = p.getARGB();
            for (int j = 0; j < imageHeigth; j++) {
                jauneImage.setRGB(i, j, rgb);
            }
        }
        if (jauneCS != null) {
            jauneCS.update(jauneImage);
        }
    }

    public void computeNoirImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue);

        for (int i = 0; i < imageWidth; i++) {
            p.setRed((int) (255 - this.noir - ((double) i / (double) imageWidth * (255 - this.noir))));
            p.setGreen((int) (255 - this.noir - ((double) i / (double) imageWidth * (255 - this.noir))));
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

        Pixel currentColor = new Pixel(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE],255);
        if (currentColor.getARGB() == result.getPixel().getARGB()) return;

        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();
        int rgbInCMYK[] = RGBtoCMYK(this.red, this.green, this.blue);

        cyanCS.setValue(rgbInCMYK[CYAN]);
        magentaCS.setValue(rgbInCMYK[MAGENTA]);
        jauneCS.setValue(rgbInCMYK[JAUNE]);
        noirCS.setValue(rgbInCMYK[NOIR]);

        computeCyanImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        computeMagentaImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        computeJauneImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        computeNoirImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
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
            computeCyanImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        }
        if (updateMagenta) {
            computeMagentaImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        }
        if (updateJaune) {
            computeJauneImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        }
        if (updateNoir) {
            computeNoirImage(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE]);
        }

        Pixel currentColor = new Pixel(cmykInRGB[RED], cmykInRGB[GREEN], cmykInRGB[BLUE], 255);
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
        cyanCS = slider;
        slider.addObserver(this);
    }

    public void setMagentaCS(ColorSlider slider) {
        magentaCS = slider;
        slider.addObserver(this);
    }

    public void setJauneCS(ColorSlider slider) {
        jauneCS = slider;
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

