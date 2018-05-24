package view;

import model.ObserverIF;
import model.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF {

    private static final int HUE = 0;
    private static final int SATURATION = 1;
    private static final int VALUE = 2;
    ColorSlider hueCS;
    ColorSlider saturationCS;
    ColorSlider valueCS;
    int red;
    int green;
    int blue;
    int hue;
    int saturation;
    int value;
    BufferedImage hueImage;
    BufferedImage saturationImage;
    BufferedImage valueImage;
    int imagesWidth;
    int imagesHeight;
    ColorDialogResult result;

    HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
        this.imagesWidth = imagesWidth;
        this.imagesHeight = imagesHeight;
        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();
        this.result = result;
        result.addObserver(this);


        hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
        saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
        valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);

        int[] hsv = RGBToHSV(red,green,blue);

        hue = hsv[HUE];
        saturation = hsv[SATURATION];
        value = hsv[VALUE];

        computeHueImage(hue, saturation, value);
        computeSaturationImage(hue, saturation, value);
        computeValueImage(hue, saturation, value);
    }

    /**
     * Converts RGB to HSV
     * source:https://cs.stackexchange.com/questions/64549/convert-hsv-to-rgb-colors
     * @param r red pixel
     * @param g green pixel
     * @param b blue pixel
     */
    public int[] RGBToHSV(int r, int g, int b){

        //Tableau pour les couleurs HSV
        float[] hsv = new float[3];
        int[] hsvInt = new int[3];

        Color.RGBtoHSB(r,g,b,hsv);

        hsv[0] = Math.round(hsv[0]*255);
        hsv[1] = Math.round(hsv[1]*255);
        hsv[2] = Math.round(hsv[2]*255);

        hsvInt[0] = (int) hsv[0];
        hsvInt[1] = (int) hsv[1];
        hsvInt[2] = (int) hsv[2];

        return hsvInt;

    }

    /**
     * Converts HSV to RGB
     * Source : https://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     * @param h Hue
     * @param s Saturation
     * @param v Value
     */
    public int[] HSVToRGB(int h, int s, int v){

        int[] rgbArray = new int[3];

        float temp_h = h/360;
        float temp_s = s/100;
        float temp_v = v/100;

        float r =0,g = 0,b =0;


        int _h = (int) (h*6);
        float f = temp_h * 6 - _h;
        float p = temp_v * (1 - temp_s);
        float q = temp_v * (1- f * temp_s);
        float t = temp_v * (1- (1-f) * temp_s );

        if(_h == 0){
            r = temp_v;
            g = t;
            b = p;
        }

        else if(_h == 1){
            r = q;
            g = temp_v;
            b = t;
        }

        else if(_h == 2){
            r = p;
            g = temp_v;
            b = t;
        }

        else if(_h == 3){
            r = p;
            g = q;
            b = temp_v;
        }

        else if(_h == 4){
            r = t;
            g = p;
            b = temp_v;
        }

        else {
            r = temp_v;
            g = p;
            b = q;
        }

        rgbArray[0] = (int) r *255;
        rgbArray[1] = (int) g *255;
        rgbArray[2] = (int) b *255;

        return rgbArray;
    }


    /*
     * @see View.SliderObserver#update(double)
     */
    public void update(ColorSlider s, int v) {
        boolean updateHue = false;
        boolean updateSaturation = false;
        boolean updateValue = false;

        if (s == hueCS && v != hue) {
            hue = v;
            updateSaturation = true;
            updateValue = true;
        }
        if (s == saturationCS && v != saturation) {
            saturation = v;
            updateHue = true;
            updateValue = true;
        }
        if (s == valueCS && v != value) {
            value = v;
            updateHue = true;
            updateSaturation = true;
        }
        if (updateHue) {
            computeHueImage(hue, saturation, value);
        }
        if (updateSaturation) {
            computeSaturationImage(hue, saturation, value);
        }
        if (updateValue) {
            computeValueImage(hue, saturation, value);
        }

        int hsvToRGB[] = HSVToRGB(this.hue, this.value, this.saturation);

        Pixel pixel = new Pixel(hsvToRGB[0], hsvToRGB[1], hsvToRGB[2], 255);
        result.setPixel(pixel);
    }

    /* (non-Javadoc)
     * @see model.ObserverIF#update()
     */
    public void update() {
        int hsvToRGB[] = HSVToRGB(this.hue, this.value, this.saturation);

        Pixel currentColor = new Pixel(hsvToRGB[0], hsvToRGB[1], hsvToRGB[2], 255);
        if(currentColor.getARGB() == result.getPixel().getARGB()) return;

        this.red = result.getPixel().getRed();
        this.green = result.getPixel().getGreen();
        this.blue = result.getPixel().getBlue();

        int[] hsvColor = RGBToHSV(this.red, this.green, this.blue);

        hue = hsvColor[HUE];
        saturation = hsvColor[SATURATION];
        value = hsvColor[VALUE];

        hueCS.setValue(hue);
        saturationCS.setValue(saturation);
        valueCS.setValue(value);

        computeHueImage(hue, saturation, value);
        computeSaturationImage(hue, saturation, value);
        computeValueImage(hue, saturation, value);

        // Efficiency issue: When the color is adjusted on a tab in the
        // user interface, the sliders color of the other tabs are recomputed,
        // even though they are invisible. For an increased efficiency, the
        // other tabs (mediators) should be notified when there is a tab
        // change in the user interface. This solution was not implemented
        // here since it would increase the complexity of the code, making it
        // harder to understand.
    }

    public void computeHueImage(int hue, int saturation, int value) {
        Pixel p = new Pixel(red,green,blue, 255);

        int[] rgbArray;

        for (int i = 0; i<imagesWidth; ++i) {
            hue = ((int)(((double)i / (double)imagesWidth)*255));
            rgbArray = RGBToHSV(hue,saturation,value);
            p.setRed(rgbArray[0]);
            p.setGreen(rgbArray[1]);
            p.setBlue(rgbArray[2]);
            int rgb = p.getARGB();
            for (int j = 0; j<imagesHeight; ++j) {
                hueImage.setRGB(i, j, rgb);
            }
        }
        if (hueCS != null) {
            hueCS.update(hueImage);
        }
    }

    public void computeSaturationImage(int hue, int saturation, int value) {

        Pixel p = new Pixel(red, green, blue, 255);

        int[] rgbArray;

        for (int i = 0; i<imagesWidth; ++i) {
            saturation = ((int)(((double)i / (double)imagesWidth)*255.0));
            rgbArray = RGBToHSV(hue,saturation,value);
            p.setRed(rgbArray[0]);
            p.setGreen(rgbArray[1]);
            p.setBlue(rgbArray[2]);
            int rgb = p.getARGB();
            for (int j = 0; j<imagesHeight; ++j) {
                saturationImage.setRGB(i, j, rgb);
            }
        }
        if (saturationCS != null) {
            saturationCS.update(saturationImage);
        }
    }

    public void computeValueImage(int hue, int saturation, int value) {


        Pixel p = new Pixel(red, green, blue, 255);

        int[] rgbArray;

        for (int i = 0; i<imagesWidth; ++i) {
            value = ((int)(((double)i / (double)imagesWidth)*255.0));
            rgbArray = HSVToRGB(hue,saturation,value);
            p.setRed(rgbArray[0]);
            p.setGreen(rgbArray[1]);
            p.setBlue(rgbArray[2]);
            int rgb = p.getARGB();
            for (int j = 0; j<imagesHeight; ++j) {
                valueImage.setRGB(i, j, rgb);
            }
        }
        if (valueCS != null) {
            valueCS.update(valueImage);
        }
    }

    public BufferedImage getHueImage() {
        return hueImage;
    }

    public BufferedImage getSaturationImage() {
        return saturationImage;
    }


    public BufferedImage getValueImage() {
        return valueImage;
    }

    public void setHueCS(ColorSlider slider) {
        hueCS = slider;
        slider.addObserver(this);
    }

    public void setSaturationCS(ColorSlider slider) {
        saturationCS = slider;
        slider.addObserver(this);
    }

    public void setValueCS(ColorSlider slider) {
        valueCS = slider;
        slider.addObserver(this);
    }

    public int getHue() {
        return hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
