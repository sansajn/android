package com.base.engine.rendering;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import com.base.engine.rendering.resourceManagement.TextureResource;
import com.example.opengltest.app.Android;

import java.util.HashMap;

import static android.opengl.GLES20.*;

public class Texture {
	public Texture(String fileName) {
		this.fileName = fileName;
		TextureResource oldResource = loadedTextures.get(fileName);
		if (oldResource != null) {
			resource = oldResource;
			resource.addReference();
		}
		else {
			resource = loadTexture(fileName);
			loadedTextures.put(fileName, resource);
		}
	}

	@Override protected void finalize() {
		if (resource.removeReference() && !fileName.isEmpty())
			loadedTextures.remove(fileName);
	}

	public void bind() {glBindTexture(GL_TEXTURE_2D, getID());}
	public int getID() {return resource.getId();}

	private static TextureResource loadTexture(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length-1];

		try {
			Bitmap image = BitmapFactory.decodeStream(Android.getAssets().open("textures/" + fileName));
//			int[] pixels = new int[image.getWidth() * image.getHeight()];
//			image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

//			ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
//			boolean hasAlpha = image.hasAlpha();

//			for (int y = 0; y < image.getHeight(); ++y) {
//				for (int x = 0; x < image.getWidth(); ++x) {
//					int pixel = pixels[y * image.getWidth() + x];
//					buffer.put((byte)((pixel >> 16) & 0xff));
//					buffer.put((byte)((pixel >> 8) & 0xff));
//					buffer.put((byte)((pixel) & 0xff));
//					if (hasAlpha)
//						buffer.put((byte)((pixel >> 24) & 0xff));
//					else
//						buffer.put((byte)(0xff));
//				}
//			}

//			buffer.flip();

			TextureResource textureResource = new TextureResource();
			glBindTexture(GL_TEXTURE_2D, textureResource.getId());

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			GLUtils.texImage2D(GL_TEXTURE_2D, 0, image, 0);

			return textureResource;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	private TextureResource resource;
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	private String fileName;
}
