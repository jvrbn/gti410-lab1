/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package controller;
import model.ImageX;
import model.Pixel;
import model.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;
import java.util.Stack;

/**
 * <p>Title: ImageLineFiller</p>
 * <p>Description: Image transformer that inverts the row color</p>
 * <p>Copyright: Copyright (c) 2003 Colin Barré-Brisebois, Éric Paquette</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author unascribed
 * @version $Revision: 1.12 $
 */
public class ImageFiller extends AbstractTransformer {
    private ImageX currentImage;
    private int currentImageWidth;
    private Pixel fillColor = new Pixel(0xFF00FFFF);
    private Pixel borderColor = new Pixel(0xFFFFFF00);
    private boolean floodFill = true;
    private int hueThreshold = 1;
    private int saturationThreshold = 2;
    private int valueThreshold = 3;

    /**
     * Creates an ImageLineFiller with default parameters.
     * Default pixel change color is black.
     */
    public ImageFiller() {
    }

    /* (non-Javadoc)
     * @see controller.AbstractTransformer#getID()
     */
    public int getID() { return ID_FLOODER; }

    protected boolean mouseClicked(MouseEvent e){
        List intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
        if (!intersectedObjects.isEmpty()) {
            Shape shape = (Shape)intersectedObjects.get(0);
            if (shape instanceof ImageX) {
                currentImage = (ImageX)shape;
                currentImageWidth = currentImage.getImageWidth();

                Point pt = e.getPoint();
                Point ptTransformed = new Point();
                try {
                    shape.inverseTransformPoint(pt, ptTransformed);
                } catch (NoninvertibleTransformException e1) {
                    e1.printStackTrace();
                    return false;
                }
                ptTransformed.translate(-currentImage.getPosition().x, -currentImage.getPosition().y);
                if (0 <= ptTransformed.x && ptTransformed.x < currentImage.getImageWidth() &&
                        0 <= ptTransformed.y && ptTransformed.y < currentImage.getImageHeight()) {
                    currentImage.beginPixelUpdate();

                    if(isFloodFill()){
                        floodFill(ptTransformed, fillColor);
                    }
                    else{
                        borderFill(ptTransformed, fillColor);
                    }
                    currentImage.endPixelUpdate();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Horizontal line fill with specified color
     *
    private void horizontalLineFill(Point ptClicked) {
        Stack stack = new Stack();
        stack.push(ptClicked);
        while (!stack.empty()) {
            Point current = (Point)stack.pop();
            if (0 <= current.x && current.x < currentImage.getImageWidth() &&
                    !currentImage.getPixel(current.x, current.y).equals(fillColor)) {
                currentImage.setPixel(current.x, current.y, fillColor);

                // Next points to fill.
                Point nextLeft = new Point(current.x-1, current.y);
                Point nextRight = new Point(current.x+1, current.y);
                stack.push(nextLeft);
                stack.push(nextRight);
            }
        }

        // TODO EP In this method, we are creating many new Point instances.
        //      We could try to reuse as many as possible to be more efficient.
        // TODO EP In this method, we could be creating many Point instances.
        //      At some point we can run out of memory. We could create a new point
        //      class that uses shorts to cut the memory use.
        // TODO EP In this method, we could test if a pixel needs to be filled before
        //      adding it to the stack (to reduce memory needs and increase efficiency).
    }
     */

    private void floodFill(Point ptClicked, Pixel color){

        Stack stack = new Stack();
        stack.push(ptClicked);
        while (!stack.empty()){
            Point current = (Point)stack.pop();
            if(0 <= current.x && current.x < currentImage.getImageWidth() && 0 <= current.y && current.y < currentImage.getImageHeight()  &&
                    !currentImage.getPixel(current.x, current.y).equals(fillColor)){

                currentImage.setPixel(current.x, current.y, fillColor);

                Point nextLeft = new Point(current.x-1, current.y);
                Point nextRight = new Point(current.x+1, current.y);
                Point nextTop = new Point(current.x, current.y+1);
                Point nextBottom = new Point(current.x, current.y-1);
                stack.push(nextLeft);
                stack.push(nextRight);
                stack.push(nextTop);
                stack.push(nextBottom);
                setFloodFill(true);
            }
        }
    }
    private void borderFill(Point ptClicked, Pixel color){

        Stack stack = new Stack();
        stack.push(ptClicked);
        while (!stack.empty()){
            Point current = (Point)stack.pop();
            if(0 <= current.x && current.x < currentImage.getImageWidth() && 0 <= current.y && current.y < currentImage.getImageHeight()  &&
                    !currentImage.getPixel(current.x, current.y).equals(borderColor) && thresholdColor(current, color)){

                currentImage.setPixel(current.x, current.y, fillColor);

                Point nextLeft = new Point(current.x-1, current.y);
                Point nextRight = new Point(current.x+1, current.y);
                Point nextTop = new Point(current.x, current.y+1);
                Point nextBottom = new Point(current.x, current.y-1);
                stack.push(nextLeft);
                stack.push(nextRight);
                stack.push(nextTop);
                stack.push(nextBottom);

            }
        }
    }

    public boolean thresholdColor(Point currentPt, Pixel borderColor){

        Pixel pixelConsiderer = this.currentImage.getPixel(currentPt.x, currentPt.y);

        float [] hsv = new float[3];
        float [] hsvBorder = new float[3];

        Color.RGBtoHSB(pixelConsiderer.getRed(), pixelConsiderer.getGreen(), pixelConsiderer.getBlue(),hsv);
        Color.RGBtoHSB(pixelConsiderer.getRed(), pixelConsiderer.getGreen(), pixelConsiderer.getBlue(),hsv);
        Color.RGBtoHSB(pixelConsiderer.getRed(), pixelConsiderer.getGreen(), pixelConsiderer.getBlue(),hsv);

        Color.RGBtoHSB(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(),hsvBorder);
        Color.RGBtoHSB(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(),hsvBorder);
        Color.RGBtoHSB(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(),hsvBorder);


        if((hsvBorder[0]*255 - getHueThreshold()) <= hsv[0]*255 && hsv[0]*255 <= (hsvBorder[0]*255 + getHueThreshold())&&
                (hsvBorder[1]*255 - getSaturationThreshold()) <= hsv[1]*255 && hsv[1]*255 <= (hsvBorder[1]*255 + getSaturationThreshold())&&
                (hsvBorder[2]*255 - getValueThreshold()) <= hsv[2]*255 && hsv[2]*255 <= (hsvBorder[2]*255 + getValueThreshold())){

            return false;
        }
        return true;

    }

    /**
     * @return
     */
    public Pixel getBorderColor() {
        return borderColor;
    }

    /**
     * @return
     */
    public Pixel getFillColor() {
        return fillColor;
    }

    /**
     * @param pixel
     */
    public void setBorderColor(Pixel pixel) {
        borderColor = pixel;
        System.out.println("new border color");
    }

    /**
     * @param pixel
     */
    public void setFillColor(Pixel pixel) {
        fillColor = pixel;
        System.out.println("new fill color");
    }
    /**
     * @return true if the filling algorithm is set to Flood Fill, false if it is set to Boundary Fill.
     */
    public boolean isFloodFill() {
        return floodFill;
    }

    /**
     * @param b set to true to enable Flood Fill and to false to enable Boundary Fill.
     */
    public void setFloodFill(boolean b) {
        floodFill = b;
        if (floodFill) {
            System.out.println("now doing Flood Fill");
        } else {
            System.out.println("now doing Boundary Fill");
        }
    }

    /**
     * @return
     */
    public int getHueThreshold() {
        return hueThreshold;
    }

    /**
     * @return
     */
    public int getSaturationThreshold() {
        return saturationThreshold;
    }

    /**
     * @return
     */
    public int getValueThreshold() {
        return valueThreshold;
    }

    /**
     * @param i
     */
    public void setHueThreshold(int i) {
        hueThreshold = i;
        System.out.println("new Hue Threshold " + i);
    }

    /**
     * @param i
     */
    public void setSaturationThreshold(int i) {
        saturationThreshold = i;
        System.out.println("new Saturation Threshold " + i);
    }

    /**
     * @param i
     */
    public void setValueThreshold(int i) {
        valueThreshold = i;
        System.out.println("new Value Threshold " + i);
    }

}
