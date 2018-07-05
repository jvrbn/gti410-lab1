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

import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

/**
 * <p>Title: PaddingZeroStrategy</p>
 * <p>Description: Padding strategy where Zero values are returned if Pixel values are out of range.</p>
 * <p>Copyright: Copyright (c) 2003 Colin Barr�-Brisebois, �ric Paquette</p>
 * <p>Company: ETS - �cole de Technologie Sup�rieure</p>
 * @author unascribed
 * @version $Revision: 1.4 $
 */
public class PaddingMirrorStrategy extends PaddingStrategy {
	Pixel zeroPixel = new Pixel(0,0,0,0);
	PixelDouble zeroPixelDouble = new PixelDouble(0,0,0,0);
	
	/**
	 * Returns and validates the Pixel at the specified coordinate.
	 * If the Pixel is invalid, a new black (0,0,0,0) Pixel is returned.
	 * @param image source Image
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the validated Pixel value at the specified coordinates
	 */
	public Pixel pixelAt(ImageX image, int x, int y) {

        Pixel pixel = null;

            //coin
            if(x <= 0 && y <= 0){

                return image.getPixel(0,0);
            }

            else if(x < 0 && (y >= 0 && y<=image.getImageHeight()-1)){

                return image.getPixel(0,y);
            }


            //coin
            else if(x <= 0 && y >= image.getImageHeight()-1){

                pixel = image.getPixel(0,image.getImageHeight()-1);
            }


            else if(y < 0 && (x >= 0 && x <= image.getImageWidth()-1)){

                pixel = image.getPixel(x,0);
            }

            //coin
            else if(x >= image.getImageWidth()-1 && y >= image.getImageHeight()-1){

                pixel = image.getPixel(image.getImageWidth()-1,image.getImageHeight()-1);

            }


            //coin
            else if(x >= image.getImageWidth()-1 && y <= 0 ){
                pixel = image.getPixel(image.getImageWidth()-1,0);

            }

            else if((x >= 0 && x <= image.getImageWidth()-1) && y > image.getImageHeight()-1){
                pixel = image.getPixel(x,image.getImageHeight()-1);

            }


            else if((y >= 0 && y <= image.getImageHeight()) && x > image.getImageWidth()){
                pixel = image.getPixel(image.getImageWidth()-1,y);

            }
            else{
                pixel = image.getPixel(x,y);
            }

        return pixel;
    }

	/**
	 * Returns and validates the PixelDouble at the specified coordinate.
	 * Original Pixel is converted to PixelDouble.
	 * If the Pixel is invalid, a new black (0,0,0,0) PixelDouble is returned.
	 * @param image source ImageDouble
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the validated PixelDouble value at the specified coordinates
	 */	
	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		PixelDouble pixel = null;


            //coin
            if(x <= 0 && y <= 0){

                return image.getPixel(0,0);
            }

            else if(x < 0 && (y >= 0 && y<=image.getImageHeight()-1)){

                return image.getPixel(0,y);
            }


            //coin
            else if(x <= 0 && y >= image.getImageHeight()-1){

                pixel = image.getPixel(0,image.getImageHeight()-1);
            }


            else if(y < 0 && (x >= 0 && x <= image.getImageWidth()-1)){

                pixel = image.getPixel(x,0);
            }

            //coin
            else if(x >= image.getImageWidth()-1 && y >= image.getImageHeight()-1){

                pixel = image.getPixel(image.getImageWidth()-1,image.getImageHeight()-1);

            }


            //coin
            else if(x >= image.getImageWidth()-1 && y <= 0 ){
                pixel = image.getPixel(image.getImageWidth()-1,0);

            }

            else if((x >= 0 && x <= image.getImageWidth()-1) && y > image.getImageHeight()-1){
                pixel = image.getPixel(x,image.getImageHeight()-1);

            }


            else if((y >= 0 && y <= image.getImageHeight()-1) && x > image.getImageWidth()-1){
                pixel = image.getPixel(image.getImageWidth()-1,y);

            }
            else{
                pixel = image.getPixel(x,y);
            }

        return pixel;
    }
}
