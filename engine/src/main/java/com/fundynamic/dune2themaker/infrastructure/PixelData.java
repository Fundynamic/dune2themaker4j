package com.fundynamic.dune2themaker.infrastructure;

import java.nio.ByteBuffer;

/**
 * PixelData is a utility class for efficiently handling per-pixel operations.
 * It holds an array of color data based on the given width/height (in pixels)
 * and specified color format. The pixels can then be "applied" or uploaded
 * to an OpenGL texture.
 * <p/>
 * See the wiki for details and usage examples.
 *
 * @author davedes
 */
public class PixelData {

	/**
	 * Convenience method to create a texture with a given internal format.
	 * @param width the width of the empty texture
	 * @param height the height of the empty texture
	 * @param filter the filter to use
	 * @param format the internal format
	 * @return a generated texture

	public static Texture createTexture(int width, int height, ImageData.Format format, int filter) throws SlickException {
	EmptyImageData data = new EmptyImageData(width, height);
	ByteBuffer dataBuffer = data.getImageBufferData();
	String ref = "pixelhandler:"+width+"x"+height+":"+format.toString();
	try {
	return InternalTextureLoader.get().createTexture(data, dataBuffer, ref,
	GL11.GL_TEXTURE_2D, filter, filter, false, format);
	} catch (IOException e) {
	throw new SlickException("Error generating texture", e);
	}
	}*/

	/**
	 * The pixels held by this PixelData object.
	 */
	protected ByteBuffer pixels;

	/** The format of the pixel data.
	 protected ImageData.Format format;*/

	/**
	 * The width of the data (in pixels).
	 */
	protected int width;
	/**
	 * The height of the data (in pixels).
	 */
	protected int height;

	/**
	 * Creates a PixelData buffer with the specified size, using RGBA format.
	 *
	 * @param width the width in pixels of our data
	 * @param height the height in pixels of our data

	public PixelData(int width, int height) {
	this(width, height, ImageData.Format.RGBA);
	}
	 */

	/**
	 * Creates a PixelData buffer with the specified size and format.
	 *
	 * Note that Slick currently loads textures with an internal RGBA format;
	 * this means that even if we upload, say, 2-component (e.g. GRAYALPHA)
	 * texture data, it will eventually be stored in OpenGL video memory
	 * using RGBA. For better performance and memory management,
	 * create textures with the same internal format as the format given to PixelData.
	 * The static 'createTexture' utility method is intended for this purpose.
	 *
	 * @param width the width in pixels of our data
	 * @param height the height in pixels of our data
	 * @param format the desired format to use during uploading

	public PixelData(int width, int height, ImageData.Format format) {
	this.format = format;
	this.width = width;
	this.height = height;
	this.pixels = BufferUtils.createByteBuffer(width * height * format.getColorComponents());
	}
	 */

	/**
	 * Sets the pixel data to the given array, which should be less
	 * than the size of length().
	 *
	 * @param pixelData the new pixel data
	 * @return this object, for chaining
	 */
	public PixelData set(byte[] pixelData) {
		pixels.clear();
		pixels.put(pixelData);
		pixels.flip();
		return this;
	}

	/**
	 * Clears the pixel array to 0x00 (i.e. transparent black for RGBA).
	 *
	 * @return this object, for chaining
	 */
	public PixelData clear() {
		return clear(0x00);
	}

	/**
	 * Clears the pixel array to the specified single-component
	 * color, i.e. to clear to white you would use:
	 * <pre>    clear(0xFF)</pre>
	 *
	 * @param value the byte value to fill the array with
	 * @return this object, for chaining
	 */
	public PixelData clear(int value) {
		pixels.clear();
		byte b = (byte) value;
		for (int i = 0; i < pixels.capacity(); i++)
			pixels.put(b);
		pixels.flip();
		return this;
	}

	/**
	 * Uploads the pixel data to the given texture at the top-left origin (0, 0).
	 * This only needs to be called once, after the pixel data has changed.
	 *
	 * @param texture the texture to modify

	public void apply(Texture texture) {
	apply(texture, 0, 0);
	}
	 */

	/**
	 * Uploads the pixel data to the given texture at the specified position.
	 * This only needs to be called once, after the pixel data has changed.
	 *
	 * @param texture the texture to modify
	 * @param x the x position to place the pixel data on the texture
	 * @param y the y position to place the pixel data on the texture

	public void apply(Texture texture, int x, int y) {
	if (x+width > texture.getTextureWidth() || y+height > texture.getTextureHeight())
	throw new IndexOutOfBoundsException("pixel data won't fit in given texture");
	position(length());
	pixels.flip();
	int glFmt = format.getOGLType();
	final SGL GL = Renderer.get();
	texture.bind();
	GL.glTexSubImage2D(SGL.GL_TEXTURE_2D, 0, x, y, width, height,
	glFmt, SGL.GL_UNSIGNED_BYTE, pixels);
	}
	 */

	/**
	 * Returns a color representation of the given pixel.
	 *
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @return a Color representing this pixel
	 *         <p/>
	 *         public Color getPixel(int x, int y) {
	 *         position(x, y);
	 *         return getPixel();
	 *         }
	 */

	private int translate(byte b) {
		return b < 0 ? 256 + b : b;
	}

	/**
	 * Relative <i>get</i> method which handles color components based on image formats.
	 * Does not offset the buffer.
	 * <p/>
	 * Color is converted like so based on the Format's color components:
	 * - If getColorComponents returns 1 and hasAlpha is true: Color(0xFF, 0xFF, 0xFF, A)
	 * - If getColorComponents returns 1 and hasAlpha is false: Color(L, L, L, 0xFF)
	 * - If getColorComponents returns 2: Color(L, L, L, A)
	 * - If getColorComponents returns 3: Color(R, G, B, 0xFF)
	 * - If getColorComponents returns 4: Color(R, G, B, A)
	 * <p/>
	 * See setPixel for details.
	 *
	 * @return a Color representation for this pixel
	 *         <p/>
	 *         public Color getPixel() {
	 *         boolean hasAlpha = getFormat().hasAlpha();
	 *         int c1 = translate(pixels.get());
	 *         int bpp = format.getColorComponents();
	 *         if (bpp==1)
	 *         return hasAlpha ? new Color(0xFF, 0xFF, 0xFF, c1) : new Color(c1, c1, c1, 0xFF);
	 *         int c2 = translate(pixels.get());
	 *         if (bpp==2)
	 *         return new Color(c1, c1, c1, c2);
	 *         int c3 = translate(pixels.get());
	 *         if (bpp==3)
	 *         return new Color(c1, c2, c3, 0xFF);
	 *         int c4 = translate(pixels.get());
	 *         return new Color(c1, c2, c3, c4);
	 *         }
	 */

	byte avg(int r, int g, int b) {
		return (byte) ((translate((byte) r) + translate((byte) g) + translate((byte) b)) / 3);
	}

	/**
	 * Relative <i>put</i> method which handles color components based on image formats.
	 * Does not offset the buffer.
	 *
	 * See setPixel for details.
	 * @param c the color to put
	 * @return this object, for chaining

	public PixelData putPixel(int r, int g, int b, int a) {
	boolean hasAlpha = getFormat().hasAlpha();
	int bpp = format.getColorComponents();
	if (bpp==1) {
	pixels.put((byte)(hasAlpha ? a : avg(r, g, b)));
	} else if (bpp==2) {
	pixels.put(avg(r, g, b)).put((byte)a);
	} else {
	pixels.put((byte)r).put((byte)g).put((byte)b);
	if (bpp>=4)
	pixels.put((byte)a);
	}
	return this;
	}

	 */

	/**
	 * Puts a single byte in the buffer; i.e. a luminance value for GRAY format.
	 *
	 * @param b the byte to place
	 * @return this object, for chaining
	 */
	public PixelData put(byte b) {
		pixels.put(b);
		return this;
	}

	/**
	 * Sets the RGBA pixel at the given (x, y) location.
	 *
	 * The Format determines what the resulting color will be:
	 * - If getColorComponents returns 1 and hasAlpha is true, 'a' will be used
	 * - If getColorComponents returns 1 and hasAlpha is false, 'rgb' will be averaged for luminance
	 * - If getColorComponents returns 2, 'rgb' will be averaged for luminance and and 'a' will be used for alpha
	 * - If getColorComponents returns 3 'r', 'g', 'b' will be used
	 * - If getColorComponents returns 4 'r', 'g', 'b' and 'a' will be used
	 *
	 * In this fashion the following formats will produce expected results
	 * when using the values from Slick's Color class: RGBA, RGB, GRAYALPHA,
	 * GRAY, ALPHA.
	 *
	 * @param x the x position to place the pixel
	 * @param y the y position to place the pixel
	 * @param r the red, luminance or alpha component
	 * @param g the green component
	 * @param b the blue component
	 * @param a the alpha component

	public void setPixel(int x, int y, int r, int g, int b, int a) {
	position(x, y);
	putPixel(r, g, b, a);
	}
	 */

	/**
	 * Calls setPixel with an alpha value of 0xFF.
	 *
	 * @param x the x position to place the pixel
	 * @param y the y position to place the pixel
	 * @param r the red component
	 * @param g the green component
	 * @param b the blue component

	public void setPixel(int x, int y, int r, int g, int b) {
	setPixel(x, y, r, g, b, 0xFF);
	}
	 */

	/**
	 * A convenience method to set a given pixel's color using Slick's color class.
	 * See the other setPixel method for details.
	 *
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @param rgba the RGBA components of the pixel
	 * @see setPixel(int x, int y, int r, int g, int b, int a)

	public void setPixel(int x, int y, Color rgba) {
	setPixel(x, y, rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
	}
	 */

	/**
	 * A convenience method to <i>put</i> a given pixel's color using Slick's color class.
	 * See the other putPixel method for details.
	 *
	 * @param rgba the RGBA components of the pixel

	public PixelData putPixel(Color rgba) {
	return putPixel(rgba.getRed(), rgba.getGreen(), rgba.getBlue(), rgba.getAlpha());
	}
	 */

	/**
	 * Copies this pixel data. Modifying the new data will not affect the original.
	 * @return a copy of this pixel data

	public PixelData copy() {
	PixelData d = new PixelData(getWidth(), getHeight(), getFormat());
	int oldPos = pixels.position();
	d.position(0);
	d.pixels.put(pixels);
	d.pixels.flip();
	pixels.position(oldPos);
	return d;
	}
	 */

	/**
	 * The total capacity of this pixel buffer in bytes.
	 *
	 * @return total bytes contained by the backing array
	 */
	public int length() {
		return pixels.capacity();
	}

	/**
	 * Returns the width in pixels.
	 *
	 * @return the width of the pixel data region
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height in pixels.
	 *
	 * @param the height of the pixel data region
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the format defined by this pixel data.
	 * @return the image format

	public ImageData.Format getFormat() {
	return format;
	}
	 */

	/**
	 * Returns the backing byte buffer.
	 *
	 * @return the byte buffer
	 */
	public ByteBuffer buffer() {
		return pixels;
	}

	/**
	 * Sets the position of this buffer, used alongside relative getPixel and putPixel methods.
	 *
	 * @param pos the position; the index for the buffer
	 */
	public void position(int pos) {
		pixels.position(pos);
	}

	/**
	 * Sets the position of the buffer to the offset given by the (x, y) coordinates, in pixels.
	 * @param x the x pixel to offset to
	 * @param y the y pixel to offset to

	public void position(int x, int y) {
	if ((x < 0) || (x >= width) || (y < 0) || (y >= height))
	throw new IndexOutOfBoundsException("Specified location: "+x+","+y+" outside of region");
	int bpp = format.getColorComponents();
	int offset = ((x + (y * width)) * bpp);
	position(offset);
	}
	 */
}