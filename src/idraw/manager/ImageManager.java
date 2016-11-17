package idraw.manager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class ImageManager {
	// 画像をBase64エンコード
	public static String png2string(BufferedImage img_png) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img_png, "png", baos);
		baos.flush();
		byte[] img_byte = baos.toByteArray();
		baos.close();

		String img_string = Base64.getMimeEncoder().encodeToString(img_byte);

		return img_string;
	}

	// Base64コードを画像にデコード
	public static BufferedImage string2png(String img_string) throws IOException {

		byte[] img_byte = Base64.getMimeDecoder().decode(img_string);

		BufferedImage img_png = ImageIO.read((ImageInputStream) new ByteArrayInputStream(img_byte));

		return img_png;
	}
	// 二つの画像の組み合わせ
	public static String merge(String base_str, String overlay_str) throws IOException {

		BufferedImage base = string2png(base_str);
		BufferedImage overlay = string2png(overlay_str);

		int width = Math.max(base.getWidth(), overlay.getWidth());
		int heigth = Math.max(base.getHeight(), overlay.getHeight());

		BufferedImage combined = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);

		Graphics g = combined.getGraphics();
		g.drawImage(base, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);

		String combined_str = png2string(combined);

		return combined_str;
	}
}