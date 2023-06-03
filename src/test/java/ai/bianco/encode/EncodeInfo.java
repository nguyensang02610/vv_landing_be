package ai.bianco.encode;
import org.apache.commons.codec.binary.Base64;

public class EncodeInfo {
	public static void main(String[] args) {

		String str = "shopid=12123323&token=8slJqZEN-kf-WmV1BvOnjD_DHKGgG2Ak";
		// Encode data on your side using BASE64
		byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
		System.out.println("encoded value is " + new String(bytesEncoded));

		
		String fbres = "c2hvcGlkPTEyMTIzMzIzJnRva2VuPThzbEpxWkVOLWtmLVdtVjFCdk9uakRfREhLR2dHMkFr=#_=_1";
		// Decode data on other side, by processing encoded data
		byte[] valueDecoded = Base64.decodeBase64(fbres.getBytes());
		System.out.println("Decoded value is " + new String(valueDecoded));
	}
}
