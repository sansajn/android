package com.base.engine.core;

import com.base.engine.rendering.Vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Util {
	public static IntBuffer createBuffer(int[] value) {
		IntBuffer buf = createIntBuffer(value.length * 4);
		buf.put(value);
		buf.position(0);
		return buf;
	}

	public static FloatBuffer createBuffer(float[] value) {
		FloatBuffer buf = createFloatBuffer(value.length * 4);
		buf.put(value);
		buf.position(0);
		return buf;
	}

	public static FloatBuffer createBuffer(Vertex[] vertices) {
		FloatBuffer buf = createFloatBuffer(vertices.length * Vertex.SIZE * 4);
		for (int i = 0; i < vertices.length; ++i)	{
			buf.put(vertices[i].getPos().getX());
			buf.put(vertices[i].getPos().getY());
			buf.put(vertices[i].getPos().getZ());
			buf.put(vertices[i].getTexCoord().getX());
			buf.put(vertices[i].getTexCoord().getY());
			buf.put(vertices[i].getNormal().getX());
			buf.put(vertices[i].getNormal().getY());
			buf.put(vertices[i].getNormal().getZ());
		}
		buf.position(0);
		return buf;
	}

	public static IntBuffer createIntBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asIntBuffer();
	}

	public static FloatBuffer createFloatBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}

	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}

	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];
		for (int i = 0; i < data.length; ++i)
			result[i] = data[i].intValue();
		return result;
	}

	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < data.length; ++i)
			if (!data[i].equals(""))
				result.add(data[i]);

		String[] res = new String[result.size()];
		result.toArray(res);

		return res;
	}
}
