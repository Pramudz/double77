package invsys.controllers.billingwindow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


// this is not tested and finalized just copied and pasted from third party faced lot
// of difficulties in printing images hence ignored for the time being
public class POSImagePrinter {

	private final static char ESC_CHAR = 0x1B;
	private final static char GS = 0x1D;
	private final static byte[] LINE_FEED = new byte[]{0x0A};
	private final static byte[] CUT_PAPER = new byte[]{GS, 0x56, 0x00};
	private final static byte[] INIT_PRINTER = new byte[]{ESC_CHAR, 0x40};
	private static byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33};
	private final static byte[] SET_LINE_SPACE_24 = new byte[]{ESC_CHAR, 0x33, 24};
	
	// The performance of this method 
	// is rather poor, place for improvement
	public static int[][] getPixelsSlow(BufferedImage image) {
	    int width = image.getWidth();
	    int height = image.getHeight();
	    int[][] result = new int[height][width];
	    for (int row = 0; row < height; row++) {
	        for (int col = 0; col < width; col++) {
	            result[row][col] = image.getRGB(col, row);
	        }
	    }

	    return result;
	}
	
	public static byte[] printImage(int[][] pixels) {
		ByteArrayOutputStream printPort = new ByteArrayOutputStream();
		 // Set the line spacing at 24 (we'll print 24 dots high)
		 printPort.writeBytes(SET_LINE_SPACE_24);
		 for (int y = 0; y < pixels.length; y += 24) {
		  // Like I said before, when done sending data, 
		  // the printer will resume to normal text printing
		  printPort.writeBytes(SELECT_BIT_IMAGE_MODE);
		  // Set nL and nH based on the width of the image
		  printPort.writeBytes(new byte[]{(byte)(0x00ff & pixels[y].length)
		                             , (byte)((0xff00 & pixels[y].length) >> 8)});
		  for (int x = 0; x < pixels[y].length; x++) {
		   // for each stripe, recollect 3 bytes (3 bytes = 24 bits)
		   printPort.writeBytes(recollectSlice(y, x, pixels));
		  }

		  // Do a line feed, if not the printing will resume on the same line
		  printPort.writeBytes(LINE_FEED);
		 }
		 printPort.writeBytes(SET_LINE_SPACE_24);
		 
		 return printPort.toByteArray();
		}

		private static byte[] recollectSlice(int y, int x, int[][] img) {
		    byte[] slices = new byte[] {0, 0, 0};
		    for (int yy = y, i = 0; yy < y + 24 && i < 3; yy += 8, i++) {
		        byte slice = 0;
		 for (int b = 0; b < 8; b++) {
		            int yyy = yy + b;
		     if (yyy >= img.length) {
		         continue;
		     }
		     int col = img[yyy][x]; 
		     boolean v = shouldPrintColor(col);
		     slice |= (byte) ((v ? 1 : 0) << (7 - b));
		 }
		        slices[i] = slice;
		    }
		 
		    return slices;
		}

		private static boolean shouldPrintColor(int col) {
		    final int threshold = 127;
		    int a, r, g, b, luminance;
		    a = (col >> 24) & 0xff;
		    if (a != 0xff) {// Ignore transparencies
		        return false;
		    }
		    r = (col >> 16) & 0xff;
		    g = (col >> 8) & 0xff;
		    b = col & 0xff;

		    luminance = (int) (0.299 * r + 0.587 * g + 0.114 * b);

		    return luminance < threshold;
		}
	
}
