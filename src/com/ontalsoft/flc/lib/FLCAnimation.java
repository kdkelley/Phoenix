// Generated by delombok at Sat Jul 01 03:46:04 EEST 2017
package com.ontalsoft.flc.lib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FLCAnimation {
	private FLCFile flcFile;
	private int frameCount; // number of frames
	private int delayMs; // delay between frames in milliseconds

	public FLCAnimation(String absolutePath) throws Exception {
		flcFile = new FLCFile(new File(absolutePath));
		flcFile.open();
		frameCount = flcFile.getHeader().getFrames();
		delayMs = flcFile.getHeader().getSpeed();
	}

	public List<BufferedImage> getFrames() throws IOException {
		flcFile.getReader().seek(flcFile.getHeader().getOframe1());
		flcFile.readNextChunk(); // read the first frame chunk
		flcFile.getReader().seek(flcFile.getHeader().getOframe2());
		List<BufferedImage> imagesList = new ArrayList<>(frameCount);
		int frameCount = 1;
		FLCColor[] colors;
		FLCColor color;
		BufferedImage imageOut;
		final short width = flcFile.getHeader().getWidth();
		final short height = flcFile.getHeader().getHeight();
		while (frameCount <= flcFile.getHeader().getFrames()) {
			colors = flcFile.getFramebufferCopy();
			imageOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					color = colors[x + (y * width)];
					imageOut.setRGB(x, y, color.getRgbInt());
				}
			}
			imagesList.add(imageOut);
			flcFile.readNextChunk();
			frameCount++;
		}
		return imagesList;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public int getFrameCount() {
		return this.frameCount;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public int getDelayMs() {
		return this.delayMs;
	}
}