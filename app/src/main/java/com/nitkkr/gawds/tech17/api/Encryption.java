package com.nitkkr.gawds.tech17.api;

import android.util.Base64;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encryption
{
	private static final String KEY = "whBT^Xg6{sS#d@1s_g5=5R9ONVg^UZ";
	private static final byte[] SALT = {
			(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
			(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
	};

	private static String base64Encode(byte[] bytes)
	{
		return new String(Base64.encode(Base64.encode(bytes, Base64.DEFAULT), Base64.DEFAULT));
	}

	private static byte[] base64Decode(String property) throws IOException
	{
		return Base64.decode(Base64.decode(property.getBytes(), Base64.DEFAULT), Base64.DEFAULT);
	}


	public static String Encrypt(String Data) throws Exception
	{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(KEY.toCharArray()));

		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

		return base64Encode(pbeCipher.doFinal(Data.getBytes("UTF-8")));
	}

	public static String Decrypt(String property) throws Exception
	{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(KEY.toCharArray()));

		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

		return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
	}


}
