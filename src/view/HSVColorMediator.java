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
    float hue;
    float saturation;
    float value;
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

        float[] hsv = RGBToHSV(red,green,blue);

        hue = (int) hsv[HUE];
        saturation = (int) hsv[SATURATION];
        value = (int) hsv[VALUE];

        int[] rgb = HSVToRGB(hue, saturation, value);

        System.out.println("hue: " + hsv[0]);
        System.out.println("saturation: " + hsv[1]);
        System.out.println("value: " + hsv[2]);

        System.out.println("r: " + rgb[0]);
        System.out.println("g: " + rgb[1]);
        System.out.println("b: " + rgb[2]);

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
    public float[] RGBToHSV(int r, int g, int b){

        /*
        float[] hsv = new float[3];

        float _red = r/255;
        float _green = g/255;
        float _blue = b/255;

        float max = Math.max(Math.max(_red,_green),_blue);
        float min = Math.min(Math.min(_red,_green),_blue);

        //value
        hsv[2] = max*255;

        //saturation
        hsv[1] = ((max-min)/max)*255;

        //hue
        if(_red == max){
            hsv[0] = ((_green - _red)/(max-min))/360*255;
        }

        else if(_green == max){
            hsv[0] = (2 + (_blue - _red)/(max-min))/360*255;
        }

        else{
            hsv[0] = (4 + (_red - _green)/(max-min))/360*255;
        }

        hsv[0] = 60 * hsv[0];

        if(hsv[0]<0){
            hsv[0] = (hsv[0]+360)/360*255;
        }

       hsv[0] = Math.round(hsv[0]);
*/

        float[] hsv = new float[3];
        Color.RGBtoHSB(r,g,b,hsv);
        hsv[0] = Math.round(hsv[0]*255);
        hsv[1] = Math.round(hsv[1]*255);
        hsv[2] = Math.round(hsv[2]*255);

        return hsv;

    }

    /**
     * Converts HSV to RGB
     * Source : https://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     * @param h Hue
     * @param s Saturation
     * @param v Value
     */
    public int[] HSVToRGB(float h, float s, float v){

        int[] rgbArray = new int[3];

        h = h/360;
        s = s/100;
        v = v/100;

        float r = 0,g = 0,b = 0;

        int _h = (int) (h*6);
        float f = h * 6 - _h;
        float p = v * (1 - s);
        float q = v * (1- f * s);
        float t = v * (1- (1-f) * s );

        if(_h == 0){
            r = v;
            g = t;
            b = p;
        }

        else if(_h == 1){
            r = q;
            g = v;
            b = t;
        }

        else if(_h == 2){
            r = p;
            g = v;
            b = t;
        }

        else if(_h == 3){
            r = p;
            g = q;
            b = v;
        }

        else if(_h == 4){
            r = t;
            g = p;
            b = v;
        }

        else if(_h == 5){
            r = v;
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

        Pixel pixel = new Pixel(red, green, blue, 255);
        result.setPixel(pixel);

        float[] hsv = RGBToHSV(red,green,blue);
        int[] rgb = HSVToRGB(hue, saturation, value);
        System.out.println("hue: " + hsv[0]);
        System.out.println("saturation: " + hsv[1]);
        System.out.println("value: " + hsv[2]);

        System.out.println("r: " + rgb[0]);
        System.out.println("g: " + rgb[1]);
        System.out.println("b: " + rgb[2]);
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

        float[] hsvColor = RGBToHSV(red, green, blue);

        int _hue = (int) hsvColor[HUE];
        int _saturation = (int) hsvColor[SATURATION];
        int _value = (int) hsvColor[VALUE];

        hueCS.setValue(_hue);
        saturationCS.setValue(_saturation);
        valueCS.setValue(_value);

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

    public void computeHueImage(float hue, float saturation, float value) {

        Pixel p = new Pixel(red, green, blue, 255);

        float[] hsv = RGBToHSV(red,green,blue);

        int [] rgbArray;

        for (int i = 0; i<imagesWidth; ++i) {
            hue = ((int)(((double)i / (double)imagesWidth)*255.0));
            rgbArray = HSVToRGB(hue,saturation,value);
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

    public void computeSaturationImage(float hue, float saturation, float value) {
        Pixel p = new Pixel(red, green, blue, 255);
        RGBToHSV(red,green,blue);

        int[] rgbArray;
        for (int i = 0; i<imagesWidth; ++i) {
            saturation = ((int)(((double)i / (double)imagesWidth)*255.0));
            rgbArray = HSVToRGB(hue,saturation,value);
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

    public void computeValueImage(float hue, float saturation, float value) {
        Pixel p = new Pixel(red, green, blue, 255);
        RGBToHSV(red,green,blue);
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

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
