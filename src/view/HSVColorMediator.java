package view;

import model.ObserverIF;
import model.Pixel;

import java.awt.*;
import java.util.Observer;
import java.awt.image.BufferedImage;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF {

    ColorSlider hueCS;
    ColorSlider saturationCS;
    ColorSlider valueCS;
    float hsv[];
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

        computeHueImage(red, green, blue);
        computeSaturationImage(red, green, blue);
        computeValueImage(red, green, blue);
    }

    public void RGBToHSV(int r, int g, int b){

        hsv = new float[3];
        Color.RGBtoHSB(r,g,b,hsv);
        hue = (int)hsv[0];
        saturation = (int)hsv[1];
        value = (int)hsv[2];

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
            computeHueImage(red, green, blue);
        }
        if (updateSaturation) {
            computeSaturationImage(red, green, blue);
        }
        if (updateValue) {
            computeValueImage(red, green, blue);
        }

        Pixel pixel = new Pixel(red, green, blue, 255);
        result.setPixel(pixel);
    }

    /* (non-Javadoc)
     * @see model.ObserverIF#update()
     */
    public void update() {

        // When updated with the new "result" color, if the "currentColor"
        // is aready properly set, there is no need to recompute the images.
        Pixel currentColor = new Pixel(red, green, blue, 255);
        if(currentColor.getARGB() == result.getPixel().getARGB()) return;

        red = result.getPixel().getRed();
        green = result.getPixel().getGreen();
        blue = result.getPixel().getBlue();


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

    public void computeHueImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue, 255);
        RGBToHSV(red,green,blue);
        for (int i = 0; i<imagesWidth; ++i) {
            p.setRed((int)(((double)i / (double)imagesWidth)*360.0));
            int rgb = p.getARGB();
            for (int j = 0; j<imagesHeight; ++j) {
                hueImage.setRGB(i, j, rgb);
            }
        }
        if (hueCS != null) {
            hueCS.update(hueImage);
        }
    }

    public void computeSaturationImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i<imagesWidth; ++i) {
            p.setGreen((int)(((double)i / (double)imagesWidth)*255.0));
            int rgb = p.getARGB();
            for (int j = 0; j<imagesHeight; ++j) {
                saturationImage.setRGB(i, j, rgb);
            }
        }
        if (saturationCS != null) {
            saturationCS.update(saturationImage);
        }
    }

    public void computeValueImage(int red, int green, int blue) {
        Pixel p = new Pixel(red, green, blue, 255);
        for (int i = 0; i<imagesWidth; ++i) {
            p.setBlue((int)(((double)i / (double)imagesWidth)*255.0));
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
